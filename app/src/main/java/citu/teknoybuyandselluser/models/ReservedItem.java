package citu.teknoybuyandselluser.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservedItem {
    private static final String TAG = "ReservedIten";
    private int itemId;
    private int reservationId;
    private String itemName;
    private float price;
    private String status;
    private String reserved_date;
    private String description;

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

    public static ReservedItem getReservedItems(JSONObject jsonObject){
        ReservedItem ri = new ReservedItem();
        Item item;
        DateFormat df=null;
        Date date=null;

        try {
            try {
                df = new SimpleDateFormat("yyyy-MM-dd");
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.getString("reserved_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ri.reservationId = jsonObject.getInt("id");
            ri.reserved_date = df.format(date);
            ri.status = jsonObject.getString("status");

            if(!jsonObject.isNull("item")){
                item = Item.getItem(jsonObject.getJSONObject("item"));
                ri.itemId = item.getId();
                ri.itemName = item.getItemName();
                ri.price = item.getPrice();
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

            ReservedItem ri = ReservedItem.getReservedItems(reservedObject);
            if (ri != null) {
                reserved.add(ri);
            }
        }
        return reserved;

    }
}
