package citu.teknoybuyandselluser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TAG = "LoginActivity";

    private EditText txtUsername;
    private EditText txtPassword;

    private Button  btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(v);
            }
        });
    }

    public void onLogin(View view){
        Map<String,String> data = new HashMap<>();

        data.put(USERNAME,txtUsername.getText().toString());
        data.put(PASSWORD,txtPassword.getText().toString());

        Server.login(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.v(TAG, "Successful Login :)");
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.e(TAG, "Error : " + statusCode);
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
