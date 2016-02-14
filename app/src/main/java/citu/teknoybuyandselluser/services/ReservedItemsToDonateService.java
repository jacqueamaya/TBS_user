package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.ReservedItemToDonate;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 ** Created by jack on 5/02/16.
 */
public class ReservedItemsToDonateService extends ConnectionService {
    public static final String TAG = "ReservedItemsToDonate";
    public static final String ACTION = ReservedItemsToDonateService.class.getCanonicalName();

    public ReservedItemsToDonateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Reserved Items to Donate...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<ReservedItemToDonate>> call = service.getReservedItemsForDonation(username);
            Response<List<ReservedItemToDonate>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<ReservedItemToDonate> items = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(ReservedItemToDonate.class).findAll().clear();
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
