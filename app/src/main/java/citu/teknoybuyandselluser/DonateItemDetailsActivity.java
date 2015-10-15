package citu.teknoybuyandselluser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DonateItemDetailsActivity extends BaseActivity {

    private TextView mTxtTitle;
    private TextView mTxtDescription;
    private TextView mTxtNumStars;
    private ImageView mImgThumbnail;

    private int mStarsRequired;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_item_details);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPicture = intent.getStringExtra(Constants.PICTURE);
        mStarsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtDescription = (TextView) findViewById(R.id.txtDetails);
        mTxtNumStars = (TextView) findViewById(R.id.txtNumStars);
        mImgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        mTxtTitle.setText(mItemName);
        mTxtDescription.setText(mDescription);
        mTxtNumStars.setText("" + mStarsRequired);

        Picasso.with(this)
                .load(mPicture)
                .into(mImgThumbnail);

        setTitle(mItemName);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_donate_items;
    }
}
