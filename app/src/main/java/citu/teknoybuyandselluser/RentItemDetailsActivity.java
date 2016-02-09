package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

/**
 ** 0.01 View Details for Rent Item      - J. Amaya      - 01/06/2016
 */

public class RentItemDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RentItemDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_rent_item_details);

        setupToolbar();

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(Constants.Item.ITEM_NAME);
        String description = intent.getStringExtra(Constants.Item.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.Item.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.Item.FORMAT_PRICE);
        int quantity = intent.getIntExtra(Constants.Item.QUANTITY, 1);
        int rentDuration = intent.getIntExtra(Constants.Item.RENT_DURATION, 1);
        String status = intent.getStringExtra(Constants.Item.STATUS);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        TextView txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        //TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
        TextView txtRentDuration = (TextView) findViewById(R.id.txtRentDuration);
        ImageView imgPreview = (ImageView) findViewById(R.id.preview);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("Php" + formatPrice);

        if (quantity == 1)
            txtQuantity.setText("" + quantity + "pc.");
        else
            txtQuantity.setText("" + quantity + "pcs.");

        if (rentDuration == 1)
            txtRentDuration.setText("" + rentDuration + "day");
        else
            txtRentDuration.setText("" + rentDuration + "days");
        //txtStatus.setText(status);

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgPreview);

        setTitle(itemName);

        Log.e(TAG,"Rent item details");
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
