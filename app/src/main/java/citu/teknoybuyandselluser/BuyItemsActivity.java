package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.adapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Category;
import citu.teknoybuyandselluser.models.Item;


public class BuyItemsActivity extends BaseActivity {

    private Spinner spinnerSortBy;
    private Toolbar toolbar;
    private TextView txtCategory;

    private static final String TAG = "BuyItems";

    private int mItemId;
    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String user;
    private String categories[];

    private ItemsListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_items);
        setupUI();

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString("username", "");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtCategory = (TextView) findViewById(R.id.txtCategory);
        getItems();

        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategories(v);
            }
        });
    }

    public void getCategories(View view) {
        Server.getCategories(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    categories = Category.getAllCategories(new JSONArray(responseBody));
                    new AlertDialog.Builder(BuyItemsActivity.this)
                            .setTitle("Categories")
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    txtCategory.setText(categories[which]);
                                }
                            })
                            .create()
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                categories = null;
                Toast.makeText(BuyItemsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buy_items, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(this);

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
        return menuItem.getItemId() != R.id.nav_buy_items;
    }

    public void getItems() {
        Log.d(TAG, txtCategory.getText().toString());
        if (txtCategory.getText().toString().equals("Categories")) {
            getAllItems();
        } else {
            listAdapter.getFilter().filter(txtCategory.getText().toString());
        }
    }

    public void getAllItems() {
        Server.getAvailableItems(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> availableItems = new ArrayList<Item>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                        txtMessage.setText("No available items to buy");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        availableItems = Item.allItems(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewBuyItems);
                        listAdapter = new ItemsListAdapter(BuyItemsActivity.this, R.layout.list_item, availableItems);
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
                                intent = new Intent(BuyItemsActivity.this, BuyItemActivity.class);
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

}
