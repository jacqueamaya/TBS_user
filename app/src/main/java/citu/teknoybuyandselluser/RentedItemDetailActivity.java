package citu.teknoybuyandselluser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

public class RentedItemDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_rented_item_detail);
        setupUI();

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.FORMAT_PRICE);
        float penalty = intent.getFloatExtra(Constants.PENALTY, 0);
        int quantity = intent.getIntExtra(Constants.QUANTITY, 1);
        long rentDate = intent.getLongExtra(Constants.RENT_DATE, 0);
        long rentExpiration = intent.getLongExtra(Constants.RENT_EXPIRATION, 0);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        ImageView imgPenalty = (ImageView) findViewById(R.id.penalty);
        TextView txtPenalty = (TextView) findViewById(R.id.txtPenalty);
        TextView txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        TextView txtRentDate = (TextView) findViewById(R.id.txtRentDate);
        TextView txtRentExpiry = (TextView) findViewById(R.id.txtRentExpiry);
        ImageView imgPreview = (ImageView) findViewById(R.id.preview);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("" + formatPrice);
        if(penalty != 0) {
            imgPenalty.setVisibility(View.VISIBLE);
            txtPenalty.setVisibility(View.VISIBLE);
            txtPenalty.setText("Penalty: " + getResources().getString(R.string.peso) + " " + penalty);
        } else {
            imgPenalty.setVisibility(View.GONE);
            txtPenalty.setVisibility(View.GONE);
        }
        txtQuantity.setText("" + quantity);
        txtRentDate.setText("Rent Date: " + Utils.parseDate(rentDate));
        txtRentExpiry.setText("Rent Expiration: " + Utils.parseDate(rentExpiration));

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumb_24dp)
                .into(imgPreview);

        setTitle(itemName);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_my_items;
    }
}
