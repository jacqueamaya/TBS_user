package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.adapters.ReservedItemsAdapter;
import citu.teknoybuyandselluser.models.ReservedItem;

public class ShoppingCartActivity extends BaseActivity {

    private static final String TAG = "ShoppingCart";

    private int mItemId;
    private int mReservationId;
    private int mStarsToUse;
    private float mDiscountedPrice;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mReservedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setupUI();

        getReservedItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReservedItems();
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_shopping_cart;
    }

    public void getReservedItems() {
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getAllReservations(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, responseBody);
                ArrayList<ReservedItem> reservations;
                JSONArray jsonArray;

                try {
                    ListView lv = (ListView) findViewById(R.id.listViewReservations);
                    TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                    jsonArray = new JSONArray(responseBody);
                    Log.d(TAG, jsonArray.toString());
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No reserved items");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        reservations = ReservedItem.allReservedItems(jsonArray);
                        ReservedItemsAdapter listAdapter = new ReservedItemsAdapter(ShoppingCartActivity.this, R.layout.list_item, reservations);
                        lv.setVisibility(View.VISIBLE);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem item = (ReservedItem) parent.getItemAtPosition(position);
                                mItemId = item.getItemId();
                                mReservationId = item.getReservationId();
                                mItemName = item.getItemName();
                                mDescription = item.getDescription();
                                mStarsToUse = item.getStarsToUse();
                                mDiscountedPrice = item.getDiscountedPrice();
                                mPrice = item.getPrice();
                                mPicture = item.getPicture();
                                mReservedDate = item.getReserved_date();

                                Intent intent;
                                intent  = new Intent(ShoppingCartActivity.this, ReservedItemActivity.class);
                                intent.putExtra(Constants.ID, mItemId);
                                intent.putExtra(Constants.RESERVATION_ID, mReservationId);
                                intent.putExtra(Constants.ITEM_NAME, mItemName);
                                intent.putExtra(Constants.DESCRIPTION, mDescription);
                                intent.putExtra(Constants.PRICE, mPrice);
                                intent.putExtra(Constants.STARS_REQUIRED, item.getStarsRequired());
                                intent.putExtra(Constants.STARS_TO_USE, mStarsToUse);
                                intent.putExtra(Constants.DISCOUNTED_PRICE, mDiscountedPrice);
                                intent.putExtra(Constants.PICTURE, mPicture);
                                intent.putExtra(Constants.RESERVED_DATE, mReservedDate);

                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(ShoppingCartActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
