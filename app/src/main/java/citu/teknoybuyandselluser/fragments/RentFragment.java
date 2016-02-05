package citu.teknoybuyandselluser.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.services.ExpirationCheckerService;
import citu.teknoybuyandselluser.ForRentItemActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.adapters.RentItemsAdapter;
import citu.teknoybuyandselluser.models.RentItem;
import citu.teknoybuyandselluser.services.ItemsForRentService;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 ** 0.01 Initial Codes                          - J. Pedrano    - 12/24/2015
 ** 0.02 View Items for Rent from database      - J. Amaya      - 01/06/2016
 */

public class RentFragment extends Fragment {
    private static final String TAG = "RentFragment";
    private RentItemsAdapter itemsAdapter;
    private ProgressBar progressBar;
    private ItemsRefreshBroadcastReceiver receiver;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String user;

    public RentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.User.USERNAME, "");
        progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        receiver = new ItemsRefreshBroadcastReceiver();

        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<RentItem> items = realm.where(RentItem.class).equalTo(Constants.Item.OWNER_USER_USERNAME, user).findAll();

        if(items.isEmpty()) {
            Log.e(TAG, "No items cached" + items.size());
            //progressBar.setVisibility(View.VISIBLE);
            txtMessage.setVisibility(View.VISIBLE);
            String errorMessage = "No items for rent";
            txtMessage.setText(errorMessage);
        }

        itemsAdapter = new RentItemsAdapter(items);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewRentItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerView.setAdapter(itemsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Refreshing ...", Toast.LENGTH_SHORT).show();
                // call this after refreshing is done
                callItemsForRentService();
                itemsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity().getBaseContext(), ForRentItemActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        callItemsForRentService();

        FragmentActivity activity = getActivity();
        activity.registerReceiver(receiver, new IntentFilter(ItemsForRentService.class.getCanonicalName()));
        itemsAdapter.notifyDataSetChanged();

        activity.startService(new Intent(activity, ExpirationCheckerService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void callItemsForRentService() {
        FragmentActivity activity = getActivity();
        Intent intent = new Intent(activity.getBaseContext(), ItemsForRentService.class);
        intent.putExtra(Constants.User.USERNAME, user);
        activity.startService(intent);
    }

    private class ItemsRefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("type").equals("Rent")) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                itemsAdapter.notifyDataSetChanged();
                Log.e(TAG, intent.getStringExtra("response"));
                if (intent.getIntExtra("result", 0) == -1) {
                    Snackbar.make(recyclerView, "No internet connection", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}
