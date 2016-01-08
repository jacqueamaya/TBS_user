package citu.teknoybuyandselluser.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.BuyItemActivity;
import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.MakeTransactionsActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.adapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Category;
import citu.teknoybuyandselluser.models.Item;

/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class ForRentFragment extends Fragment{
    private static final String TAG = "For Rent Fragment";
    private View view = null;

    private ArrayList<Item> availableItems;
    private ItemsListAdapter listAdapter;
    private ProgressBar progressBar;
    private TextView txtCategory;

    private String categories[];
    private String sortBy[];

    private String category = "";
    private String lowerCaseSort = "price";

    public ForRentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_for_rent, container, false);
        //txtCategory = (TextView) view.findViewById(R.id.txtCategory);
        progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        sortBy = getResources().getStringArray(R.array.sort_by);

        //getItems();
        getAllItemsForRent();
        /*getCategories();
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog displayCategories = new AlertDialog.Builder(getActivity().getBaseContext())
                        .setTitle("Categories")
                        .setItems(categories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtCategory.setText(categories[which]);
                                category = txtCategory.getText().toString();
                                if (category.equals("All")) {
                                    category = "";
                                }
                                listAdapter.getFilter().filter(category);
                            }
                        })
                        .create();
                displayCategories.show();
            }
        });*/
        return view;
    }
/*
    public void getItems() {
        if (txtCategory.getText().toString().equals("Categories")) {
            getAllItemsForRent();
        }
    }*/

    public void getAllItemsForRent() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString(Constants.USERNAME, "");

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getAvailableItemsForRent(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                availableItems = new ArrayList<>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray;

                try {
                    TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                    ListView lv = (ListView) view.findViewById(R.id.listViewRentItems);
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No available items for rent");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        availableItems = Item.allItems(jsonArray);
                        listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.list_item, availableItems);
                        ((MakeTransactionsActivity) getActivity()).setListAdapter(listAdapter);
                        listAdapter.sortItems(lowerCaseSort);
                        lv.setVisibility(View.VISIBLE);
                        lv.setAdapter(listAdapter);

                        Spinner spinnerSortBy = (Spinner) view.findViewById(R.id.spinnerSortBy);
                        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                lowerCaseSort = sortBy[position].toLowerCase();
                                listAdapter.sortItems(lowerCaseSort);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = listAdapter.getDisplayView().get(position);

                                Intent intent;
                                intent = new Intent(getActivity().getBaseContext(), BuyItemActivity.class);
                                intent.putExtra(Constants.ID, item.getId());
                                intent.putExtra(Constants.ITEM_NAME, item.getItemName());
                                intent.putExtra(Constants.DESCRIPTION, item.getDescription());
                                intent.putExtra(Constants.PRICE, item.getPrice());
                                intent.putExtra(Constants.QUANTITY, item.getQuantity());
                                intent.putExtra(Constants.PICTURE, item.getPicture());
                                intent.putExtra(Constants.STARS_REQUIRED, item.getStars_required());
                                intent.putExtra(Constants.FORMAT_PRICE, item.getFormattedPrice());

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

    public void getCategories() {
        progressBar.setVisibility(View.GONE);
        Server.getCategories(progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONArray json = new JSONArray(responseBody);
                    if (json.length() != 0) {
                        categories = Category.getAllCategories(new JSONArray(responseBody));
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Empty categories", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                categories = null;
                Toast.makeText(getActivity().getBaseContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //txtCategory.setText("Categories");
        //getItems();
        getAllItemsForRent();
    }

}