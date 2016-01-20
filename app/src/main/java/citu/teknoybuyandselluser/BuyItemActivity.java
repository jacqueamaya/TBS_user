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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ReservedItem;

public class BuyItemActivity extends BaseActivity {

    private static final String TAG = "Buy Item";
    private static final int DIVISOR = 1000;

    private int mQuantity;
    private int mStarsToUse;
    private double mDiscount;
    private double mDiscountedPrice;
    private float mPrice;
    private String mItemName;

    private Spinner mSpinnerStarsToUse;
    private RadioButton mRdWithoutDiscount;
    private RadioButton mRdWithDiscount;
    private EditText mTxtQuantity;

    private ProgressDialog mProgressDialog;

    private SharedPreferences mPreferences;
    private Map<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_buy_item);
        setupUI();

        mPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);

        Intent intent;
        intent = getIntent();
        int itemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);
        mQuantity = intent.getIntExtra(Constants.QUANTITY, 1);
        String picture = intent.getStringExtra(Constants.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.FORMAT_PRICE);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        mSpinnerStarsToUse = (Spinner) findViewById(R.id.spinnerStarsToUse);
        mRdWithoutDiscount = (RadioButton) findViewById(R.id.rdWithoutDiscount);
        mRdWithDiscount = (RadioButton) findViewById(R.id.rdWithDiscount);
        mTxtQuantity = (EditText) findViewById(R.id.txtQuantity);
        ImageView btnBuyItem = (ImageView) findViewById(R.id.btnBuyItem);
        ImageView imgItem = (ImageView) findViewById(R.id.imgItem);

        mProgressDialog = new ProgressDialog(this);

        txtItem.setText(mItemName);
        txtDescription.setText(description);
        txtPrice.setText(getResources().getString(R.string.peso) + formatPrice);

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgItem);

        setTitle(mItemName);

        data = new HashMap<>();

        String user = mPreferences.getString(Constants.USERNAME, "");
        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + itemId);

        btnBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuy(v);
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_make_transactions;
    }

    public void onBuy(View view) {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");
        if (mRdWithDiscount.isChecked()) {
            buyWithDiscount();
        } else if (mRdWithoutDiscount.isChecked()) {
            buyItem();
        }
    }

    public void buyItem() {
        int quantity = Integer.parseInt(mTxtQuantity.getText().toString());
        if(quantity <= mQuantity && quantity > 0) {
            data.put(Constants.QUANTITY, quantity + "");
            Server.buyItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getInt("status") == 201) {
                            Log.d(TAG, "Buy Item success");
                            Toast.makeText(BuyItemActivity.this, mItemName + " is now reserved.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(BuyItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Log.d(TAG, "Server error: "+ responseBody);
                    Toast.makeText(BuyItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(BuyItemActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
        }
    }

    public void buyWithDiscount() {
        final AlertDialog.Builder buyItem = new AlertDialog.Builder(this);
        buyItem.setTitle("Buy With Discount");
        buyItem.setIcon(R.drawable.ic_star_black_24dp);

        int starsToUse = mSpinnerStarsToUse.getSelectedItemPosition();
        switch (starsToUse) {
            case 0: mStarsToUse = 50;
                break;
            case 1: mStarsToUse = 100;
                break;
            case 2: mStarsToUse = 150;
                break;
            default: mStarsToUse = 50;
        }
        if (getStars() < mStarsToUse) {
            buyItem.setMessage("Not enough stars collected. You only have " + getStars() + " stars collected.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        } else {
            mStarsToUse = Integer.parseInt(mSpinnerStarsToUse.getSelectedItem().toString());
            if (mStarsToUse < 50) {
                buyItem.setMessage("Stars to use should be greater than or equal to 50.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
            } else if (mStarsToUse > 150) {
                buyItem.setMessage("Stars to use should not be greater than 150.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
            } else {
                ReservedItem ri = new ReservedItem();
                ri.setStars_to_use(mStarsToUse);
                calculateDiscount();
                calculateDiscountedPrice();
                buyItem.setMessage("Discount:\t" + Utils.formatDouble(mDiscount * 100) + "%\n" +
                        "Original Price:\t" + Utils.formatFloat(mPrice) + "\n" +
                        "Discounted Price: \t" + Utils.formatDouble(mDiscountedPrice) + "\n" +
                        "Stars Remaining: " + getStarsRemaining())
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.put(Constants.STARS_TO_USE, "" + mStarsToUse);
                                buyItem();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        }

        AlertDialog alert = buyItem.create();
        alert.show();
    }

    private int getStars() {
        mPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        return mPreferences.getInt(Constants.STARS_COLLECTED, 0);
    }

    private int getStarsRemaining() {
        return getStars() - mStarsToUse;
    }

    private void calculateDiscount() {
        mDiscount = (double) mStarsToUse / DIVISOR;
    }

    private void calculateDiscountedPrice() {
        mDiscountedPrice = mPrice * (1 - mDiscount);
    }

    public void showInputStars(View view) {
        mSpinnerStarsToUse.setVisibility(View.VISIBLE);
    }

    public void hideInputStars(View view) {
        if (mSpinnerStarsToUse.getVisibility() == View.VISIBLE)
            mSpinnerStarsToUse.setVisibility(View.GONE);
    }
}
