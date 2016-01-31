package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CheckBox chkAgreeTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        chkAgreeTerms = (CheckBox) findViewById(R.id.terms);
    }

    public void onRegister (View view) {
        Map<String, String> data = new HashMap<>();

        EditText mTxtStudentId = (EditText) findViewById(R.id.txtStudentID);
        EditText mTxtFirstName = (EditText) findViewById(R.id.txtFirstName);
        EditText mTxtLastName = (EditText) findViewById(R.id.txtLastName);
        EditText mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        EditText mTxtPassword = (EditText) findViewById(R.id.txtPassword);

        String studentId = mTxtStudentId.getText().toString().trim();
        String firstName = mTxtFirstName.getText().toString().trim();
        String lastName = mTxtLastName.getText().toString().trim();
        String username = mTxtUsername.getText().toString().trim();
        String password = mTxtPassword.getText().toString();

        if(!("".equals(studentId))
                && !("".equals(firstName))
                && !("".equals(lastName))
                && !("".equals(username))
                && !("".equals(password))) {

            data.put(Constants.User.ID_NUMBER, studentId);
            data.put(Constants.User.FIRST_NAME, firstName);
            data.put(Constants.User.LAST_NAME, lastName);
            data.put(Constants.User.USERNAME, username);
            data.put(Constants.User.PASSWORD, password);

            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait. . .");

            if (chkAgreeTerms.isChecked()) {
                Server.register(data, mProgressDialog, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.v(TAG, responseBody);
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            String response = json.getString("statusText");
                            if (response.equals("User created")) {
                                startActivity(LoginActivity.class);
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
        } else
            Toast.makeText(MainActivity.this, "Some input parameters are missing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(LoginActivity.class);
    }

    public void onCancel (View view) {
        onBackPressed();
    }

    public void startActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }

    public void openDialog(View view) {
        if (chkAgreeTerms.isChecked()) {
            AlertDialog.Builder termsAndConditions = new AlertDialog.Builder(this);
            termsAndConditions.setTitle("TBS Terms and Conditions");
            termsAndConditions.setMessage("USER in General" + "\n" +
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
            AlertDialog alert = termsAndConditions.create();
            alert.show();

        }
    }
}
