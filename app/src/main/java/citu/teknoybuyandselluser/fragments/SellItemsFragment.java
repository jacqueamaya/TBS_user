package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.CustomListAdapterNotification;
import citu.teknoybuyandselluser.CustomListAdapterQueue;
import citu.teknoybuyandselluser.CustomListAdapterSellItems;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.SellForm;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.listAdapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private static final String TAG = "SellItemsFragment";
    private View view = null;

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

        view = inflater.inflate(R.layout.fragment_sell_items, container, false);
/*        List<String> soldItems = new ArrayList<String>();
        soldItems.add("General Sociology Book");
        soldItems.add("Rizal Book");
        soldItems.add("College Algebra book");
        soldItems.add("Calculator");

        List<String> itemImg = new ArrayList<String>();
        itemImg.add("");

        ListView lv = (ListView) view.findViewById(R.id.listViewSellItems);
        CustomListAdapterSellItems listAdapter = new CustomListAdapterSellItems(getActivity().getBaseContext(), R.layout.activity_sell_item , soldItems);
        lv.setAdapter(listAdapter);
*/
        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        Server.getItemsToSell(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> ownedItems = new ArrayList<Item>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if(jsonArray.length()==0){
                        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                        txtMessage.setText("You have no available items to be sold");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        ownedItems = Item.allItems(jsonArray);

                        ListView lv = (ListView) view.findViewById(R.id.listViewSellItems);
                        ItemsListAdapter listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.activity_item, ownedItems);
                        lv.setAdapter(listAdapter);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
            }
        });

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
