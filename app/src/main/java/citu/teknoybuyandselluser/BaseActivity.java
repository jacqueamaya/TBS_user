package citu.teknoybuyandselluser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Jacquelyn on 9/24/2015.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private SharedPreferences prefs;

    protected void setupUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if(null == toolbar) {
            throw new RuntimeException("No toolbar found");
        }

        if(null == mDrawerLayout) {
            throw new RuntimeException("No drawer layout found");
        }

        if(null == mNavigationView) {
            throw new RuntimeException("No navigation view found");
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.syncState();

        TextView txtUser = (TextView) findViewById(R.id.txtUserName);
        txtUser.setText(getUserPreferences());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent = null;

        if(checkItemClicked(menuItem)) {
            switch(menuItem.getItemId()) {
                case R.id.nav_notifications:
                    intent = new Intent(this, NotificationsActivity.class);
                    break;
                case R.id.nav_sell_items:
                    intent = new Intent(this, SellItemsActivity.class);
                    break;
                case R.id.nav_pending_items:
                    intent = new Intent(this, PendingItemsActivity.class);
                    break;
                case R.id.nav_buy_items:
                    intent = new Intent(this, BuyItemsActivity.class);
                    break;
                case R.id.nav_shopping_cart:
                    intent = new Intent(this, ShoppingCartActivity.class);
                    break;
                case R.id.nav_donate_items:
                    intent = new Intent(this, DonateItemsActivity.class);
                    break;
                case R.id.nav_stars_collected:
                    intent = new Intent(this, StarsCollectedActivity.class);
                    break;
                case R.id.nav_logout:
                    intent = new Intent(this, LoginActivity.class);
                    break;
                default:
                    intent = new Intent(this, NotificationsActivity.class);

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        mDrawerLayout.closeDrawers();

        if(intent != null) {
            startActivity(intent);
            this.finish();
            overridePendingTransition(0, 0);
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public String getUserPreferences() {
        prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("first_name", "No FirstName") + " " + prefs.getString("last_name", "No LastName");
    }

    public abstract boolean checkItemClicked(MenuItem menuItem);
}
