package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.SellItem;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;


public class ItemsToSellService extends ConnectionService {
    public static final String TAG = "ItemsToSellService";
    public static final String ACTION = ItemsToSellService.class.getCanonicalName();

    public ItemsToSellService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Items to Sell...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<SellItem>> call = service.getItemsToSell(username);
            Response<List<SellItem>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<SellItem> items = response.body();
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
