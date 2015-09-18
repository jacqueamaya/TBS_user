package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String OLD_PASSWORD = "old_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CONFIRM_PASSWORD = "confirm_password";

    private EditText txtUsername;
    private EditText txtOldPassword;
    private EditText txtNewPassword;
    private EditText txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
        txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
    }

    public void onCancel (View view) {
        finish();
    }

    public void onChangePassword(View view){
        Map<String,String> data = new HashMap<>();

        data.put(USERNAME,txtUsername.getText().toString());
        data.put(OLD_PASSWORD,txtOldPassword.getText().toString());
        data.put(NEW_PASSWORD,txtNewPassword.getText().toString());
        data.put(CONFIRM_PASSWORD, txtConfirmPassword.getText().toString());

        Server.changePassword(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                intent.putExtra("error", responseBody);
                finish();
                startActivity(intent);
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(ChangePasswordActivity.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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
