package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mReservedDate;

    private TextView mTxtItem;
    private TextView mTxtDescription;
    private TextView mTxtPrice;
    private TextView mTxtReservedDate;
    private ImageView mImgPreview;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mReservationId = intent.getIntExtra(Constants.RESERVATION_ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);
        mPicture = intent.getStringExtra(Constants.PICTURE);
        mReservedDate = intent.getStringExtra(Constants.RESERVED_DATE);

        mTxtItem = (TextView) findViewById(R.id.txtItem);
        mTxtDescription = (TextView) findViewById(R.id.txtDescription);
        mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        mTxtReservedDate = (TextView) findViewById(R.id.txtReservedDate);
        mImgPreview = (ImageView) findViewById(R.id.preview);

        mProgressDialog = new ProgressDialog(this);

        mTxtItem.setText(mItemName);
        mTxtDescription.setText(mDescription);
        if(mPrice != 0) {
            mTxtPrice.setText("Php " + mPrice);
        } /*else if (mDiscountedPrice != 0) {
            mTxtPrice.setText("Php " + mDiscountedPrice + " (discounted)");
        }*/ else {
            mTxtPrice.setText("(Donated)");
        }

        Picasso.with(this)
                .load(mPicture)
                .into(mImgPreview);

        mTxtReservedDate.setText(mReservedDate);

        setTitle(mItemName);
    }

    public void onCancelReservedItem(View v) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
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
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Cancel Item Reservation error " + statusCode + " " + responseBody + " " + statusText);
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_shopping_cart;
    }
}
