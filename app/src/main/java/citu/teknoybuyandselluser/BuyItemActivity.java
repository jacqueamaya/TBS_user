package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyItemActivity extends BaseActivity {

    private static final String TAG = "Buy Item";

    private int mItemId;
    private int mStarsToUse;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    private TextView mTxtItem;
    private TextView mTxtDescription;
    private TextView mTxtPrice;
    private TextView mLblStarsToUse;
    private EditText mTxtStarsToUse;
    private RadioButton mRdWithoutDiscount;
    private RadioButton mRdWithDiscount;

    private ProgressDialog mProgressDialog;
    private ImageView mBtnBuyItem;
    private ImageView mImgItem;

    private SharedPreferences mPreferences;
    private Map<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);
        setupUI();

        mPreferences = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);
        mPicture = intent.getStringExtra(Constants.PICTURE);

        mTxtItem = (TextView) findViewById(R.id.txtItem);
        mTxtDescription = (TextView) findViewById(R.id.txtDescription);
        mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        mLblStarsToUse = (TextView) findViewById(R.id.lblStarsToUse);
        mTxtStarsToUse = (EditText) findViewById(R.id.txtStarsToUse);
        mRdWithoutDiscount = (RadioButton) findViewById(R.id.rdWithoutDiscount);
        mRdWithDiscount = (RadioButton) findViewById(R.id.rdWithDiscount);
        mBtnBuyItem = (ImageView) findViewById(R.id.btnBuyItem);
        mImgItem = (ImageView) findViewById(R.id.imgItem);

        mProgressDialog = new ProgressDialog(this);

        mTxtItem.setText(mItemName);
        mTxtDescription.setText(mDescription);
        mTxtPrice.setText("Php " + mPrice);

        Picasso.with(this)
                .load(mPicture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(mImgItem);

        setTitle(mItemName);

        data = new HashMap<>();

        String user = mPreferences.getString("username", "");
        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + mItemId);

        mBtnBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuy(v);
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_buy_items;
    }

    public void onBuy(View view) {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");
        if (mRdWithDiscount.isChecked()) {
            if (mStarsToUse != 0) {
                buyWithDiscountDialogBox();
            } else {
                Toast.makeText(BuyItemActivity.this, "Number of stars to use is not defined.", Toast.LENGTH_SHORT).show();
            }
        } else if (mRdWithoutDiscount.isChecked()) {
            buyItem();
        }
    }

    public void buyItem() {
        Server.buyItem(data, mProgressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 201) {
                        Log.d(TAG, "Buy Item success");
                        Toast.makeText(BuyItemActivity.this, mItemName + " is now reserved.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BuyItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Server error");
            }
        });
    }

    public void buyWithDiscountDialogBox() {
        mStarsToUse = Integer.parseInt(mTxtStarsToUse.getText().toString());
        final AlertDialog.Builder buyItem = new AlertDialog.Builder(this);
        buyItem.setTitle("Buy With Discount");
        buyItem.setIcon(R.drawable.ic_star_black_24dp);
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
            buyItem.setMessage("Discount:\t" + calculateDiscount() + "%\n" +
                    "Original Price:\t" + mPrice + "\n" +
                    "Discounted Price: \t" + calculateDiscountedPrice() + "\n" +
                    "Stars Remaining: " + getStarsRemaining())
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mPreferences.edit().putInt("stars_collected", getStarsRemaining()).apply();
                            data.put(Constants.DISCOUNTED_PRICE, "" + calculateDiscountedPrice());
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
        AlertDialog alert = buyItem.create();
        alert.show();
    }

    private int getStars() {
        mPreferences = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        return mPreferences.getInt(Constants.STARS_COLLECTED, 0);
    }

    private int getStarsRemaining() {
        return getStars() - mStarsToUse;
    }

    private float calculateDiscount() {
        return mStarsToUse / 1000;
    }

    private float calculateDiscountedPrice() {
        return mPrice * (1 - calculateDiscount());
    }

    public void showInputStars(View view) {
        if (getStars() != 0) {
            mLblStarsToUse.setText("Stars to use");
            mLblStarsToUse.setVisibility(View.VISIBLE);
            mTxtStarsToUse.setVisibility(View.VISIBLE);
        } else {
            mLblStarsToUse.setText("No stars collected");
            mLblStarsToUse.setVisibility(View.VISIBLE);
        }
    }

    public void hideInputStars(View view) {
        if (mLblStarsToUse.getVisibility() == View.VISIBLE) {
            mLblStarsToUse.setVisibility(View.GONE);
            mTxtStarsToUse.setVisibility(View.GONE);
        }
    }
}
