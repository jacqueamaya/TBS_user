package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

public class SellItemDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_sell_item_details);
        setupUI();

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.FORMAT_PRICE);
        int quantity = intent.getIntExtra(Constants.QUANTITY, 1);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        TextView txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        ImageView imgPreview = (ImageView) findViewById(R.id.preview);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("" + formatPrice);
        txtQuantity.setText("" + quantity);

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
