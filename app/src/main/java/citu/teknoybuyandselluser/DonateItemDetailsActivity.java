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

public class DonateItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_donate_item_details);

        setupToolbar();

        Intent intent;
        intent = getIntent();
        String itemName = intent.getStringExtra(Constants.Item.ITEM_NAME);
        String description = intent.getStringExtra(Constants.Item.DESCRIPTION);
        int quantity = intent.getIntExtra(Constants.Item.QUANTITY, 1);
        String picture = intent.getStringExtra(Constants.Item.PICTURE);
        int starsRequired = intent.getIntExtra(Constants.Item.STARS_REQUIRED, 0);

        TextView txtTitle = (TextView) findViewById(R.id.txtItem);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtNumStars = (TextView) findViewById(R.id.txtNumStars);
        TextView txtQuantity = (TextView) findViewById(R.id.txtQuantity);
        SimpleDraweeView imgThumbnail = (SimpleDraweeView) findViewById(R.id.preview);

        txtTitle.setText(itemName);
        txtDescription.setText(description);
        txtNumStars.setText("" + starsRequired);

        if (quantity == 1)
            txtQuantity.setText("" + quantity + "pc.");
        else
            txtQuantity.setText("" + quantity + "pcs.");

        Picasso.with(this)
                .load(picture)
                .placeholder(R.drawable.thumbsq_24dp)
                .into(imgThumbnail);

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
