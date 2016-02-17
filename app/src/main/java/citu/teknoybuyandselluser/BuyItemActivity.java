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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ReservedItem;

public class BuyItemActivity extends AppCompatActivity {

    private static final int DIVISOR = 1000;

    private int mItemQuantity;
    private Intent intent;

    private Map<String, String> data;
    private RadioButton mRdWithDiscount;
    private Spinner mSpinnerStarsToUse;

    private SharedPreferences mSharedPreferences;

    private String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_buy_item);

        setupToolbar();

        mSharedPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);

        intent = getIntent();
        int itemId = intent.getIntExtra(Constants.Item.ID, 0);
        mItemQuantity = intent.getIntExtra(Constants.Item.QUANTITY, 1);
        String strAvailableQuantity = mItemQuantity + "";
        String description = intent.getStringExtra(Constants.Item.DESCRIPTION);
        String formatPrice = "Php " + intent.getStringExtra(Constants.Item.FORMAT_PRICE);
        String picture = intent.getStringExtra(Constants.Item.PICTURE);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtAvailableQuantity = (TextView) findViewById(R.id.txtAvailableQuantity);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        mSpinnerStarsToUse = (Spinner) findViewById(R.id.spinnerStarsToUse);
        mRdWithDiscount = (RadioButton) findViewById(R.id.rdWithDiscount);

        Button btnBuyItem = (Button) findViewById(R.id.btnBuyItem);
        SimpleDraweeView imgItem = (SimpleDraweeView) findViewById(R.id.imgItem);

        itemName = intent.getStringExtra(Constants.Item.ITEM_NAME);
        txtItem.setText(itemName);
        txtAvailableQuantity.setText(strAvailableQuantity);
        txtDescription.setText(description);
        txtPrice.setText(formatPrice);

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgItem);

        setTitle(itemName);
        data = new HashMap<>();
        data.put(Constants.Item.BUYER, getUserName());
        data.put(Constants.Item.ID, "" + itemId);

        btnBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuy(v);
            }
        });
    }

    public void onBuy(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait. . .");

        EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity);

        int quantity = 0;
        if(!"".equals(txtQuantity.getText().toString()))
            quantity = Integer.parseInt(txtQuantity.getText().toString());

        if(quantity <= mItemQuantity && quantity > 0) {
            data.put(Constants.Item.QUANTITY, quantity + "");
            if (mRdWithDiscount.isChecked())
                buyWithDiscount(progressDialog);
            else {
                data.put(Constants.Item.STARS_TO_USE, "");
                buyItem(progressDialog);
            }
        } else
            Toast.makeText(BuyItemActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
    }

    public void buyItem(ProgressDialog progressDialog) {
        Server.buyItem(data, progressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 201) {
                        //Toast.makeText(BuyItemActivity.this, intent.getStringExtra(Constants.Item.ITEM_NAME) + " is now reserved.", Toast.LENGTH_SHORT).show();
                        finish();
                        showAlertDialog();
                    } else {
                        Toast.makeText(BuyItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(BuyItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void buyWithDiscount(final ProgressDialog progressDialog) {
        final AlertDialog.Builder buyItem = new AlertDialog.Builder(this);
        buyItem.setTitle("Buy With Discount");
        buyItem.setIcon(R.drawable.ic_star_black_24dp);

        float price = intent.getFloatExtra(Constants.Item.PRICE, 0);
        int starsCollected = getUserStarsCollected();

        int starsToUse = getStarsToUse();

        if (starsCollected < starsToUse) {
            buyItem.setMessage("Not enough stars collected. You only have " + starsCollected + " stars collected.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        } else {
            ReservedItem reservedItem = new ReservedItem();
            reservedItem.setStars_to_use(starsToUse);
            buyItem.setMessage("Discount:\t" + Utils.formatDouble(calculateDiscount() * 100) + "%\n" +
                    "Original Price:\t" + Utils.formatFloat(price) + "\n" +
                    "Discounted Price: \t" + Utils.formatDouble(calculateDiscountedPrice(price)) + "\n" +
                    "Stars Remaining: " + getStarsRemaining())
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            data.put(Constants.Item.STARS_TO_USE, "" + getStarsToUse());
                            buyItem(progressDialog);
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

    private int getStarsToUse() {
        int starsToUse = mSpinnerStarsToUse.getSelectedItemPosition();
        switch (starsToUse) {
            case 0: starsToUse = 50;
                break;
            case 1: starsToUse = 100;
                break;
            case 2: starsToUse = 150;
                break;
            default: starsToUse = 50;
        }
        return starsToUse;
    }

    private int getStarsRemaining() {
        return getUserStarsCollected() - getStarsToUse();
    }

    public String getUserName() {
        return mSharedPreferences.getString(Constants.User.USERNAME, "");
    }

    private int getUserStarsCollected() {
        return mSharedPreferences.getInt(Constants.User.STARS_COLLECTED, 0);
    }

    private double calculateDiscount() {
        return ((double) getStarsToUse() / DIVISOR);
    }

    private double calculateDiscountedPrice(float price) {
        return (price * (1 - calculateDiscount()));
    }

    public void showInputStars(View view) {
        mSpinnerStarsToUse.setVisibility(View.VISIBLE);
    }

    public void hideInputStars(View view) {
        if (mSpinnerStarsToUse.getVisibility() == View.VISIBLE)
            mSpinnerStarsToUse.setVisibility(View.GONE);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void showAlertDialog() {
        AlertDialog.Builder buyItem = new AlertDialog.Builder(this);
        buyItem.setTitle("Buy Item Reminder");
        buyItem.setMessage(Utils.capitalize(itemName) + ", is now reserved. " +
                "Please wait within three(3) days for the seller to give the item to TBS Admin. " +
                "Otherwise, your reservation will expire and will be deleted from Reserved Items on Sale list.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        buyItem.create().show();
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
