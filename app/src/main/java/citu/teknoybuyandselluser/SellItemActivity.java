package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SellItemActivity extends BaseActivity {

    private static final String TAG = "Sell Item";

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);
        setupUI();

        txtItem = (EditText) findViewById(R.id.inputItem);
        txtDescription = (EditText) findViewById(R.id.inputDescription);
        txtPrice = (EditText) findViewById(R.id.inputPrice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell_item, menu);
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
        return menuItem.getItemId() != R.id.nav_sell_items;
    }

    public void onSell(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(Constants.OWNER, user);
        data.put(Constants.NAME, txtItem.getText().toString());
        data.put(Constants.DESCRIPTION, txtDescription.getText().toString());
        data.put(Constants.PRICE, txtPrice.getText().toString());

        Server.sellItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Sell Item success");
                Toast.makeText(SellItemActivity.this, "Sell Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Sell Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(SellItemActivity.this, "Sell Item ERROR: " + statusCode + " " + responseBody + " " + statusText, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
