package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.DashboardActivity;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.listAdapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllDonationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllDonationsFragment extends Fragment {
    private static final String TAG = "AllDonationsFragment";
    private View view = null;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1
     * @return A new instance of fragment AllDonationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllDonationsFragment newInstance(String user) {
        AllDonationsFragment fragment = new AllDonationsFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_all_donations, container, false);

        view = inflater.inflate(R.layout.fragment_all_donations, container, false);
        ((DashboardActivity) getActivity()).setActionBarTitle("All Donations");

        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        Server.getAllDonations(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> allDonations = new ArrayList<Item>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                        txtMessage.setText("No available donations");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        allDonations = Item.allItems(jsonArray);

                        ListView lv = (ListView) view.findViewById(R.id.listViewDonations);
                        ItemsListAdapter listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.activity_item, allDonations);
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

        return view;
    }

}
