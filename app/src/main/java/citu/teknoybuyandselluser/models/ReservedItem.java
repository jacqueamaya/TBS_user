package citu.teknoybuyandselluser.models;


import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models_old.Item;

public class ReservedItem {
    private static final int DIVISOR = 1000;

    private int id;
    private int quantity;
    private int stars_to_use;
    private float payment;
    private Item item;
    private long reserved_date;
    private long reserved_expiration;
    private String item_code;
    private UserProfile buyer;

    private String status;
    private String str_reserved_date = Utils.parseDate(reserved_date);

    public String getStr_reserved_date() {
        return str_reserved_date;
    }

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

    public long getReserved_date() {
        return reserved_date;
    }

    public void setReserved_date(long reserved_date) {
        this.reserved_date = reserved_date;
    }

    public long getReserved_expiration() {
        return reserved_expiration;
    }

    public void setReserved_expiration(long reserved_expiration) {
        this.reserved_expiration = reserved_expiration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getStars_to_use() {
        return stars_to_use;
    }

    public void setStars_to_use(int stars_to_use) {
        this.stars_to_use = stars_to_use;
    }

    public float getDiscountedPrice() {
        float discount = (float) stars_to_use / DIVISOR;
        //discountedPrice = item.getPrice() * (1 - discount);
        return item.getPrice() * (1 - discount);
    }

    public float getPayment() {
        return payment;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getItem_code() {
        return item_code;
    }
}
