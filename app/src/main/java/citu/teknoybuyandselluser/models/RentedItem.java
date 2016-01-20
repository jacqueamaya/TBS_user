package citu.teknoybuyandselluser.models;

/**
 * Created by Batistil on 1/20/2016.
 */
public class RentedItem {
    private int id;
    private Users renter;
    private Item item;
    private int quantity;
    private String item_code;
    private long rent_date;
    private long  rent_expiration;
    private float penalty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getRenter() {
        return renter;
    }

    public void setRenter(Users renter) {
        this.renter = renter;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public long getRent_date() {
        return rent_date;
    }

    public void setRent_date(long rent_date) {
        this.rent_date = rent_date;
    }

    public long getRent_expiration() {
        return rent_expiration;
    }

    public void setRent_expiration(long rent_expiration) {
        this.rent_expiration = rent_expiration;
    }

    public float getPenalty() {
        return penalty;
    }

    public void setPenalty(float penalty) {
        this.penalty = penalty;
    }
}
