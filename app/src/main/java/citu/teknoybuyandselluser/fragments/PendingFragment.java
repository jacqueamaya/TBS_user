package citu.teknoybuyandselluser.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import citu.teknoybuyandselluser.PendingItemActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.adapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;

/**
 ** 0.01 Initial Codes                      - J. Pedrano    - 12/24/2015
 ** 0.02 View Pending Items from database   - J. Amaya      - 12/31/2015
 */


public class PendingFragment extends Fragment {
    private static final String TAG = "Pending Fragment";
    private View view = null;
    private ItemsListAdapter listAdapter;

    private int mItemId;
    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mFormatPrice;

    public PendingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending, container, false);
        getPendingItems();
        return view;
    }

    public void getPendingItems() {

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);
        Server.getPendingItems(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> pendingItems;
                Log.v(TAG, responseBody);
                JSONArray jsonArray;

                try {
                    TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                    ListView lv = (ListView) view.findViewById(R.id.listViewPending);
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No pending items");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        pendingItems = Item.allItems(jsonArray);
                        listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.list_item, pendingItems);
                        lv.setVisibility(View.VISIBLE);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = listAdapter.getDisplayView().get(position);
                                mItemId = item.getId();
                                mItemName = item.getItemName();
                                mDescription = item.getDescription();
                                mPrice = item.getPrice();
                                mPicture = item.getPicture();
                                mStarsRequired = item.getStars_required();
                                mFormatPrice = item.getFormattedPrice();
                                String purpose = item.getPurpose();

                                Intent intent;
                                intent = new Intent(getActivity().getBaseContext(), PendingItemActivity.class);
                                intent.putExtra(Constants.ID, mItemId);
                                intent.putExtra(Constants.ITEM_NAME, mItemName);
                                intent.putExtra(Constants.DESCRIPTION, mDescription);
                                intent.putExtra(Constants.PRICE, mPrice);
                                intent.putExtra(Constants.PICTURE, mPicture);
                                intent.putExtra(Constants.STARS_REQUIRED, mStarsRequired);
                                intent.putExtra(Constants.FORMAT_PRICE, mFormatPrice);
                                intent.putExtra("purpose", purpose);

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
