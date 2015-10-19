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
    private static final String TAG = "ReservedIten";

    private int itemId;
    private int reservationId;
    private int starsToUse;
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

    public static ReservedItem getReservedItem(JSONObject jsonObject){
        ReservedItem ri = new ReservedItem();
        Item item;
        DateFormat df=null;
        Date date=null;

        try {
            /*try {
                df = new SimpleDateFormat("yyyy-MM-dd");
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.getString("reserved_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            ri.reservationId = jsonObject.getInt("id");
            ri.reserved_date = Utils.parseToDateOnly(jsonObject.getLong("reserved_date"));
            ri.status = jsonObject.getString("status");

            if(!jsonObject.isNull("item")){
                item = Item.getItem(jsonObject.getJSONObject("item"));
                ri.itemId = item.getId();
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
