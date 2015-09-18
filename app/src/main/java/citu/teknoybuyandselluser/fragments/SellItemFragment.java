package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import citu.teknoybuyandselluser.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellItemFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment SellItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellItemFragment newInstance(String user) {
        SellItemFragment fragment = new SellItemFragment();
        Bundle args = new Bundle();
        args.putString("userId", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell_item, container, false);
    }

}
