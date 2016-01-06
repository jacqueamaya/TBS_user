package citu.teknoybuyandselluser.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.ReservedItemActivity;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.adapters.ReservedItemsAdapter;
import citu.teknoybuyandselluser.models.ReservedItem;

/**
 ** 0.01 Initial Codes                                  - J. Pedrano    - 12/24/2015
 ** 0.02 View Reserved Items on Sale from database      - J. Amaya      - 01/06/2016
 */

public class ItemsOnSaleFragment extends Fragment {
    private static final String TAG = "Items On Sale Fragment";
    private View view = null;

    public ItemsOnSaleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_items_on_sale, container, false);
        getReservedItemsOnSale();
        return view;
    }

    public void getReservedItemsOnSale() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString(Constants.USERNAME, "");

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getReservedItemsOnSale(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, responseBody);
                ArrayList<ReservedItem> reservations;
                JSONArray jsonArray;

                try {
                    ListView lv = (ListView) view.findViewById(R.id.listViewItemsOnSale);
                    TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                    jsonArray = new JSONArray(responseBody);
                    Log.d(TAG, jsonArray.toString());
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No reserved items on sale");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        reservations = ReservedItem.allReservedItems(jsonArray);
                        ReservedItemsAdapter listAdapter = new ReservedItemsAdapter(getActivity().getBaseContext(), R.layout.list_item, reservations);
                        lv.setVisibility(View.VISIBLE);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem item = (ReservedItem) parent.getItemAtPosition(position);

                                Intent intent;
                                intent = new Intent(getActivity().getBaseContext(), ReservedItemActivity.class);
                                intent.putExtra(Constants.ID, item.getItemId());
                                intent.putExtra(Constants.RESERVATION_ID, item.getReservationId());
                                intent.putExtra(Constants.ITEM_NAME, item.getItemName());
                                intent.putExtra(Constants.DESCRIPTION, item.getDescription());
                                intent.putExtra(Constants.PRICE, item.getPrice());
                                intent.putExtra(Constants.STARS_REQUIRED, item.getStarsRequired());
                                intent.putExtra(Constants.STARS_TO_USE, item.getStarsToUse());
                                intent.putExtra(Constants.DISCOUNTED_PRICE, item.getDiscountedPrice());
                                intent.putExtra(Constants.PICTURE, item.getPicture());
                                intent.putExtra(Constants.RESERVED_DATE, item.getReserved_date());

                                startActivity(intent);
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
                Toast.makeText(getActivity().getBaseContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
