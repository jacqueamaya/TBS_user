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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ReservedItemActivity extends AppCompatActivity {

    private static final String TAG = "ShoppingCart";

    private Intent intent;
    private String mItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_reserved_item);

        setupToolbar();

        intent = getIntent();
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        float price = intent.getFloatExtra(Constants.PRICE, 0);
        int mStarsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);
        int mStarsToUse = intent.getIntExtra(Constants.STARS_TO_USE, 0);
        float discountedPrice = intent.getFloatExtra(Constants.DISCOUNTED_PRICE, 0);
        String picture = intent.getStringExtra(Constants.PICTURE);
        long reservedDate = intent.getLongExtra(Constants.RESERVED_DATE, 0);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        TextView txtReservedDate = (TextView) findViewById(R.id.txtReservedDate);
        ImageView imgPreview = (ImageView) findViewById(R.id.preview);

        txtItem.setText(mItemName);
        txtDescription.setText(description);

        if (price != 0) {
            if(mStarsToUse != 0) {
                txtPrice.setText("Php " + Utils.formatFloat(discountedPrice) + " (" + mStarsToUse + " stars used)");
            } else {
                txtPrice.setText("Php " + Utils.formatFloat(price));
            }
        }
        else {
            txtPrice.setText(mStarsRequired + " stars required (Donated)");
        }

        Picasso.with(this)
                .load(picture)
                .into(imgPreview);

        txtReservedDate.setText(Utils.parseDate(reservedDate));

        setTitle(mItemName);
    }

    public void onCancelReservedItem(View v) {
        final AlertDialog.Builder cancelBuyItem = new AlertDialog.Builder(this);
        cancelBuyItem.setTitle("Cancel Reservation");
        cancelBuyItem.setIcon(R.drawable.ic_delete_black_24dp);
        cancelBuyItem.setMessage("Are you sure you want to cancel your reservation for this item?");
        cancelBuyItem.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelBuyItem();
            }
        });
        cancelBuyItem.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = cancelBuyItem.create();
        alert.show();
    }

    public void cancelBuyItem () {
        int itemId = intent.getIntExtra(Constants.ID, 0);
        int reservationId = intent.getIntExtra(Constants.RESERVATION_ID, 0);

        Map<String, String> data = new HashMap<>();
        String user = getUserName();
        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + itemId);
        data.put(Constants.RESERVATION_ID, "" + reservationId);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait. . .");

        Server.cancelBuyItem(data, progressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Cancel Item Reservation success");
                Toast.makeText(ReservedItemActivity.this, "Your reservation for " + mItemName + " has been canceled.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(ReservedItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
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
