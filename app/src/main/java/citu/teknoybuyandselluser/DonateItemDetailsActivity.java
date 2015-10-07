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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donate_item_details, menu);
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
        return menuItem.getItemId() != R.id.nav_donate_items;
    }
}
