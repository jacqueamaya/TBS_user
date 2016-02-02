package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.services.LoginService;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private TextView mTxtErrorMessage;
    private ProgressDialog mLoginProgress;
    private ProgressBar mProgressBar;

    private String mStrUsername;
    private String mStrPassword;
    private Map<String,String> data = new HashMap<>();

    private LoginBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetUser);
        mProgressBar.setVisibility(View.GONE);
        mLoginProgress = new ProgressDialog(this);

        SharedPreferences sp = this.getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        if(sp.getString(Constants.User.USERNAME, null) != null && sp.getString(Constants.User.PASSWORD, null) != null) {
            Intent intent;
            intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
        }

        mReceiver = new LoginBroadcastReceiver();

        TextView txtSignUp = (TextView) findViewById(R.id.txtSignup);
        mTxtErrorMessage = (TextView) findViewById(R.id.txtLoginErrorMessage);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, MainActivity.class);
                mTxtPassword.setText("");
                startActivity(intent);
            }
        });
    }

    public void onLogin(View view){
        mLoginProgress.setIndeterminate(true);
        mLoginProgress.setCancelable(false);
        mLoginProgress.setMessage("Please wait. . .");
        mStrUsername = mTxtUsername.getText().toString().trim();
        mStrPassword = mTxtPassword.getText().toString();
        loginUser();
    }

    public void loginUser() {
        data.put(Constants.User.USERNAME, mStrUsername);
        data.put(Constants.User.PASSWORD, mStrPassword);

        if (mStrUsername.isEmpty() || mStrPassword.isEmpty() || "".equals(mStrUsername.trim()) || "".equals(mStrPassword.trim())) {
            Toast.makeText(LoginActivity.this, "Please input username and password", Toast.LENGTH_SHORT).show();
        } else {
            /*ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                mLoginProgress.show();

                Intent intent = new Intent(this, LoginService.class);
                intent.putExtra(Constants.User.USERNAME, mStrUsername);
                intent.putExtra(Constants.User.PASSWORD, mStrPassword);
                startService(intent);
                Log.e(TAG, "starting service");
            } else {
                Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }*/

            Server.login(data, mLoginProgress, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String response = json.getString("statusText");
                        if (response.equals("Successful Login")) {
                            getUser();
                        } else {
                            mTxtErrorMessage.setText(response);
                            mTxtPassword.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    mTxtPassword.setText("");
                    mTxtErrorMessage.setText("Unable to connect to server");
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getUser () {
        Server.getUser(mStrUsername, mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(responseBody);
                    if(jsonArray.length() != 0) {
                        JSONObject json = jsonArray.getJSONObject(0);
                        JSONObject jsonUser = json.getJSONObject("student");

                        SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString(Constants.User.USERNAME, mStrUsername);
                        editor.putString(Constants.User.PASSWORD, mStrPassword);
                        editor.putString(Constants.User.FIRST_NAME, jsonUser.getString(Constants.User.FIRST_NAME));
                        editor.putString(Constants.User.LAST_NAME, jsonUser.getString(Constants.User.LAST_NAME));
                        editor.putInt(Constants.User.STARS_COLLECTED, json.getInt(Constants.User.STARS_COLLECTED));
                        editor.putString(Constants.PICTURE, json.getString(Constants.PICTURE));
                        editor.apply();

                        mTxtErrorMessage.setText("");

                        Intent intent;
                        intent = new Intent(LoginActivity.this, NotificationsActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        mTxtPassword.setText("");
                        mTxtErrorMessage.setText("User not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Error: " + statusCode + " " + responseBody);
            }
        });
    }

    private class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(Constants.RESULT, 0);
            String statusText = intent.getStringExtra(Constants.RESPONSE);

            Log.e(TAG,result+"");
            Log.e(TAG,statusText);

            if (result == 1) {
                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(Constants.User.USERNAME, Utils.capitalize(mStrUsername));
                editor.apply();

                startActivity(new Intent(LoginActivity.this, NotificationsActivity.class));
                finish();
            } else {
                mLoginProgress.hide();
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
