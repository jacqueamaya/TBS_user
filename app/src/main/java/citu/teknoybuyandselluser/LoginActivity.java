package citu.teknoybuyandselluser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import citu.teknoybuyandselluser.services.LoginService;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private TextView mTxtErrorMessage;
    private ProgressBar progressBar;

    private String mStrUsername;
    private String mStrPassword;

    private LoginBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);
        TextView txtSignUp = (TextView) findViewById(R.id.txtSignup);
        mTxtErrorMessage = (TextView) findViewById(R.id.txtLoginErrorMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressLogin);

        SharedPreferences sp = this.getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        if(sp.getString(Constants.User.USERNAME, null) != null) {
            Intent intent;
            intent = new Intent(this, NotificationsActivity.class);
            finish();
            startActivity(intent);
        } else {
            mReceiver = new LoginBroadcastReceiver();

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
    }

    public void onLogin(View view){
        /*mLoginProgress.setIndeterminate(true);
        mLoginProgress.setCancelable(false);
        mLoginProgress.setMessage("Please wait. . .");*/

        mStrUsername = mTxtUsername.getText().toString().trim();
        mStrPassword = mTxtPassword.getText().toString();
        loginUser();
    }

    public void loginUser() {

        if (mStrUsername.isEmpty() || mStrPassword.isEmpty() || "".equals(mStrUsername.trim()) || "".equals(mStrPassword.trim())) {
            Toast.makeText(LoginActivity.this, "Please input username and password", Toast.LENGTH_SHORT).show();
        } else {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                //mLoginProgress.show();
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(this, LoginService.class);
                intent.putExtra(Constants.User.USERNAME, mStrUsername);
                intent.putExtra(Constants.User.PASSWORD, mStrPassword);
                startService(intent);
                Log.e(TAG, "starting service");
            } else {
                Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(LoginService.class.getCanonicalName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(Constants.RESULT, 0);
            int starsCollected = intent.getIntExtra(Constants.User.STARS_COLLECTED, 0);
            String username = intent.getStringExtra(Constants.User.USERNAME);
            String password = intent.getStringExtra(Constants.User.PASSWORD);
            String firstName = intent.getStringExtra(Constants.User.FIRST_NAME);
            String lastName = intent.getStringExtra(Constants.User.LAST_NAME);
            String picture = intent.getStringExtra(Constants.User.PICTURE);

            //mLoginProgress.hide();
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(Constants.User.USERNAME, username);
                editor.putString(Constants.User.PASSWORD, password);
                editor.putString(Constants.User.FIRST_NAME, firstName);
                editor.putString(Constants.User.LAST_NAME, lastName);
                editor.putString(Constants.User.PICTURE, picture);
                editor.putInt(Constants.User.STARS_COLLECTED, starsCollected);
                editor.apply();
                finish();
                startActivity(new Intent(LoginActivity.this, NotificationsActivity.class));
            } else {
                String response = intent.getStringExtra(Constants.RESPONSE);
                mTxtErrorMessage.setVisibility(View.VISIBLE);
                mTxtErrorMessage.setText(response);
            }
        }
    }
}
