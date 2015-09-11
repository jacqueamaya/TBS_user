package citu.teknoybuyandselluser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String ID_NUMBER = "id_number";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String TAG = "MainActivity";

    private EditText txtStudentId;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtUsername;
    private EditText txtPassword;

    private String address;
    private Button btnRegister;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStudentId = (EditText) findViewById(R.id.txtStudentID);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister(v);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel(v);
            }
        });
    }

    public void onRegister (View view) {
        Map<String, String> data = new HashMap<>();

        data.put(ID_NUMBER, txtStudentId.getText().toString());
        data.put(FIRST_NAME, txtFirstName.getText().toString());
        data.put(LAST_NAME, txtLastName.getText().toString());
        data.put(USERNAME, txtUsername.getText().toString());
        data.put(PASSWORD, txtPassword.getText().toString());

        Server.register(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.v(TAG, "Success :)");
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.e(TAG, "Error : " + statusCode);
            }
        });
    }

    public void onCancel (View view) {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
