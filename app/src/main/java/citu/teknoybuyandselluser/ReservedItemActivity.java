package citu.teknoybuyandselluser;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ReservedItemActivity extends BaseActivity {

    private static final String TAG = "ShoppingCart";

    private int mItemId;
    private int mReservationId;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mReservedDate;

    private TextView txtItem;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtReservedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mReservationId = intent.getIntExtra(Constants.RESERVATION_ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);
        mReservedDate = intent.getStringExtra(Constants.RESERVED_DATE);

        txtItem = (TextView) findViewById(R.id.txtItem);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtReservedDate = (TextView) findViewById(R.id.txtReservedDate);

        txtItem.setText(mItemName);
        txtDescription.setText(mDescription);
        txtPrice.setText("Php "+mPrice);
        txtReservedDate.setText(mReservedDate);

        setTitle(mItemName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reserved_item, menu);
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
