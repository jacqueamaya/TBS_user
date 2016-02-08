package citu.teknoybuyandselluser.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 ** Created by jack on 5/02/16.
 */
public class ReservedItemToDonate extends RealmObject{
    @PrimaryKey
    private int id;
    private UserProfile buyer;
    private Item item;
    private float payment;
    private int quantity;
    private int stars_to_use;
    private long reserved_date;
    private long request_expiration;
    private String item_code;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserProfile getBuyer() {
        return buyer;
    }

    public void setBuyer(UserProfile buyer) {
        this.buyer = buyer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStars_to_use() {
        return stars_to_use;
    }

    public void setStars_to_use(int stars_to_use) {
        this.stars_to_use = stars_to_use;
    }

    public long getReserved_date() {
        return reserved_date;
    }

    public void setReserved_date(long reserved_date) {
        this.reserved_date = reserved_date;
    }

    public long getRequest_expiration() {
        return request_expiration;
    }

    public void setRequest_expiration(long request_expiration) {
        this.request_expiration = request_expiration;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
