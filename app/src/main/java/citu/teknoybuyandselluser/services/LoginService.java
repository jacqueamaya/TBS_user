package citu.teknoybuyandselluser.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.ResponseStatus;
import retrofit.Call;
import retrofit.Response;

public class LoginService extends IntentService {
    public static final String TAG = "LoginService";

    public LoginService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String username = intent.getStringExtra(Constants.User.USERNAME);
        String password = intent.getStringExtra(Constants.User.PASSWORD);

        TBSUserInterface service = ServiceManager.getInstance();

        try {
            Call<ResponseStatus> call = service.login(username, password);
            Response<ResponseStatus> response = call.execute();

            int statusCode = response.code();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                ResponseStatus status = response.body();

                String statusText = status.getStatusText();
                if (status.getStatus() == HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "status code: " + status.getStatus());
                    Log.i(TAG, "Login Success: " + statusText);
                    notifySuccess(statusText);
                } else {
                    Log.e(TAG, "Login Error: " + statusText);
                    notifyFailure(statusText);
                }
            } else {
                Log.e(TAG, "HTTP " + statusCode);
                Log.e(TAG, response.errorBody().string());
                notifyFailure(response.errorBody().string());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(e.getMessage());
        }
    }

    protected void notifySuccess(String responseBody) {
        Intent intent = new Intent(LoginService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, 1);
        intent.putExtra(Constants.RESPONSE, responseBody);
        sendBroadcast(intent);
    }

    protected void notifyFailure(String responseBody) {
        Intent intent = new Intent(LoginService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, -1);
        intent.putExtra(Constants.RESPONSE, responseBody);
        sendBroadcast(intent);
    }
}
