package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.PendingItem;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;


public class PendingItemsService extends ConnectionService {
    public static final String TAG = "PendingItemsService";
    public static final String ACTION = PendingItemsService.class.getCanonicalName();

    public PendingItemsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Pending Items...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<PendingItem>> call = service.getPendingItems(username);
            Response<List<PendingItem>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<PendingItem> items = response.body();
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
