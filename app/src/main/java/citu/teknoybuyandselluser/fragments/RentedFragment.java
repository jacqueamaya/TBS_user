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

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ExpirationCheckerService;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.RentedItemDetailActivity;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.adapters.RentedItemsAdapter;
import citu.teknoybuyandselluser.models.RentedItem;

/**
 ** 0.01 Initial Codes                      - J. Pedrano    - 12/24/2015
 ** 0.02 View Rented Items from database    - J. Amaya      - 01/06/2016
 */

public class RentedFragment extends Fragment {
    private View view = null;
    private RentedItemsAdapter listAdapter;

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

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.User.USERNAME, "");

        getRentedItems();
        return view;
    }

    public void getRentedItems(){

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getRentedItems(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<RentedItem> mOwnedItems = gson.fromJson(responseBody, new TypeToken<ArrayList<RentedItem>>(){}.getType());
                ListView listView;

                TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                listView = (ListView) view.findViewById(R.id.listViewRentedItems);
                if (mOwnedItems.size() == 0) {
                    txtMessage.setText(getResources().getString(R.string.no_rented_items));
                            txtMessage.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    txtMessage.setVisibility(View.GONE);
                    listAdapter = new RentedItemsAdapter(getActivity().getBaseContext(), R.layout.list_item, mOwnedItems);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            RentedItem rent = listAdapter.getDisplayView().get(position);

                            Intent intent;
                            intent = new Intent(getActivity().getBaseContext(), RentedItemDetailActivity.class);
                            intent.putExtra(Constants.ID, rent.getId());
                            intent.putExtra(Constants.ITEM_NAME, rent.getItem().getName());
                            intent.putExtra(Constants.DESCRIPTION, rent.getItem().getDescription());
                            intent.putExtra(Constants.PICTURE, rent.getItem().getPicture());
                            intent.putExtra(Constants.STARS_REQUIRED, rent.getItem().getStars_required());
                            intent.putExtra(Constants.FORMAT_PRICE, Utils.formatFloat(rent.getItem().getPrice()));
                            intent.putExtra(Constants.PENALTY, rent.getPenalty());
                            intent.putExtra(Constants.QUANTITY, rent.getQuantity());
                            intent.putExtra(Constants.RENT_DATE, rent.getRent_date());
                            intent.putExtra(Constants.RENT_EXPIRATION, rent.getRent_expiration());
                            startActivity(intent);
                        }
                    });
            }

        }

        @Override
        public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(getActivity().getBaseContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRentedItems();

        Intent service = new Intent(getActivity().getBaseContext(), ExpirationCheckerService.class);
        service.putExtra(Constants.User.USERNAME, user);
        getActivity().startService(service);
    }
}
