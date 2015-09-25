package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.listAdapters.NotificationListAdapter;
import citu.teknoybuyandselluser.models.Notification;

public class NotificationsActivity extends BaseActivity {
    private static final String TAG = "Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        Server.getNotifications(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications = new ArrayList<Notification>();
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
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
                }

            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }
}
