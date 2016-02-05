package citu.teknoybuyandselluser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselluser.adapters.NotificationsAdapter;
import citu.teknoybuyandselluser.models.Notification;
import citu.teknoybuyandselluser.services.NotificationService;
import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationsActivity extends BaseActivity {
    private static final String TAG = "NotificationsActivity";
    private NotificationsAdapter notificationsAdapter;
    private NotificationRefreshBroadcastReceiver notificationRefreshBroadcastReceiver;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();

        final TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressGetNotifs);
        progressBar.setVisibility(View.GONE);
        notificationRefreshBroadcastReceiver = new NotificationRefreshBroadcastReceiver();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Notification> notifications = realm.where(Notification.class).findAll();

        if(notifications.isEmpty()) {
            Log.e(TAG, "No notifications cached" + notifications.size());
            txtMessage.setText("No notifications cached");
        }

        notificationsAdapter = new NotificationsAdapter(notifications);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listViewNotif);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.setAdapter(notificationsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(NotificationsActivity.this, "Refreshing ...", Toast.LENGTH_SHORT).show();
                // call this after refreshing is done
                getNotifications();
                notificationsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getNotifications();

    }
    private void getNotifications() {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(Constants.User.USERNAME, getUserName());
        startService(intent);
    }


    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(notificationRefreshBroadcastReceiver, new IntentFilter(NotificationService.class.getCanonicalName()));
        notificationsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notificationRefreshBroadcastReceiver);
    }

    private class NotificationRefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, intent.getStringExtra("response"));
        }

    }
}
