package citu.teknoybuyandselluser.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import citu.teknoybuyandselluser.adapters.PendingItemsAdapter;
import citu.teknoybuyandselluser.models.PendingItem;
import citu.teknoybuyandselluser.services.PendingItemsService;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 ** 0.01 Initial Codes                      - J. Pedrano    - 12/24/2015
 ** 0.02 View Pending Items from database   - J. Amaya      - 12/31/2015
 */


public class PendingFragment extends Fragment {
    private static final String TAG = "PendingFragment";

    private ItemsRefreshBroadcastReceiver receiver;
    private PendingItemsAdapter itemsAdapter;
    private RealmResults<PendingItem> items;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtMessage;

    private String user;

    public PendingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        user = prefs.getString(Constants.User.USERNAME, "");

        progressBar = (ProgressBar) view.findViewById(R.id.progressGetItems);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewPending);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        Realm realm = Realm.getDefaultInstance();
        items = realm.where(PendingItem.class).equalTo(Constants.Item.OWNER_USER_USERNAME, user).findAll();
        itemsAdapter = new PendingItemsAdapter(items);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerView.setAdapter(itemsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callPendingService();
                itemsAdapter.notifyDataSetChanged();
            }
        });

        receiver = new ItemsRefreshBroadcastReceiver();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callPendingService();

        FragmentActivity activity = getActivity();
        activity.registerReceiver(receiver, new IntentFilter(PendingItemsService.ACTION));
        itemsAdapter.notifyDataSetChanged();

        activity.startService(new Intent(activity, ExpirationCheckerService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void callPendingService() {
        FragmentActivity activity = getActivity();
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Intent intent = new Intent(activity, PendingItemsService.class);
            intent.putExtra(Constants.User.USERNAME, user);
            getActivity().startService(intent);
            if(items.isEmpty())
                progressBar.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            showHideErrorMessage();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showHideErrorMessage() {
        if(items.isEmpty()) {
            Log.e(TAG, "No pending items cached" + items.size());
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(getResources().getString(R.string.no_pending_items));
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
