package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;

import citu.teknoybuyandselluser.adapters.ViewPagerAdapter;
import citu.teknoybuyandselluser.fragments.ItemsForDonationFragment;
import citu.teknoybuyandselluser.fragments.ItemsForRentFragment;
import citu.teknoybuyandselluser.fragments.ItemsOnSaleFragment;
import citu.teknoybuyandselluser.services.ExpirationCheckerService;

/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class ReservedItemsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_reserved_items);
        setupUI();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemsOnSaleFragment(), "Items On Sale");
        adapter.addFragment(new ItemsForRentFragment(), "Items For Rent");
        adapter.addFragment(new ItemsForDonationFragment(), "Items For Donation");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        Snackbar snack = Snackbar.make(findViewById(R.id.viewpager), Constants.NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG);

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            snack.dismiss();
            Intent service = new Intent(ReservedItemsActivity.this, ExpirationCheckerService.class);
            service.putExtra(Constants.User.USERNAME, getUserName());
            startService(service);
        } else {
            snack.show();
        }
    }
}