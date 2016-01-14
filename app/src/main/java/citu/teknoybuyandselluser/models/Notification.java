package citu.teknoybuyandselluser.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification {
    private String makerUserName;
    private String itemName;
    private String message;
    private String notification_type;
    private long notification_date;

    public String getMakerUserName() {
        return makerUserName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public long getNotification_date() {
        return notification_date;
    }

    public String getMessage() {
        return message;
    }

    public static Notification getNotification(JSONObject jsonObject){
        Notification n = new Notification();
        JSONObject item,maker,maker_student;

        try {
            n.notification_type=jsonObject.getString("notification_type");
            n.notification_date=jsonObject.getLong("notification_date");
            n.message = jsonObject.getString("message");

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
    /*private int id;
    private Users target;
    private Users maker;
    private Item item;
    private String message;
    private String notification_type;
    private String status;
    private long notification_date;
    private long notification_expiration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getTarget() {
        return target;
    }

    public void setTarget(Users target) {
        this.target = target;
    }

    public Users getMaker() {
        return maker;
    }

    public void setMaker(Users maker) {
        this.maker = maker;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(long notification_date) {
        this.notification_date = notification_date;
    }

    public long getNotification_expiration() {
        return notification_expiration;
    }

    public void setNotification_expiration(long notification_expiration) {
        this.notification_expiration = notification_expiration;
    }*/
}
