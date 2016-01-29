package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
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
    private String mItemName;

    private ProgressDialog mProgressDialog;
    private Map<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_rent_item);

        setupToolbar();

        Intent intent;
        intent = getIntent();
        int mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        String mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mQuantity = intent.getIntExtra(Constants.QUANTITY, 1);
        String mPicture = intent.getStringExtra(Constants.PICTURE);
        String mFormatPrice = intent.getStringExtra(Constants.FORMAT_PRICE);

        TextView mTxtItem = (TextView) findViewById(R.id.txtItem);
        TextView mTxtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        Button mBtnRentItem = (Button) findViewById(R.id.btnRentItem);
        ImageView mImgItem = (ImageView) findViewById(R.id.imgItem);

        mProgressDialog = new ProgressDialog(this);

        mTxtItem.setText(mItemName);
        mTxtDescription.setText(mDescription);
        mTxtPrice.setText("Php " + mFormatPrice);

        Picasso.with(this)
                .load(mPicture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(mImgItem);

        setTitle(mItemName);

        data = new HashMap<>();

        String user = getUserName();
        data.put(Constants.RENTER, user);
        data.put(Constants.ID, "" + mItemId);

        mBtnRentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRent(v);
            }
        });
    }

    public void onRent(View view) {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");
        rentItem();
    }

    public void rentItem() {
        EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        int quantity = Integer.parseInt(txtQuantity.getText().toString());

        if(quantity <= mQuantity && quantity > 0) {
            data.put(Constants.QUANTITY, quantity + "");
            Server.rentItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getInt("status") == 201) {
                            Toast.makeText(RentItemActivity.this, mItemName + " is now reserved.", Toast.LENGTH_SHORT).show();
                            finish();
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
