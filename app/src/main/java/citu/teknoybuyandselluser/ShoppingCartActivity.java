package citu.teknoybuyandselluser;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.fragments.ReservedItemFragment;
import citu.teknoybuyandselluser.listAdapters.ReservedItemsAdapter;
import citu.teknoybuyandselluser.models.ReservedItem;

public class ShoppingCartActivity extends BaseActivity {

    private static final String TAG = "ShoppingCart";

    private int mItemId;
    private int mReservationId;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mReservedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setupUI();

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        Server.getAllReservations(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, responseBody);
                ArrayList<ReservedItem> reservations = new ArrayList<ReservedItem>();
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    Log.d(TAG, jsonArray.toString());
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                        txtMessage.setText("You have not reserved an item");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        reservations = ReservedItem.allReservedItems(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewReservations);
                        ReservedItemsAdapter listAdapter = new ReservedItemsAdapter(ShoppingCartActivity.this, R.layout.activity_item, reservations);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem item = (ReservedItem) parent.getItemAtPosition(position);
                                mItemId = item.getItemId();
                                mReservationId = item.getReservationId();
                                mItemName = item.getItemName();
                                mDescription = item.getDescription();
                                mPrice = item.getPrice();
                                mReservedDate = item.getReserved_date();

                                Intent intent;
                                intent  = new Intent(ShoppingCartActivity.this, ReservedItemActivity.class);
                                intent.putExtra(Constants.ID, mItemId);
                                intent.putExtra(Constants.RESERVATION_ID, mReservationId);
                                intent.putExtra(Constants.ITEM_NAME, mItemName);
                                intent.putExtra(Constants.DESCRIPTION, mDescription);
                                intent.putExtra(Constants.PRICE, mPrice);
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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_shopping_cart;
    }
}
