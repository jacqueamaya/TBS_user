package citu.teknoybuyandselluser.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.services.ExpirationCheckerService;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.SellItemActivity;
import citu.teknoybuyandselluser.adapters.SellItemsAdapter;
import citu.teknoybuyandselluser.models.SellItem;
import citu.teknoybuyandselluser.services.ItemsToSellService;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 ** 0.01 Initial Codes                      - J. Pedrano    - 12/24/2015
 ** 0.02 View Sell Items from database      - J. Amaya      - 12/31/2015
 ** 0.03 Working Floating Action Button     - J. Amaya      - 01/01/2016
 ** 0.04 Add Quantity to intent             - J. Amaya      - 01/06/2016
 */

public class SellFragment extends Fragment {
    private static final String TAG = "SellFragment";

    private ItemsRefreshBroadcastReceiver receiver;
    private RealmResults<SellItem> items;
    private SellItemsAdapter itemsAdapter;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtMessage;

    private String user;

    public SellFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.User.USERNAME, "");

        progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewSellItems);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        Realm realm = Realm.getDefaultInstance();
        items = realm.where(SellItem.class).equalTo(Constants.Item.OWNER_USER_USERNAME, user).findAll();
        itemsAdapter = new SellItemsAdapter(items);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerView.setAdapter(itemsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callItemsToSellService();
                itemsAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getBaseContext(), SellItemActivity.class));
            }
        });

        receiver = new ItemsRefreshBroadcastReceiver();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callItemsToSellService();

        FragmentActivity activity = getActivity();
        activity.registerReceiver(receiver, new IntentFilter(ItemsToSellService.class.getCanonicalName()));
        itemsAdapter.notifyDataSetChanged();

        activity.startService(new Intent(activity, ExpirationCheckerService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void callItemsToSellService() {
        FragmentActivity activity = getActivity();
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Intent intent = new Intent(activity, ItemsToSellService.class);
            intent.putExtra(Constants.User.USERNAME, user);
            activity.startService(intent);
            if(items.isEmpty())
                progressBar.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            showHideErrorMessage();
        }
    }

    public void showHideErrorMessage() {
        if(items.isEmpty()) {
            Log.e(TAG, "No items to sell cached" + items.size());
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(getResources().getString(R.string.no_items_to_sell));
        } else {
            txtMessage.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class ItemsRefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            showHideErrorMessage();
            itemsAdapter.notifyDataSetChanged();
            Log.e(TAG, intent.getStringExtra(Constants.RESPONSE));
            if (intent.getIntExtra(Constants.RESULT, 0) == -1) {
                Snackbar.make(recyclerView, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
