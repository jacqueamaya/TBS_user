package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String ID_NUMBER = "id_number";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String TAG = "MainActivity";

    private EditText mTxtStudentId;
    private EditText mTxtFirstName;
    private EditText mTxtLastName;
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private ProgressDialog mProgressDialog;

    private RadioButton terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        mTxtStudentId = (EditText) findViewById(R.id.txtStudentID);
        mTxtFirstName = (EditText) findViewById(R.id.txtFirstName);
        mTxtLastName = (EditText) findViewById(R.id.txtLastName);
        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);

        mProgressDialog = new ProgressDialog(this);

        terms = (RadioButton) findViewById(R.id.terms);
    }

    public void onRegister (View view) {
        if ("".equals(mTxtUsername.getText().toString().trim())) {
            Utils.alertInfo(MainActivity.this, "Please input username");
        }
        else{
            register();
        }
    }

    public void register()
    {
        Map<String, String> data = new HashMap<>();

        data.put(ID_NUMBER, mTxtStudentId.getText().toString());
        data.put(FIRST_NAME, mTxtFirstName.getText().toString());
        data.put(LAST_NAME, mTxtLastName.getText().toString());
        data.put(USERNAME, mTxtUsername.getText().toString());
        data.put(PASSWORD, mTxtPassword.getText().toString());

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        if(terms.isChecked()) {
            Server.register(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    Log.v(TAG, responseBody);
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String response = json.getString("statusText");
                        if(response.equals("User created")) {
                            Intent intent;
                            intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Log.e(TAG, "Error : " + statusCode);
                    Toast.makeText(MainActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please agree to the terms and conditions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancel (View view) {
        finish();
    }

    public void openDialog(View view){

        AlertDialog.Builder termsandconditions= new AlertDialog.Builder(this);
        termsandconditions.setTitle("TBS Terms and Conditions");
        termsandconditions.setMessage("USER in General" + "\n" +
                "1.\tUser should be a bona fide student of Cebu Institute of Technology University.\n" +
                "2.\tUser should register an account in order to use the mobile application.\n" +
                "3.\tA user can be a seller, a buyer, and a donor depending on the transaction he is going to make. User becomes a seller when he sells an item to his prospect buyers. User becomes a buyer when he buys items available in the application. User becomes a donor when he donates an item to his fellow TBS users.\n" +
                "4.\tA user receives a certain number of stars as incentive for making a transaction. These stars can be used to get discount from items. These can also be used to get an award which will be coming from the donated items.\n" +
        "\nADMINISTRATOR\n" +
                 "1.\tThe administrator takes control of the transactions made by the users.\n" +
                 "2.\tIn case a user sells an item, the administrator will receive the form submitted by the seller. He shall wait for the seller’s item and when the item is on-hand, he shall check its condition. If the item is in good condition, he shall approve the item to be sold and set the stars for that certain item. If the item is not in good condition, he shall reject the item.\n" +
        "\nSELLER\n" +
                        "1.\tWhen user sells an item, he shall fill-up the “Sell Item” form and submit it to the administrator. He shall meet the administrator within 3 days upon his submission of the “Sell Item” form or else, his submission of the form will become void.\n" +
                        "2.\tWhen the seller’s item has been bought, he will receive a notification from the administrator and that he must hand the item.\n" +
                        "3.\tWhen the seller’s item has been claimed by the buyer, he will receive another notification from the administrator and that he should claim his money.\n" +
                        "4.\tFor every time the seller sells an item, he gets a certain number of stars which will be determined by the administrator.\n" +
        "\nBUYER\n" +
                        "1.\tWhen a user buys an item, he shall wait for a notification coming from the administrator telling him that the item is already on-hand and that he can now claim the item.\n" +
                        "2.\tA buyer can get a discount depending on the required stars of the item and his collected stars. If the stars he collected are enough for the item, then he can get a discount.\n"
        )
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = termsandconditions.create();
        alert.show();

    }
}
