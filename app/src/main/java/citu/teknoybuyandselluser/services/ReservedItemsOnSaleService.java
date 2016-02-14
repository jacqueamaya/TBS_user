package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.ReservedItemOnSale;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 ** Created by jack on 8/02/16.
 */
public class ReservedItemsOnSaleService extends ConnectionService {
    public static final String TAG = "ReservedItemsOnSale";
    public static final String ACTION = ReservedItemsOnSaleService.class.getCanonicalName();

    public ReservedItemsOnSaleService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting Reserved Items on Sale...");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<ReservedItemOnSale>> call = service.getReservedItemsOnSale(username);
            Response<List<ReservedItemOnSale>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<ReservedItemOnSale> items = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(ReservedItemOnSale.class).findAll().clear();
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
