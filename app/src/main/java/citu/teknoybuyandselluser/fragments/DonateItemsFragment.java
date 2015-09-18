package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselluser.CustomListAdapterQueue;
import citu.teknoybuyandselluser.CustomListAdapterSellItems;
import citu.teknoybuyandselluser.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonateItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment DonateItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonateItemsFragment newInstance(String user) {
        DonateItemsFragment fragment = new DonateItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_donate_items, container, false);
        View view = null;
        view = inflater.inflate(R.layout.fragment_donate_items, container, false);
        List<String> soldItems = new ArrayList<String>();
        soldItems.add("Footmop");
        soldItems.add("Paintbrushes");
        soldItems.add("College Algebra book");
        soldItems.add("Physics book");

        List<String> itemImg = new ArrayList<String>();
        itemImg.add("");

        ListView lv = (ListView) view.findViewById(R.id.listViewDonateItems);
        CustomListAdapterSellItems listAdapter = new CustomListAdapterSellItems(getActivity().getBaseContext(), R.layout.activity_donate_item , soldItems);
        lv.setAdapter(listAdapter);

        FloatingActionButton fab= (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = SellItemFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContent, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });

        return view;
    }

}
