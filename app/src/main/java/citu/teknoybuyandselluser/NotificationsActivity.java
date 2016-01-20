package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.adapters.NotificationListAdapter;
import citu.teknoybuyandselluser.models.Notification;

public class NotificationsActivity extends BaseActivity {
    private Gson gson  = new Gson();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_notifications);
        setupUI();

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString(Constants.USERNAME,"");
        final TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressGetNotifs);
        progressBar.setVisibility(View.GONE);

        Server.getNotifications(user, progressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications = new ArrayList<Notification>();
                notifications = gson.fromJson(responseBody, new TypeToken<ArrayList<Notification>>(){}.getType());
                    if (notifications.size() == 0) {

                        txtMessage.setText(getResources().getString(R.string.no_notifications));
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {

                        ListView lv = (ListView) findViewById(R.id.listViewNotif);
                        NotificationListAdapter listAdapter = new NotificationListAdapter(NotificationsActivity.this, R.layout.item_notification, notifications);
                        lv.setAdapter(listAdapter);
                    }

                /*} catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(NotificationsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                    txtMessage.setText("No new notifications");
                    txtMessage.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Toast.makeText(NotificationsActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent service = new Intent(NotificationsActivity.this, ExpirationCheckerService.class);
        service.putExtra("username",prefs.getString(Constants.USERNAME, ""));
        startService(service);
    }
}
