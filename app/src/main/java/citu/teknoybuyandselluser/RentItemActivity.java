package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RentItemActivity extends AppCompatActivity {

    private int mQuantity;
    private Map<String, String> data;
    private String mItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_rent_item);

        setupToolbar();

        Intent intent;
        intent = getIntent();
        int itemId = intent.getIntExtra(Constants.Item.ID, 0);
        mItemName = intent.getStringExtra(Constants.Item.ITEM_NAME);
        mQuantity = intent.getIntExtra(Constants.Item.QUANTITY, 1);
        String availableQuantity = mQuantity + "";
        String description = intent.getStringExtra(Constants.Item.DESCRIPTION);
        String formatPrice = "Php " + intent.getStringExtra(Constants.Item.FORMAT_PRICE);
        String picture = intent.getStringExtra(Constants.Item.PICTURE);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtAvailableQuantity = (TextView) findViewById(R.id.txtAvailableQuantity);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        Button btnRentItem = (Button) findViewById(R.id.btnRentItem);
        ImageView imgItem = (ImageView) findViewById(R.id.imgItem);

        txtItem.setText(mItemName);
        txtAvailableQuantity.setText(availableQuantity);
        txtDescription.setText(description);
        txtPrice.setText(formatPrice);

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgItem);

        setTitle(mItemName);

        data = new HashMap<>();

        String user = getUserName();
        data.put(Constants.Item.RENTER, user);
        data.put(Constants.Item.ID, "" + itemId);

        btnRentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRent(v);
            }
        });
    }

    public void onRent(View view) {
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        int quantity = 0;
        if(!"".equals(txtQuantity.getText().toString()))
            quantity = Integer.parseInt(txtQuantity.getText().toString());

        if(quantity <= mQuantity && quantity > 0) {
            data.put(Constants.Item.QUANTITY, quantity + "");
            Server.rentItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getInt("status") == 201) {
                            //Toast.makeText(RentItemActivity.this, mItemName + " is now reserved.", Toast.LENGTH_SHORT).show();
                            showAlertDialog();
                        } else {
                            Toast.makeText(RentItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Toast.makeText(RentItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(RentItemActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
        }
    }

    public String getUserName() {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        return mSharedPreferences.getString(Constants.User.USERNAME, "");
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void showAlertDialog() {
        AlertDialog.Builder rentItem = new AlertDialog.Builder(this);
        rentItem.setTitle("Rent Item Reminder");
        rentItem.setMessage(Utils.capitalize(mItemName) + ", is now reserved. " +
                "Please wait within three(3) days for the renter to give the item to TBS Admin. " +
                "Otherwise, your reservation will expire and will be deleted from Reserved Items for Rent list.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        RentItemActivity.this.finish();
                    }
                });
        rentItem.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
