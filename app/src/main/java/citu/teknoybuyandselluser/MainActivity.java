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
            termsAndConditions.setMessage("USER" + "\n" +
                            "1.\tUser should be a bona fide student of Cebu Institute of Technology University.\n" +
                            "2.\tUser should register an account in order to use the mobile application.\n" +
                            "3.\tA user can be a seller, a buyer, a renter, and a donor depending on the transaction he is going to make. \n" +
                            "4.\tA user receives a certain number of stars as incentive for making a transaction. " +
                            "When an item has been bought, the stars that will be given to the owner will be 10% of the price of the item. The stars that will be given to the buyer will also be 10% of the price of the item." +
                            "When an item has been rented, the stars that will be given to the owner will be 10% of the price of the item. The stars that will be given to the renter will also be 10% of the price of the item." +
                            "When a buyer buys an item with discount, he will not be awarded with stars. " +
                            "These stars can also be used to get an award which will be coming from the donated items. When a user donates an item, the stars he will receive will be 20% of the stars set by the administrator.\n" +
                            "5.\tIn every transaction, TBS developers will get a 10% share and the remaining 90% will be for the users. When an item has been bought or rented, 10% of the money will go to the TBS developers and 90% will go to the users.  " +
                            "In case of unreturned rented items on the set expiration date, penalties will be fined to the renters. 10% of the penalty will go to the TBS developers and 90% will go to the user. If the user cannot
                            return the rented item 14 days after its expiration date, the user's account will be blocked. This means that the user cannot make any transactions may it be selling, buying, renting or donating. The user's account
                            will only be unblocked if he has returned the rented item with its corresponding penalty.\n" +
                            "\nADMINISTRATOR\n" + 
                            "1.\tThe administrator takes control of the transactions made by the users.\n" +
                            "2.\tThe administrator has the right to approve or deny requested items for sale, for rent and for donation. " +
                            "An item will be approved if it is of good condition and if the information you have entered about the item is true and correct.\n ")
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
