package citu.teknoybuyandselluser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import citu.teknoybuyandselluser.fragments.BuyFragment;
import citu.teknoybuyandselluser.fragments.ForRentFragment;

/**
 * Created by Batistil on 1/19/2016.
 */
public class MakeTransactionsAdapter extends FragmentPagerAdapter {
    public static String [] MAKE_TRANSACTION_ITEMS = {"Buy","For Rent"};

    public MakeTransactionsAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new BuyFragment();
            default:
                return new ForRentFragment();
        }
    }

    @Override
    public int getCount() {
        return MAKE_TRANSACTION_ITEMS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MAKE_TRANSACTION_ITEMS[position];
    }
}
