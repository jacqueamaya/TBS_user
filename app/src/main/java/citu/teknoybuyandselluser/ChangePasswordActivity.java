package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String OLD_PASSWORD = "old_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CONFIRM_PASSWORD = "confirm_password";

    private EditText mTxtUsername;
    private EditText mTxtOldPassword;
    private EditText mTxtNewPassword;
    private EditText mTxtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
        mTxtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        mTxtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
    }

    public void onCancel (View view) {
        finish();
    }

    public void onChangePassword(View view){
        Map<String,String> data = new HashMap<>();

        data.put(USERNAME, mTxtUsername.getText().toString());
        data.put(OLD_PASSWORD, mTxtOldPassword.getText().toString());
        data.put(NEW_PASSWORD, mTxtNewPassword.getText().toString());
        data.put(CONFIRM_PASSWORD, mTxtConfirmPassword.getText().toString());

        Server.changePassword(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                JSONObject json = null;
                try {
                    json = new JSONObject(responseBody);
                    String response = json.getString("statusText");
                    if(response.equals("Password changed")) {
                        Intent intent;
                        intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        intent.putExtra("error", responseBody);
                        finish();
                        startActivity(intent);
                    }
                    Toast.makeText(ChangePasswordActivity.this, response, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(ChangePasswordActivity.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
