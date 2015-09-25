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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class BuyItemActivity extends BaseActivity {

    private static final String TAG = "Buy Item";

    private int mItemId;
    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    private TextView txtItem;
    private TextView txtDescription;
    private TextView txtPrice;

    private ImageView btnBuyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);

        txtItem = (TextView) findViewById(R.id.txtItem);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        btnBuyItem = (ImageView) findViewById(R.id.btnBuyItem);

        txtItem.setText(mItemName);
        txtDescription.setText(mDescription);
        txtPrice.setText("Php " + mPrice);

        setTitle(mItemName);

        btnBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuy(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy_item, menu);
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

    public void onBuy(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + mItemId);

        Server.buyItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Buy Item success");
                Toast.makeText(BuyItemActivity.this, mItemName + " has been reserved.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Buy Item error " + statusCode + " " + responseBody + " " + statusText);
            }
        });
    }
}
