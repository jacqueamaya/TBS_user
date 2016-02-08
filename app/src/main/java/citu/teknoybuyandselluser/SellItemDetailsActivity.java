package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

public class SellItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_sell_item_details);

        setupToolbar();

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(Constants.Item.ITEM_NAME);
        String description = intent.getStringExtra(Constants.Item.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.Item.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.Item.FORMAT_PRICE);
        int quantity = intent.getIntExtra(Constants.Item.QUANTITY, 1);

        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtPrice = (TextView) findViewById(R.id.txtPrice);
        TextView txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        SimpleDraweeView imgPreview = (SimpleDraweeView) findViewById(R.id.preview);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("" + formatPrice);
        txtQuantity.setText("" + quantity);

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
