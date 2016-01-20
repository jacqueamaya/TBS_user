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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.ExpirationCheckerService;
import citu.teknoybuyandselluser.MakeTransactionsActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.RentItemActivity;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.adapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Category;
import citu.teknoybuyandselluser.models.Item;

/**
 ** 0.01 initially created by J. Pedrano on 12/24/15
 */

public class ForRentFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "For Rent Fragment";
    private View view = null;

    private ArrayList<Item> availableItems;
    private ItemsListAdapter listAdapter;
    private ProgressBar progressBar;

    private Category categories[];
    private String categoryNames[];
    private String sortBy[];
    private String user;

    private String lowerCaseSort = "price";

    private Gson gson = new Gson();

    public ForRentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_for_rent, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);


        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.USERNAME, "");

        sortBy = getResources().getStringArray(R.array.sort_by);

        getCategories();

        return view;
    }

    public void getAllItemsForRent() {

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        Server.getAvailableItemsForRent(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                availableItems = gson.fromJson(responseBody, new TypeToken<ArrayList<Item>>(){}.getType());

                TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                ListView lv = (ListView) view.findViewById(R.id.listViewRentItems);
                    if (availableItems.size() == 0) {
                        txtMessage.setText(getResources().getString(R.string.no_items_for_rent));
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.list_item, availableItems);
                        ((MakeTransactionsActivity) getActivity()).setListAdapterForRent(listAdapter);
                        listAdapter.sortItems(lowerCaseSort);
                        lv.setVisibility(View.VISIBLE);
                        lv.setAdapter(listAdapter);

                        Spinner spinnerSortBy = (Spinner) view.findViewById(R.id.spinnerSortBy);
                        sortOrFilter(spinnerSortBy);

                        if(categoryNames.length != 0) {
                            Spinner spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCategory.setAdapter(adapter);
                            sortOrFilter(spinnerCategory);
                        }

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = listAdapter.getDisplayView().get(position);

                                Intent intent;
                                intent = new Intent(getActivity().getBaseContext(), RentItemActivity.class);
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

    public void getCategories() {
        progressBar.setVisibility(View.GONE);
        Server.getCategories(progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                if (!("".equals(responseBody))) {
                    categories = gson.fromJson(responseBody, Category[].class);
                    categoryNames = new String[categories.length + 1];
                    categoryNames[0] = "All";
                    for (int i = 1; i < categoryNames.length; i++) {
                        categoryNames[i] = categories[i - 1].getCategory_name();
                    }
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Empty categories", Toast.LENGTH_SHORT).show();
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
        getAllItemsForRent();

        Intent service = new Intent(getActivity().getBaseContext(), ExpirationCheckerService.class);
        service.putExtra("username", user);
        getActivity().startService(service);
    }

    public void sortOrFilter(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int spinnerId = adapterView.getId();
        switch (spinnerId){
            case R.id.spinnerSortBy:
                lowerCaseSort = sortBy[i].toLowerCase();
                listAdapter.sortItems(lowerCaseSort);
                break;
            case R.id.spinnerCategory:
                String category = categoryNames[i];
                if (category.equals("All")) {
                    category = "";
                }
                listAdapter.getFilter().filter(category);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}