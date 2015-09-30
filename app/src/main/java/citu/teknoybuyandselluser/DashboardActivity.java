package citu.teknoybuyandselluser;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TextView txtUser = (TextView) findViewById(R.id.txtUserName);
        txtUser.setText(getUserPreferences());

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.sandwich);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

    }

    public void setActionBarTitle(String title){
        mToolbar.setTitle(title);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //menuItem.setChecked(true);
                        selectDrawerItem(menuItem);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        Class fragmentClass = null;
/*
        switch(menuItem.getItemId()) {
            case R.id.nav_notifications:
                fragmentClass = NotificationsFragment.class;
                break;
            case R.id.nav_sell_items:
                fragmentClass = SellItemsFragment.class;
                break;
            case R.id.nav_pending_items:
                fragmentClass = PendingItemsFragment.class;
                break;
            case R.id.nav_buy_items:
                fragmentClass = BuyItemsFragment.class;
                break;
            case R.id.nav_shopping_cart:
                fragmentClass = ShoppingCartFragment.class;
                break;
            case R.id.nav_donate_items:
                fragmentClass = DonateItemsFragment.class;
                break;
            case R.id.nav_stars_collected:
                fragmentClass = StarsCollectedFragment.class;
                break;
            default:
                fragmentClass = NotificationsFragment.class;

        }
*/
        if(menuItem.getItemId() != (R.id.nav_logout)) {
            try {
                //Toast.makeText(this, "" + fragmentClass.toString(), Toast.LENGTH_SHORT).show();
                fragment = (Fragment) fragmentClass.newInstance();
                //Toast.makeText(this, "" + fragment.getActivity().toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment).addToBackStack("tag");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent;
            intent = new Intent(DashboardActivity.this, LoginActivity.class);
            //prefs.edit().clear().commit();
            finish();
            startActivity(intent);
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getUserPreferences() {
        prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("first_name", "No FirstName") + " " + prefs.getString("last_name", "No LastName");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
