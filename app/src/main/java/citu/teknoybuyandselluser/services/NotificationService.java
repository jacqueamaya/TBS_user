package citu.teknoybuyandselluser.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.Notification;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.Call;
import retrofit.Response;

public class NotificationService extends IntentService {
    public static final String TAG = "NotificationService";

    public NotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "getting notifications. . . ");
        String username = intent.getStringExtra(Constants.User.USERNAME);
        TBSUserInterface service = ServiceManager.getInstance();
        try {

            Call<List<Notification>> call = service.getNotifications(username);
            Response<List<Notification>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Notification> notifications = response.body();
                Log.e(TAG, response.body().toString());

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(Notification.class).findAll().clear();
                realm.copyToRealmOrUpdate(notifications);
                realm.commitTransaction();
                realm.close();

                notifySuccess("Successful");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Error: " + error);
                notifyFailure("Error");
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            notifyFailure("Unable to connect to server");
        }
    }

    protected void notifySuccess(String responseBody){
        Intent intent = new Intent(NotificationService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, 1);
        intent.putExtra(Constants.RESPONSE, responseBody);
        sendBroadcast(intent);
    }

    protected void notifyFailure(String responseBody){
        Intent intent = new Intent(NotificationService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, -1);
        intent.putExtra(Constants.RESPONSE,  responseBody);
        sendBroadcast(intent);
    }
}
