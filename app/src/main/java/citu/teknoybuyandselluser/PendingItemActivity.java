package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class PendingItemActivity extends BaseActivity {

    private static final String TAG = "Pending Item";

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;

    private int mItemId;
    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);

        txtItem = (EditText) findViewById(R.id.txtItem);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtPrice = (EditText) findViewById(R.id.txtPrice);

        txtItem.setText(mItemName);
        txtDescription.setText(mDescription);
        if(mPrice == 0.0) {
            txtPrice.setText("(To Donate)");
            txtPrice.setEnabled(false);
        } else {
            txtPrice.setText("" + mPrice);
            txtPrice.setEnabled(true);
        }

        setTitle(mItemName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pending_item, menu);
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

    public void onEditItem(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(Constants.OWNER, user);
        data.put(Constants.ID, "" + mItemId);
        data.put(Constants.NAME, txtItem.getText().toString());
        data.put(Constants.DESCRIPTION, txtDescription.getText().toString());
        data.put(Constants.PRICE, txtPrice.getText().toString());

        Server.editItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Edit Item success");
                Toast.makeText(PendingItemActivity.this, "Edit Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Edit Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(PendingItemActivity.this, "Edit Item ERROR: " + statusCode + " " + responseBody + " " + statusText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeleteItem(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        Log.d(TAG, "user: " + user);
        data.put(Constants.OWNER, user);
        data.put(Constants.ID, "" + mItemId);

        Server.deleteItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Delete Item success");
                Toast.makeText(PendingItemActivity.this, "Delete Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Delete Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(PendingItemActivity.this, "Delete Item ERROR: " + mItemId + statusText, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
