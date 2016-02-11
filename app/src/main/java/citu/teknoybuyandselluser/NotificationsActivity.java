package citu.teknoybuyandselluser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselluser.adapters.NotificationsAdapter;
import citu.teknoybuyandselluser.models.Notification;
import citu.teknoybuyandselluser.services.NotificationService;
import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationsActivity extends BaseActivity {
    private static final String TAG = "NotificationsActivity";

    private NotificationsAdapter notificationsAdapter;
    private NotificationRefreshBroadcastReceiver broadcastReceiver;
    private RealmResults<Notification> notifications;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();

        recyclerView = (RecyclerView) findViewById(R.id.listViewNotif);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        Realm realm = Realm.getDefaultInstance();
        notifications = realm.where(Notification.class).findAll();
        notificationsAdapter = new NotificationsAdapter(notifications);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.setAdapter(notificationsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // call this after refreshing is done
                startNotificationService();
                notificationsAdapter.notifyDataSetChanged();
            }
        });

        broadcastReceiver = new NotificationRefreshBroadcastReceiver();
    }

    private void startNotificationService() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra(Constants.User.USERNAME, getUserName());
            startService(intent);
        } else
            Snackbar.make(recyclerView, Constants.NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG).show();
    }

    public void showHideErrorMessage() {
        if(notifications.isEmpty()) {
            Log.e(TAG, "No notifications cached" + notifications.size());
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(getResources().getString(R.string.no_notifications));
        } else {
            txtMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startNotificationService();

        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.ACTION));
        notificationsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private class NotificationRefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            showHideErrorMessage();
            notificationsAdapter.notifyDataSetChanged();
            Log.e(TAG, intent.getStringExtra(Constants.RESPONSE));
            if (intent.getIntExtra(Constants.RESULT, 0) == -1) {
                Snackbar.make(recyclerView, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }

    }
}
