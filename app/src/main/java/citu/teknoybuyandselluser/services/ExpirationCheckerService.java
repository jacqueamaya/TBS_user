package citu.teknoybuyandselluser.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.ResponseStatus;
import retrofit.Call;
import retrofit.Response;

public class ExpirationCheckerService extends IntentService {

    public static final String TAG = "ExpirationCheckerSvc";

    public ExpirationCheckerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TBSUserInterface service = ServiceManager.getInstance();
        try {
            Call<ResponseStatus> call = service.checkExpiration();
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Log.e(TAG, "Successfully checked expiration.");
            } else{
                Log.e(TAG, "Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
