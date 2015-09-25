package citu.teknoybuyandselluser.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import citu.teknoybuyandselluser.listAdapters.ReservedItemsAdapter;
import citu.teknoybuyandselluser.models.ReservedItem;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCartFragment extends Fragment {
    private static final String TAG = "ShoppingCartFragment";
    private View view = null;

    private int mItemId;
    private int mReservationId;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mReservedDate;

    public static ShoppingCartFragment newInstance(String user) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        Server.getAllReservations(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, responseBody);
                ArrayList<ReservedItem> reservations = new ArrayList<ReservedItem>();
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    Log.d(TAG, jsonArray.toString());
                    if(jsonArray.length()==0){
                        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                        txtMessage.setText("You have not reserved an item");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        reservations = ReservedItem.allReservedItems(jsonArray);

                        ListView lv = (ListView) view.findViewById(R.id.listViewReservations);
                        ReservedItemsAdapter listAdapter = new ReservedItemsAdapter(getActivity().getBaseContext(), R.layout.activity_item, reservations);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem item = (ReservedItem) parent.getItemAtPosition(position);
                                mItemId = item.getItemId();
                                mReservationId = item.getReservationId();
                                mItemName = item.getItemName();
                                mPrice = item.getPrice();
                                mReservedDate = item.getReserved_date();

                                Fragment fragment = null;
                                fragment = ReservedItemFragment.newInstance(mItemId, mReservationId, mItemName, mDescription, mPrice, mPicture, mReservedDate);
                                ((DashboardActivity) getActivity()).setActionBarTitle(mItemName);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.flContent, fragment).addToBackStack("tag");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.commit();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
