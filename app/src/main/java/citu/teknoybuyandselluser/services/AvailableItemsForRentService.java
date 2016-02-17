package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.AvailableItemForRent;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 ** Created by jack on 6/02/16.
 */
public class AvailableItemsForRentService extends ConnectionService {
    public static final String TAG = "AvailableItemsForRent";
    public static final String ACTION = AvailableItemsForRentService.class.getCanonicalName();

    public AvailableItemsForRentService() {
        super(TAG + "Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Available Items for Rent...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<AvailableItemForRent>> call = service.getAvailableItemsForRent(username);
            Response<List<AvailableItemForRent>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<AvailableItemForRent> items = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(AvailableItemForRent.class).findAll().clear();
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
