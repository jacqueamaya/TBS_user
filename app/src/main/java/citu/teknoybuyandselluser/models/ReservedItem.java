package citu.teknoybuyandselluser.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import citu.teknoybuyandselluser.Utils;

public class ReservedItem {
    private static final String TAG = "ReservedItem";
    private static final int DIVISOR = 1000;

    private int itemId;
    private int reservationId;
    private int starsRequired;
    private int starsToUse;
    private float discountedPrice;
    private float price;
    private String description;
    private String itemName;
    private String picture;
    private String reserved_date;
    private String status;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public float getPrice() {
        return price;
    }

    public float getDiscountedPrice() {
        float discount = (float) getStarsToUse() / DIVISOR;
        discountedPrice = getPrice() * (1 - discount);
        return discountedPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getReserved_date() {
        return reserved_date;
    }

    public int getStarsToUse() {
        return starsToUse;
    }

    public void setStarsToUse(int starsToUse) {
        this.starsToUse = starsToUse;
    }

    public int getStarsRequired() {
        return starsRequired;
    }

    public static ReservedItem getReservedItem(JSONObject jsonObject){
        ReservedItem ri = new ReservedItem();
        Item item;

        try {
            ri.reservationId = jsonObject.getInt("id");
            ri.reserved_date = Utils.parseToDateOnly(jsonObject.getLong("reserved_date"));
            ri.status = jsonObject.getString("status");

            if(!jsonObject.isNull("item")){
                item = Item.getItem(jsonObject.getJSONObject("item"));
                ri.itemId = item.getId();
                ri.starsRequired = item.getStars_required();
                ri.starsToUse = item.getStarsToUse();
                ri.itemName = item.getItemName();
                ri.price = item.getPrice();
                ri.picture = item.getPicture();
                ri.description = item.getDescription();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ri;
    }

    public static ArrayList<ReservedItem> allReservedItems(JSONArray jsonArray){
        ArrayList<ReservedItem> reserved = new ArrayList<ReservedItem>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject reservedObject = null;
            try {
                reservedObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            ReservedItem ri = ReservedItem.getReservedItem(reservedObject);
            if (ri != null) {
                reserved.add(ri);
            }
        }
        return reserved;

    }
}
