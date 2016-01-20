package citu.teknoybuyandselluser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import citu.teknoybuyandselluser.fragments.ItemsForDonationFragment;
import citu.teknoybuyandselluser.fragments.ItemsForRentFragment;
import citu.teknoybuyandselluser.fragments.ItemsOnSaleFragment;

/**
 * Created by Batistil on 1/19/2016.
 */
public class ReservationAdapter extends FragmentPagerAdapter {

    public static String [] RESERVED_ITEMS = {"Items On Sale", "Items For Rent", "Items For Donation"};

    public ReservationAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ItemsOnSaleFragment();
            case 1:
                return new ItemsForRentFragment();
            default:
                return new ItemsForDonationFragment();
        }
    }

    @Override
    public int getCount() {
        return RESERVED_ITEMS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return RESERVED_ITEMS[position];
    }
}