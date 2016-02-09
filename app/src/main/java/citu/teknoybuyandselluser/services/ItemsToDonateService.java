package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.DonateItem;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;


public class ItemsToDonateService extends ConnectionService {
    public static final String TAG = "ItemsToDonateService";
    public static final String ACTION = ItemsToDonateService.class.getCanonicalName();

    public ItemsToDonateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Items to Donate...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<DonateItem>> call = service.getItemsToDonate(username);
            Response<List<DonateItem>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<DonateItem> items = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(items);
                realm.commitTransaction();
                realm.close();

                notifySuccess(ACTION, "Successful");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Error: " + error);
                notifyFailure(ACTION, "Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
