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

    private Intent intent;

    private Spinner mSpinnerStarsToUse;
    private RadioButton mRdWithDiscount;
    private Map<String, String> data;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_buy_item);

        setupToolbar();

        mSharedPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);

        intent = getIntent();
        int itemId = intent.getIntExtra(Constants.ID, 0);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.FORMAT_PRICE);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        mSpinnerStarsToUse = (Spinner) findViewById(R.id.spinnerStarsToUse);
        mRdWithDiscount = (RadioButton) findViewById(R.id.rdWithDiscount);

        Button btnBuyItem = (Button) findViewById(R.id.btnBuyItem);
        SimpleDraweeView imgItem = (SimpleDraweeView) findViewById(R.id.imgItem);

        String itemName = intent.getStringExtra(Constants.ITEM_NAME);
        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText(getResources().getString(R.string.peso) + formatPrice);

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgItem);

        setTitle(itemName);
        data = new HashMap<>();
        data.put(Constants.BUYER, getUserName());
        data.put(Constants.ID, "" + itemId);

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

        if (mRdWithDiscount.isChecked()) {
            buyWithDiscount(progressDialog);
        } else {
            buyItem(progressDialog);
        }
    }

    public void buyItem(ProgressDialog progressDialog) {
        EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        int quantity = Integer.parseInt(txtQuantity.getText().toString());
        int itemQuantity = intent.getIntExtra(Constants.QUANTITY, 1);

        if(quantity <= itemQuantity && quantity > 0) {
            data.put(Constants.QUANTITY, quantity + "");
            Server.buyItem(data, progressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getInt("status") == 201) {
                            Toast.makeText(BuyItemActivity.this, intent.getStringExtra(Constants.ITEM_NAME) + " is now reserved.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BuyItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(BuyItemActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
        }
    }

    public void buyWithDiscount(final ProgressDialog progressDialog) {
        final AlertDialog.Builder buyItem = new AlertDialog.Builder(this);
        buyItem.setTitle("Buy With Discount");
        buyItem.setIcon(R.drawable.ic_star_black_24dp);

        float price = intent.getFloatExtra(Constants.PRICE, 0);
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
            starsToUse = Integer.parseInt(mSpinnerStarsToUse.getSelectedItem().toString());
            if (starsToUse < 50) {
                buyItem.setMessage("Stars to use should be greater than or equal to 50.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
            } else if (starsToUse > 150) {
                buyItem.setMessage("Stars to use should not be greater than 150.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
            } else {
                ReservedItem ri = new ReservedItem();
                ri.setStars_to_use(starsToUse);
                calculateDiscount();
                calculateDiscountedPrice(price);
                buyItem.setMessage("Discount:\t" + Utils.formatDouble(calculateDiscount() * 100) + "%\n" +
                        "Original Price:\t" + Utils.formatFloat(price) + "\n" +
                        "Discounted Price: \t" + Utils.formatDouble(calculateDiscountedPrice(price)) + "\n" +
                        "Stars Remaining: " + getStarsRemaining())
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.put(Constants.STARS_TO_USE, "" + getStarsToUse());
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
