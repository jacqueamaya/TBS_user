package citu.teknoybuyandselluser.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 ** Created by jack on 5/02/16.
 */
public class SellItem extends RealmObject {
    @PrimaryKey
    private int id;
    private UserProfile owner;
    private String name;
    private String description;
    private Category category;
    private String status;
    private String purpose;
    private float price;
    private int quantity;
    private int reserved_quantity;
    private String picture;
    private int stars_required;
    private int stars_to_use;
    private long date_approved;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReserved_quantity() {
        return reserved_quantity;
    }

    public void setReserved_quantity(int reserved_quantity) {
        this.reserved_quantity = reserved_quantity;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getStars_required() {
        return stars_required;
    }

    public void setStars_required(int stars_required) {
        this.stars_required = stars_required;
    }

    public int getStars_to_use() {
        return stars_to_use;
    }

    public void setStars_to_use(int stars_to_use) {
        this.stars_to_use = stars_to_use;
    }

    public long getDate_approved() {
        return date_approved;
    }

    public void setDate_approved(long date_approved) {
        this.date_approved = date_approved;
    }
}
