package citu.teknoybuyandselluser.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.listAdapters.NotificationListAdapter;
import citu.teknoybuyandselluser.models.Notification;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private static final String TAG = "NotificationsFragment";
    private View view = null;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String user) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_notifications, container, false);

        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        Server.getNotifications(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications = new ArrayList<Notification>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if(jsonArray.length()==0){
                        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                        txtMessage.setText("No new notifications");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        notifications = Notification.allNotifications(jsonArray);

                        ListView lv = (ListView) view.findViewById(R.id.listViewNotif);
                        NotificationListAdapter listAdapter = new NotificationListAdapter(getActivity().getBaseContext(), R.layout.activity_notification_item, notifications);
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
        return view;
    }

}
