package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.Category;

import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 ** Created by jack on 9/02/16.
 */
public class AllCategoriesService extends ConnectionService {
    public static final String TAG = "AllCategoriesService";
    public static final String ACTION = AllCategoriesService.class.getCanonicalName();

    public AllCategoriesService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Getting All Categories...");
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<List<Category>> call = service.getCategories();
            Response<List<Category>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Category> categories = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(categories);
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
