package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.RentedItem;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

public class RentedItemsService extends ConnectionService {
    public static final String TAG = "RentedItemsService";
    public static final String ACTION = RentedItemsService.class.getCanonicalName();

    public RentedItemsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Rented Items...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<RentedItem>> call = service.getRentedItems(username);
            Response<List<RentedItem>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<RentedItem> items = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(RentedItem.class).findAll().clear();
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
