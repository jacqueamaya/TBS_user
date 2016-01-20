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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ExpirationCheckerService;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.RentItemDetailsActivity;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.adapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;

/**
 ** 0.01 Initial Codes                      - J. Pedrano    - 12/24/2015
 ** 0.02 View Rented Items from database    - J. Amaya      - 01/06/2016
 */

public class RentedFragment extends Fragment {
    private static final String TAG = "Rented Fragment";
    private View view = null;
    private ItemsListAdapter listAdapter;

    private SharedPreferences prefs;

    private String user;

    private Gson gson = new Gson();

    public RentedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rented, container, false);

        prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.USERNAME, "");

        getRentedItems();
        return view;
    }

    public void getRentedItems(){

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getRentedItems(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> mOwnedItems = new ArrayList<Item>();
                mOwnedItems = gson.fromJson(responseBody, new TypeToken<ArrayList<Item>>(){}.getType());
                ListView listView;

                TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                listView = (ListView) view.findViewById(R.id.listViewRentedItems);
                if (mOwnedItems.size() == 0) {
                    txtMessage.setText("No rented items");
                    txtMessage.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    txtMessage.setVisibility(View.GONE);
                    listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.list_item, mOwnedItems);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Item item = listAdapter.getDisplayView().get(position);

                            Intent intent;
                            intent = new Intent(getActivity().getBaseContext(), RentItemDetailsActivity.class);
                            intent.putExtra(Constants.ID, item.getId());
                            intent.putExtra(Constants.ITEM_NAME, item.getName());
                            intent.putExtra(Constants.DESCRIPTION, item.getDescription());
                            intent.putExtra(Constants.PICTURE, item.getPicture());
                            intent.putExtra(Constants.STARS_REQUIRED, item.getStars_required());
                            intent.putExtra(Constants.FORMAT_PRICE, Utils.formatFloat(item.getPrice()));
                            intent.putExtra(Constants.QUANTITY, item.getQuantity());
                            intent.putExtra(Constants.STATUS, item.getStatus());
                            startActivity(intent);
                        }
                    });
            }

        }

        @Override
        public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(getActivity().getBaseContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRentedItems();

        Intent service = new Intent(getActivity().getBaseContext(), ExpirationCheckerService.class);
        service.putExtra("username", user);
        getActivity().startService(service);
    }
}
