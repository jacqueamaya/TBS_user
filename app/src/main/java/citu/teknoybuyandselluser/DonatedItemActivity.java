package citu.teknoybuyandselluser;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DonatedItemActivity extends BaseActivity {

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtNumStars;

    private String mItemName;
    private String mDescription;
    private String mPicture;
    private int mStarsRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mStarsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDetails);
        txtNumStars = (TextView) findViewById(R.id.txtNumStars);

        txtTitle.setText(mItemName);
        txtDescription.setText(mDescription);
        txtNumStars.setText("" + mStarsRequired);

        setTitle(mItemName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donated_item, menu);
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
        return menuItem.getItemId() != R.id.nav_stars_collected;
    }
}
