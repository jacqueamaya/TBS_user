package citu.teknoybuyandselluser;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

import citu.teknoybuyandselluser.adapters.GridAdapter;
import citu.teknoybuyandselluser.models_old.Category;
import citu.teknoybuyandselluser.models_old.Item;
import citu.teknoybuyandselluser.services.ExpirationCheckerService;

public class DonationsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private ProgressBar progressBar;

    private Category categories[];
    private String categoryNames[];

    private GridAdapter gridAdapter;

    private String searchQuery = "";
    private String category = "";

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_donations);
        setupUI();

        progressBar = (ProgressBar) findViewById(R.id.progressGetItems);
        progressBar.setVisibility(View.GONE);

        getCategories();

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllItems();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllItems();

        Intent service = new Intent(DonationsActivity.this, ExpirationCheckerService.class);
        service.putExtra(Constants.User.USERNAME, getUserName());
        startService(service);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_donations, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                if (gridAdapter != null) {
                    gridAdapter.getFilter().filter(searchQuery + "," + category);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                if (gridAdapter != null) {
                    gridAdapter.getFilter().filter(searchQuery + "," + category);
                    return true;
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(gridAdapter != null) {
            switch (id) {
                case R.id.nav_sort_by_date:
                    gridAdapter.sortItems(Constants.Sort.DATE);
                    break;
                case R.id.nav_sort_by_name:
                    gridAdapter.sortItems(Constants.Sort.NAME);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_stars_collected;
    }

    public void getAllItems() {

        Server.getAllDonations(getUserName(), progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> allDonations;
                allDonations = gson.fromJson(responseBody, new TypeToken<ArrayList<Item>>(){}.getType());

                TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                GridView gridView = (GridView) findViewById(R.id.gridViewForDonations);

                if (allDonations.size() == 0) {
                    txtMessage.setText(getResources().getString(R.string.no_donations));
                    txtMessage.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                } else {
                    txtMessage.setVisibility(View.GONE);
                    gridAdapter = new GridAdapter(DonationsActivity.this, allDonations);
                    gridView.setVisibility(View.VISIBLE);
                    gridView.setAdapter(gridAdapter);

                    if(categoryNames.length != 0) {
                        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DonationsActivity.this, R.layout.spinner_item, categoryNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategory.setAdapter(adapter);
                        setItemSelectedListener(spinnerCategory);
                    }

                    setItemClickListener(gridView);
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(DonationsActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DonationsActivity.this, "Empty categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                categories = null;
                Toast.makeText(DonationsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setItemClickListener(GridView gridView){
        gridView.setOnItemClickListener(this);
    }

    public void setItemSelectedListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int spinnerId = adapterView.getId();
        switch (spinnerId){
            case R.id.spinnerCategory:
                String category = categoryNames[i];
                if (category.equals("All")) {
                    category = "";
                }

                if(gridAdapter != null)
                    gridAdapter.getFilter().filter(category);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Item item = gridAdapter.getDisplayView().get(position);

        Intent intent;
        intent = new Intent(DonationsActivity.this, DonatedItemActivity.class);
        intent.putExtra(Constants.Item.ID, item.getId());
        intent.putExtra(Constants.Item.ITEM_NAME, item.getName());
        intent.putExtra(Constants.Item.DESCRIPTION, item.getDescription());
        intent.putExtra(Constants.Item.QUANTITY, item.getQuantity());
        intent.putExtra(Constants.Item.PICTURE, item.getPicture());
        intent.putExtra(Constants.Item.STARS_REQUIRED, item.getStars_required());
        startActivity(intent);
    }
}
