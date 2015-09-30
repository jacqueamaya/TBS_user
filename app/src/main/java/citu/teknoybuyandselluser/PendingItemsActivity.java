package citu.teknoybuyandselluser;

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

import citu.teknoybuyandselluser.listAdapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;

public class PendingItemsActivity extends BaseActivity {

    private static final String TAG = "Pending Items";

    private int mItemId;
    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_items);
        setupUI();

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        Server.getPendingItems(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> pendingItems = new ArrayList<Item>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                        txtMessage.setText("You have no pending items");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        pendingItems = Item.allItems(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewPending);
                        ItemsListAdapter listAdapter = new ItemsListAdapter(PendingItemsActivity.this, R.layout.activity_item, pendingItems);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = (Item) parent.getItemAtPosition(position);
                                mItemId = item.getId();
                                mItemName = item.getItemName();
                                mDescription = item.getDescription();
                                mPrice = item.getPrice();
                                mPicture = item.getPicture();
                                mStarsRequired = item.getStars_required();

                                Intent intent;
                                intent = new Intent(PendingItemsActivity.this, PendingItemActivity.class);
                                intent.putExtra(Constants.ID, mItemId);
                                intent.putExtra(Constants.ITEM_NAME, mItemName);
                                intent.putExtra(Constants.DESCRIPTION, mDescription);
                                intent.putExtra(Constants.PRICE, mPrice);
                                intent.putExtra(Constants.PICTURE, mPicture);
                                intent.putExtra(Constants.STARS_REQUIRED, mStarsRequired);

                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
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
        getMenuInflater().inflate(R.menu.menu_pending_items, menu);
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
        return menuItem.getItemId() != R.id.nav_pending_items;
    }
}
