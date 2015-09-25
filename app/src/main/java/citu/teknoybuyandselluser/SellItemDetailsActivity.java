package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class SellItemDetailsActivity extends BaseActivity {

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;

    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item_details);
        setupUI();

        Intent intent = getIntent();
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);

        txtItem = (EditText) findViewById(R.id.txtItem);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtPrice = (EditText) findViewById(R.id.txtPrice);

        txtItem.setText(mItemName);
        txtDescription.setText(mDescription);
        txtPrice.setText("" + mPrice);

        setTitle(mItemName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell_item_details, menu);
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
}
