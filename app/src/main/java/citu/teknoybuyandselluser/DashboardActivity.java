package citu.teknoybuyandselluser;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import citu.teknoybuyandselluser.fragments.BuyItemsFragment;
import citu.teknoybuyandselluser.fragments.DonateItemsFragment;
import citu.teknoybuyandselluser.fragments.NotificationsFragment;
import citu.teknoybuyandselluser.fragments.PendingItemsFragment;
import citu.teknoybuyandselluser.fragments.SellItemsFragment;
import citu.teknoybuyandselluser.fragments.ShoppingCartFragment;
import citu.teknoybuyandselluser.fragments.StarsCollectedFragment;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        String user = intent.getStringExtra("user");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TextView txtUser = (TextView) findViewById(R.id.txtName);
        txtUser.setText(user);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.sandwich);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
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

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;

        Class fragmentClass = null;
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
        if(menuItem.getItemId() != (R.id.nav_logout)) {
            try {
                //Toast.makeText(this, "" + fragmentClass.toString(), Toast.LENGTH_SHORT).show();
                fragment = (Fragment) fragmentClass.newInstance();
                //Toast.makeText(this, "" + fragment.getActivity().toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();*/

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent;
            intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
