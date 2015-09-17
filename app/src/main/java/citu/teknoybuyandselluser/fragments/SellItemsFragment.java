package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselluser.CustomListAdapterNotification;
import citu.teknoybuyandselluser.CustomListAdapterQueue;
import citu.teknoybuyandselluser.CustomListAdapterSellItems;
import citu.teknoybuyandselluser.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment SellItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellItemsFragment newInstance(String user) {
        SellItemsFragment fragment = new SellItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    public SellItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sell_items, container, false);
        View view = null;
        view = inflater.inflate(R.layout.fragment_sell_items, container, false);
        List<String> soldItems = new ArrayList<String>();
        soldItems.add("General Sociology Book");
        soldItems.add("Rizal Book");
        soldItems.add("College Algebra book");
        soldItems.add("Calculator");

        List<String> itemImg = new ArrayList<String>();
        itemImg.add("");

        ListView lv = (ListView) view.findViewById(R.id.listViewSellItems);
        CustomListAdapterSellItems listAdapter = new CustomListAdapterSellItems(getActivity().getBaseContext(), R.layout.activity_sell_item , soldItems);
        lv.setAdapter(listAdapter);

        return view;
    }

}
