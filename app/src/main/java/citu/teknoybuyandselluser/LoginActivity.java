package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPreferences";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private static final String TAG = "LoginActivity";

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private TextView mTxtSignUp;
    private TextView mTxtForgotPassword;
    private TextView mTxtErrorMessage;
    private ProgressDialog mLoginProgress;

    private String mStrUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);

        mLoginProgress = new ProgressDialog(this);

        SharedPreferences sp = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if(sp.getString(USERNAME, null) != null) {
            Intent intent;
            intent = new Intent(LoginActivity.this, NotificationsActivity.class);
            finish();
            startActivity(intent);
        }

        mTxtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        mTxtSignUp = (TextView) findViewById(R.id.txtSignup);
        mTxtErrorMessage = (TextView) findViewById(R.id.txtLoginErrorMessage);

        mTxtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                mTxtPassword.setText("");
                startActivity(intent);
            }
        });

        mTxtSignUp.setOnClickListener(new View.OnClickListener() {
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
        Map<String,String> data = new HashMap<>();
        mStrUsername = mTxtUsername.getText().toString();
        data.put(USERNAME, mStrUsername);
        data.put(PASSWORD, mTxtPassword.getText().toString());

        mLoginProgress.setIndeterminate(true);
        mLoginProgress.setMessage("Please wait. . .");

        Server.login(data, mLoginProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "LOGIN: " + responseBody);
                try {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressGetUser);
                    progressBar.setVisibility(View.GONE);
                    JSONObject json = new JSONObject(responseBody);
                    String response = json.getString("statusText");
                    if(response.equals("Successful Login")) {
                        Server.getUser(mStrUsername, progressBar, new Ajax.Callbacks() {
                            @Override
                            public void success(String responseBody) {
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(responseBody);
                                    if(jsonArray.length() != 0) {
                                        JSONObject json = jsonArray.getJSONObject(0);
                                        JSONObject jsonUser = json.getJSONObject("student");

                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString(Constants.USERNAME, mStrUsername);
                                        editor.putString(Constants.FIRST_NAME, jsonUser.getString("first_name"));
                                        editor.putString(Constants.LAST_NAME, jsonUser.getString("last_name"));
                                        editor.putInt(Constants.STARS_COLLECTED, json.getInt("stars_collected"));
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
                    } else {
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
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
                mTxtErrorMessage.setText("Cannot connect to server");
            }
        });
    }
}
