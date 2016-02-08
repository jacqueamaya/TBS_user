package citu.teknoybuyandselluser.services;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ServiceManager;
import citu.teknoybuyandselluser.models.ResponseStatus;
import citu.teknoybuyandselluser.models.Student;
import citu.teknoybuyandselluser.models.User;
import citu.teknoybuyandselluser.models.UserProfile;
import retrofit.Call;
import retrofit.Response;

public class LoginService extends ConnectionService {
    public static final String TAG = "LoginService";
    public static final String ACTION = LoginService.class.getCanonicalName();

    public LoginService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "handle intent");
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
                    getUser(service, username);
                } else {
                    Log.e(TAG, "Login Error: " + statusText);
                    notifyFailure(ACTION, "Invalid username or password");
                }
            } else {
                Log.e(TAG, "HTTP " + statusCode);
                Log.e(TAG, response.errorBody().string());
                notifyFailure(ACTION, "Invalid username or password");
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Unable to connect to server");
        }
    }

    protected void getUser(TBSUserInterface service, String username) {
        try {
            Log.e(TAG, username);
            Call<List<UserProfile>> call = service.getUser(username);
            Response<List<UserProfile>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<UserProfile> user = response.body();
                Log.e(TAG, response.body().toString());

                UserProfile userProfile = user.get(0);
                notifySuccess(userProfile);
                Log.e(TAG, "Successful login");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Get user Error: " + error);
                notifyFailure(ACTION, "User not found");
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Unable to connect to server");
        }
    }

    protected void notifySuccess(UserProfile userProfile) {
        Student student = userProfile.getStudent();
        User user = userProfile.getUser();
        Intent intent = new Intent(LoginService.class.getCanonicalName());
        intent.putExtra(Constants.RESULT, 1);
        intent.putExtra(Constants.User.USERNAME, user.getUsername());
        intent.putExtra(Constants.User.USERNAME, user.getUsername());
        intent.putExtra(Constants.User.FIRST_NAME, student.getFirst_name());
        intent.putExtra(Constants.User.LAST_NAME, student.getLast_name());
        intent.putExtra(Constants.User.STARS_COLLECTED, userProfile.getStars_collected());
        intent.putExtra(Constants.User.PICTURE, userProfile.getPicture());
        sendBroadcast(intent);
    }
}
