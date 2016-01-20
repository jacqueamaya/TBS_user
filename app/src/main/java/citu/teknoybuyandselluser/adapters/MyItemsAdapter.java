package citu.teknoybuyandselluser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import citu.teknoybuyandselluser.fragments.DonateFragment;
import citu.teknoybuyandselluser.fragments.PendingFragment;
import citu.teknoybuyandselluser.fragments.RentFragment;
import citu.teknoybuyandselluser.fragments.RentedFragment;
import citu.teknoybuyandselluser.fragments.SellFragment;

/**
 * Created by Batistil on 1/19/2016.
 */
public class MyItemsAdapter extends FragmentPagerAdapter {

    public static final String ITEMS_TABS[] = {"Pending", "Sell", "Rent", "Donate", "Rented"};

    public MyItemsAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PendingFragment();
            case 1:
                return new SellFragment();
            case 2:
                return new RentFragment();
            case 3:
                return new DonateFragment();
            default:
                return new RentedFragment();
        }
    }

    @Override
    public int getCount() {
        return ITEMS_TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ITEMS_TABS[position];
    }
}