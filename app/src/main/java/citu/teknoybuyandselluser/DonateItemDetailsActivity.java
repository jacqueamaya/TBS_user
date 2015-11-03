package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DonateItemDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_item_details);
        setupUI();

        Intent intent;
        intent = getIntent();
        String itemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.PICTURE);
        int starsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) findViewById(R.id.txtDetails);
        TextView txtNumStars = (TextView) findViewById(R.id.txtNumStars);
        ImageView imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        txtTitle.setText(itemName);
        txtDescription.setText(description);
        txtNumStars.setText("" + starsRequired);

        Picasso.with(this)
                .load(picture)
                .into(imgThumbnail);

        setTitle(itemName);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_donate_items;
    }
}
