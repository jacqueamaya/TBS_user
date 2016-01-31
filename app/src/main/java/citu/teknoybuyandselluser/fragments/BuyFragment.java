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
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.BuyItemActivity;
import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ExpirationCheckerService;
import citu.teknoybuyandselluser.MakeTransactionsActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.adapters.GridAdapter;
import citu.teknoybuyandselluser.models.Item;


/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class BuyFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String TAG = "Buy Fragment";

    private GridAdapter gridAdapter;
    private Gson gson = new Gson();
    private String user;
    private View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.User.USERNAME, "");

        ((MakeTransactionsActivity) getActivity()).getCategories();

        return view;
    }

    public void getAllItemsToSell() {
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getAvailableItemsToSell(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> availableItems = gson.fromJson(responseBody, new TypeToken<ArrayList<Item>>() {
                }.getType());

                TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                GridView gridView = (GridView) view.findViewById(R.id.gridViewForBuy);

                if (availableItems.size() == 0) {
                    txtMessage.setText(getResources().getString(R.string.no_items_to_buy));
                    txtMessage.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                } else {
                    txtMessage.setVisibility(View.GONE);

                    gridAdapter = new GridAdapter(getActivity(), availableItems);
                    ((MakeTransactionsActivity) getActivity()).setGridAdapterForBuy(gridAdapter);
                    gridView.setAdapter(gridAdapter);
                    gridView.setVisibility(View.VISIBLE);
                    setItemClickListener(gridView);
                    ((MakeTransactionsActivity) getActivity()).populateCategories();
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
        getAllItemsToSell();

        Intent service = new Intent(getActivity().getBaseContext(), ExpirationCheckerService.class);
        service.putExtra("username", user);
        getActivity().startService(service);
    }

    public void setItemClickListener(AdapterView<?> adapterView) {
        adapterView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Item item = gridAdapter.getDisplayView().get(i);

        Log.e(TAG, item.getName());
        Intent intent;
        intent = new Intent(getActivity().getBaseContext(), BuyItemActivity.class);
        intent.putExtra(Constants.ID, item.getId());
        intent.putExtra(Constants.ITEM_NAME, item.getName());
        intent.putExtra(Constants.DESCRIPTION, item.getDescription());
        intent.putExtra(Constants.PRICE, item.getPrice());
        intent.putExtra(Constants.QUANTITY, item.getQuantity());
        intent.putExtra(Constants.PICTURE, item.getPicture());
        intent.putExtra(Constants.STARS_REQUIRED, item.getStars_required());
        intent.putExtra(Constants.FORMAT_PRICE, Utils.formatFloat(item.getPrice()));

        startActivity(intent);
    }
}