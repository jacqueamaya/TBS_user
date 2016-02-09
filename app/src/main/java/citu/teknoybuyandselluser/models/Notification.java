package citu.teknoybuyandselluser.models;

import citu.teknoybuyandselluser.models.Item;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Notification extends RealmObject{
    @PrimaryKey
    private int id;

    private User target;
    private User maker;
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

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public User getMaker() {
        return maker;
    }

    public void setMaker(User maker) {
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
    }
}
