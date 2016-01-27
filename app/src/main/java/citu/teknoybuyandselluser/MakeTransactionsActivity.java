package citu.teknoybuyandselluser;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import citu.teknoybuyandselluser.adapters.GridAdapter;

import citu.teknoybuyandselluser.adapters.ViewPagerAdapter;
import citu.teknoybuyandselluser.fragments.BuyFragment;
import citu.teknoybuyandselluser.fragments.ForRentFragment;

/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class MakeTransactionsActivity extends BaseActivity {

    private String searchQuery = "";

    private GridAdapter gridAdapterForBuy;
    private GridAdapter gridAdapterForRent;

    private BuyFragment buyFragment;
    private ForRentFragment forRentFragment;

    public void setGridAdapterForBuy(GridAdapter gridAdapter) {
        this.gridAdapterForBuy = gridAdapter;
    }

    public void setGridAdapterForRent(GridAdapter gridAdapterForRent) {
        this.gridAdapterForRent = gridAdapterForRent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_make_transactions);
        setupUI();

        buyFragment = new BuyFragment();
        forRentFragment = new ForRentFragment();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(buyFragment, "Buy");
        adapter.addFragment(forRentFragment, "For Rent");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_make_transactions;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_make_transactions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                if(buyFragment.getUserVisibleHint())
                    gridAdapterForBuy.getFilter().filter(searchQuery);
                else if(forRentFragment.getUserVisibleHint())
                    gridAdapterForRent.getFilter().filter(searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                if(buyFragment.getUserVisibleHint())
                    gridAdapterForBuy.getFilter().filter(searchQuery);
                else if(forRentFragment.getUserVisibleHint())
                    gridAdapterForRent.getFilter().filter(searchQuery);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //return id == R.id.action_search || super.onOptionsItemSelected(item);
        if(buyFragment.getUserVisibleHint()) {
            switch (id) {
                case R.id.nav_sort_by_date:
                    gridAdapterForBuy.sortItems(Constants.Sort.DATE);
                    break;
                case R.id.nav_sort_by_name:
                    gridAdapterForBuy.sortItems(Constants.Sort.NAME);
                    break;
                case R.id.nav_sort_by_price:
                    gridAdapterForBuy.sortItems(Constants.Sort.PRICE);
                    break;

            }
        } else if(forRentFragment.getUserVisibleHint()) {
            switch (id) {
                case R.id.nav_sort_by_date:
                    gridAdapterForRent.sortItems(Constants.Sort.DATE);
                    break;
                case R.id.nav_sort_by_name:
                    gridAdapterForRent.sortItems(Constants.Sort.NAME);
                    break;
                case R.id.nav_sort_by_price:
                    gridAdapterForRent.sortItems(Constants.Sort.PRICE);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(MakeTransactionsActivity.this, ExpirationCheckerService.class);
        service.putExtra(Constants.User.USERNAME, getUserName());
        startService(service);
    }
}