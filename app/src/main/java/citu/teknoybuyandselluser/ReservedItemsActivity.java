package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import citu.teknoybuyandselluser.adapters.ReservationAdapter;
/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class ReservedItemsActivity extends BaseActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_reserved_items);
        setupUI();

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ReservationAdapter reservationAdapter = new ReservationAdapter(getSupportFragmentManager());
        viewPager.setAdapter(reservationAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(ReservedItemsActivity.this, ExpirationCheckerService.class);
        service.putExtra("username", prefs.getString(Constants.USERNAME,""));
        startService(service);
    }
}