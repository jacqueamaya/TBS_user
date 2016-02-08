package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

public class RentedItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_rented_item_detail);

        setupToolbar();

        Intent intent = getIntent();
        float penalty = intent.getFloatExtra(Constants.Item.PENALTY, 0);
        int quantity = intent.getIntExtra(Constants.Item.QUANTITY, 1);
        long rentDate = intent.getLongExtra(Constants.Item.RENT_DATE, 0);
        long rentExpiration = intent.getLongExtra(Constants.Item.RENT_EXPIRATION, 0);
        String itemName = intent.getStringExtra(Constants.Item.ITEM_NAME);
        String description = intent.getStringExtra(Constants.Item.DESCRIPTION);
        String formatPrice = intent.getStringExtra(Constants.Item.FORMAT_PRICE);
        String strPenalty = "Penalty: Php " + penalty;
        String picture = intent.getStringExtra(Constants.Item.PICTURE);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        TextView lblPenalty = (TextView) findViewById(R.id.lblPenalty);
        TextView txtPenalty = (TextView) findViewById(R.id.txtPenalty);
        TextView txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        TextView txtRentDate = (TextView) findViewById(R.id.txtRentDate);
        TextView txtRentExpiry = (TextView) findViewById(R.id.txtRentExpiry);
        ImageView imgPreview = (ImageView) findViewById(R.id.preview);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("Php" + formatPrice);
        if(penalty != 0) {
            //imgPenalty.setVisibility(View.VISIBLE);
            lblPenalty.setVisibility(View.VISIBLE);
            txtPenalty.setVisibility(View.VISIBLE);
            txtPenalty.setText(strPenalty);
        } else {
            //imgPenalty.setVisibility(View.GONE);
            lblPenalty.setVisibility(View.GONE);
            txtPenalty.setVisibility(View.GONE);
        }

        if (quantity == 1)
            txtQuantity.setText("" + quantity + "pc.");
        else
            txtQuantity.setText("" + quantity + "pcs.");

        txtRentDate.setText("Rent Date: " + Utils.parseDate(rentDate));
        txtRentExpiry.setText("Rent Expiration: " + Utils.parseDate(rentExpiration));

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgPreview);

        setTitle(itemName);
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
