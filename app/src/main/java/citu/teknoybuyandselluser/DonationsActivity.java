package citu.teknoybuyandselluser;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

import citu.teknoybuyandselluser.adapters.GridAdapter;
import citu.teknoybuyandselluser.models.Category;
import citu.teknoybuyandselluser.models.Item;

public class DonationsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private ProgressBar progressBar;

    private Category categories[];
    private String categoryNames[];
    private String sortBy[];

    private GridAdapter gridAdapter;

    private String searchQuery = "";
    private String category = "";
    private String lowerCaseSort = "date";

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

        sortBy = getResources().getStringArray(R.array.donations_sort_by);

        getCategories();
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

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                gridAdapter.getFilter().filter(searchQuery + "," + category);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                gridAdapter.getFilter().filter(searchQuery + "," + category);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //return id == R.id.action_search || super.onOptionsItemSelected(item);
        switch (id) {
            case R.id.nav_sort_by_date:
                gridAdapter.sortItems(Constants.Sort.DATE);
                break;
            case R.id.nav_sort_by_name:
                gridAdapter.sortItems(Constants.Sort.NAME);
                break;
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
                    gridAdapter.sortItems(lowerCaseSort);
                    gridView.setVisibility(View.VISIBLE);
                    gridView.setAdapter(gridAdapter);

                    Spinner spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
                    setItemSelectedListener(spinnerSortBy);

                    if(categoryNames.length != 0) {
                        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DonationsActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategory.setAdapter(adapter);
                        setItemSelectedListener(spinnerCategory);
                    }

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Item item = gridAdapter.getDisplayView().get(position);

                            Intent intent;
                            intent = new Intent(DonationsActivity.this, DonatedItemActivity.class);
                            intent.putExtra(Constants.ID, item.getId());
                            intent.putExtra(Constants.ITEM_NAME, item.getName());
                            intent.putExtra(Constants.DESCRIPTION, item.getDescription());
                            intent.putExtra(Constants.QUANTITY, item.getQuantity());
                            intent.putExtra(Constants.PICTURE, item.getPicture());
                            intent.putExtra(Constants.STARS_REQUIRED, item.getStars_required());
                            startActivity(intent);
                        }
                    });
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

    public void setItemSelectedListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int spinnerId = adapterView.getId();
        switch (spinnerId){
            case R.id.spinnerSortBy:
                lowerCaseSort = sortBy[i].toLowerCase();
                gridAdapter.sortItems(lowerCaseSort);
                break;
            case R.id.spinnerCategory:
                String category = categoryNames[i];
                if (category.equals("All")) {
                    category = "";
                }
                gridAdapter.getFilter().filter(category);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
