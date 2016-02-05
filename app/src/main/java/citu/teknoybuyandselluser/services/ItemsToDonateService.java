package citu.teknoybuyandselluser.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.Item;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.Call;
import retrofit.Response;


public class ItemsToDonateService extends IntentService {
    public static final String TAG = "ItemsToDonateService";

    public ItemsToDonateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Items to Donate...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<Item>> call = service.getItemsToDonate(username);
            Response<List<Item>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Item> items = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getInstance(new RealmConfiguration.Builder(this).build());
                realm.beginTransaction();
                realm.where(Item.class).findAll().clear();
                realm.copyToRealmOrUpdate(items);
                realm.commitTransaction();
                realm.close();

                notifySuccess("Successful");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Error: " + error);
                notifyFailure("Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void notifySuccess(String responseBody){
        Intent intent = new Intent(ItemsToDonateService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, 1);
        intent.putExtra(Constants.RESPONSE, responseBody);
        sendBroadcast(intent);
    }

    protected void notifyFailure(String responseBody){
        Intent intent = new Intent(ItemsToDonateService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, -1);
        intent.putExtra(Constants.RESPONSE,  responseBody);
        sendBroadcast(intent);
    }
}
