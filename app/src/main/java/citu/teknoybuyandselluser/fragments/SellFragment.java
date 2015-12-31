package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import citu.teknoybuyandselluser.SellItemDetailsActivity;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.adapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;

/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class SellFragment extends Fragment {
    private static final String TAG = "Sell Fragment";
    private View view = null;
    private ItemsListAdapter listAdapter;

    private int mStarsRequired;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mFormatPrice;
    private String mStatus;

    public SellFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sell, container, false);
        getSellItems();
        return view;
    }

    public void getSellItems() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getItemsToSell(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;
                ArrayList<Item> mOwnedItems = new ArrayList<Item>();
                ListView listView;

                try {
                    TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                    listView = (ListView) view.findViewById(R.id.listViewSellItems);
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No available items to sell");
                        txtMessage.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        mOwnedItems = Item.allItems(jsonArray);
                        listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.list_item, mOwnedItems);
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(listAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = listAdapter.getDisplayView().get(position);
                                mItemName = item.getItemName();
                                mDescription = item.getDescription();
                                mPicture = item.getPicture();
                                mStarsRequired = item.getStars_required();
                                mFormatPrice = item.getFormattedPrice();
                                mStatus = item.getStatus();

                                Intent intent;
                                intent = new Intent(getActivity().getBaseContext(), SellItemDetailsActivity.class);
                                intent.putExtra(Constants.ID, item.getId());
                                intent.putExtra(Constants.ITEM_NAME, mItemName);
                                intent.putExtra(Constants.DESCRIPTION, mDescription);
                                intent.putExtra(Constants.PICTURE, mPicture);
                                intent.putExtra(Constants.STARS_REQUIRED, mStarsRequired);
                                intent.putExtra(Constants.FORMAT_PRICE, mFormatPrice);
                                intent.putExtra(Constants.STATUS, mStatus);
                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
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
