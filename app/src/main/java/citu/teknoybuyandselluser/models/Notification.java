package citu.teknoybuyandselluser.models;

public class Notification {

    private int id;
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
    }
}
