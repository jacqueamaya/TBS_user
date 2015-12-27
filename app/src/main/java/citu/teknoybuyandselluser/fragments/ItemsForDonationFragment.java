package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import citu.teknoybuyandselluser.R;
/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class ItemsForDonationFragment extends Fragment {
    public ItemsForDonationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_for_donation, container, false);
    }
}
