package citu.teknoybuyandselluser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private EditText txtUsername;
    private EditText txtPassword;

    private TextView txtSignUp;
    private TextView txtForgotPassword;
    private TextView txtErrorMessage;

    private String strUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtSignUp = (TextView) findViewById(R.id.txtSignup);
        txtErrorMessage = (TextView) findViewById(R.id.txtLoginErrorMessage);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                txtPassword.setText("");
                startActivity(intent);
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, MainActivity.class);
                txtPassword.setText("");
                startActivity(intent);
            }
        });
    }

    public void onLogin(View view){
        Map<String,String> data = new HashMap<>();
        strUsername = txtUsername.getText().toString();
        data.put(USERNAME,strUsername);
        data.put(PASSWORD,txtPassword.getText().toString());

        Server.login(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "LOGIN success");

                Server.getUser(strUsername, new Ajax.Callbacks(){
                    @Override
                    public void success(String responseBody) {
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            JSONObject json = jsonArray.getJSONObject(0);
                            Log.d(TAG, json.toString());
                            JSONObject jsonUser = json.getJSONObject("student");

                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("first_name", jsonUser.getString("first_name"));
                            editor.putString("last_name", jsonUser.getString("last_name"));
                            editor.putInt("stars_collected", json.getInt("stars_collected"));
                            editor.apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(int statusCode, String responseBody, String statusText) {
                        Log.d(TAG, "Error: " + statusCode + " " + responseBody);
                    }
                });
                txtErrorMessage.setText("");

                Intent intent;
                intent = new Intent(LoginActivity.this, DashboardActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "LOGIN error " + responseBody);
                //Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
                txtErrorMessage.setText("");
                txtErrorMessage.setText("Invalid username or password");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
