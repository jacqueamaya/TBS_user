package citu.teknoybuyandselluser.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification {
    private static final String TAG = "Notification";
    private String makerUserName;
    private String itemName;
    private String notification_type;
    private String notification_date;

    public String getMakerUserName() {
        return makerUserName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public static Notification getNotification(JSONObject jsonObject){
        Notification n = new Notification();
        JSONObject item,maker,maker_student;

        try {
            n.notification_type=jsonObject.getString("notification_type");
            n.notification_date=jsonObject.getString("notification_date");

            if(!jsonObject.isNull("item")){
                item = jsonObject.getJSONObject("item");

                n.itemName = item.getString("name");
            }

            if(!jsonObject.isNull("maker")){
                maker = jsonObject.getJSONObject("maker");
                if(maker.getString("username") != "admin") {
                    n.makerUserName = maker.getString("username");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return n;
    }

    public static ArrayList<Notification> allNotifications(JSONArray jsonArray) {
        ArrayList<Notification> notifications = new ArrayList<Notification>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject notificationObject = null;
            try {
                notificationObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Notification notification = Notification.getNotification(notificationObject);
            if (notification != null) {
                notifications.add(notification);
            }
        }

        return notifications;
    }
}
