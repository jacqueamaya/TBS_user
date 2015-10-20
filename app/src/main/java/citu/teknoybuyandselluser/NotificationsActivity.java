package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.adapters.NotificationListAdapter;
import citu.teknoybuyandselluser.models.Notification;

public class NotificationsActivity extends BaseActivity {
    private static final String TAG = "Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        final TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressGetNotifs);
        progressBar.setVisibility(View.GONE);

        Server.getNotifications(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications;
                JSONArray jsonArray;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {

                        txtMessage.setText("No new notifications");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {

                        notifications = Notification.allNotifications(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewNotif);
                        NotificationListAdapter listAdapter = new NotificationListAdapter(NotificationsActivity.this, R.layout.item_notification, notifications);
                        lv.setAdapter(listAdapter);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(NotificationsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                    txtMessage.setText("No new notifications");
                    txtMessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(NotificationsActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }
}
