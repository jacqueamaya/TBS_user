package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ReservedItemActivity extends BaseActivity {

    private static final String TAG = "ShoppingCart";

    private int mItemId;
    private int mReservationId;
    private int mStarsRequired;
    private int mStarsToUse;
    private String mItemName;

    private ProgressDialog mProgressDialog;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_item);
        setupUI();

        mPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mReservationId = intent.getIntExtra(Constants.RESERVATION_ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        float price = intent.getFloatExtra(Constants.PRICE, 0);
        mStarsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);
        mStarsToUse = intent.getIntExtra(Constants.STARS_TO_USE, 0);
        float discountedPrice = intent.getFloatExtra(Constants.DISCOUNTED_PRICE, 0);
        String picture = intent.getStringExtra(Constants.PICTURE);
        String reservedDate = intent.getStringExtra(Constants.RESERVED_DATE);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        TextView txtReservedDate = (TextView) findViewById(R.id.txtReservedDate);
        ImageView imgPreview = (ImageView) findViewById(R.id.preview);

        mProgressDialog = new ProgressDialog(this);

        txtItem.setText(mItemName);
        txtDescription.setText(description);

        if (price != 0) {
            if(mStarsToUse != 0) {
                txtPrice.setText("Php " + discountedPrice + " (" + mStarsToUse + " stars used)");
            } else {
                txtPrice.setText("Php " + price);
            }
        }
        else {
            txtPrice.setText("(Donated)");
        }

        Picasso.with(this)
                .load(picture)
                .into(imgPreview);

        txtReservedDate.setText(reservedDate);

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

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_shopping_cart;
    }

    public void cancelBuyItem () {
        Map<String, String> data = new HashMap<>();
        String user = mPreferences.getString("username", "");
        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + mItemId);
        data.put(Constants.RESERVATION_ID, "" + mReservationId);

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        Server.cancelBuyItem(data, mProgressDialog, new Ajax.Callbacks() {
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
}
