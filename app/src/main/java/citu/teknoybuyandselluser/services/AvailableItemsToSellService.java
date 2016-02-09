package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.AvailableItemToSell;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 ** Created by jack on 6/02/16.
 */
public class AvailableItemsToSellService extends ConnectionService {
    public static final String TAG = "AvailableItemsToSell";
    public static final String ACTION = AvailableItemsToSellService.class.getCanonicalName();

    public AvailableItemsToSellService() {
        super(TAG + "Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Available Items to Sell...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<AvailableItemToSell>> call = service.getAvailableItemsToSell(username);
            Response<List<AvailableItemToSell>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<AvailableItemToSell> items = response.body();
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
