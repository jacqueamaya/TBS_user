package citu.teknoybuyandselluser.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.Utils;

/**
 * Created by Jacquelyn on 9/20/2015.
 */
public class Item {
    private static final String TAG = "Item";

    private int id;
    private String owner;
    private String itemName;
    private String description;
    private String category;
    private String status;
    private String purpose;
    private String formattedPrice;
    private float price;
    private float discountedPrice;
    private String picture;
    private int stars_required;

    private String dateApproved;

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getPurpose() {
        return purpose;
    }

    public float getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }

    public int getStars_required() {
        return stars_required;
    }

    public float getDiscountedPrice() {
        return discountedPrice;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public static Item getItem(JSONObject jsonObject){
        Item item = new Item();

        try {
            item.id = jsonObject.getInt("id");
            item.itemName = jsonObject.getString(Constants.NAME);
            item.description = jsonObject.getString(Constants.DESCRIPTION);
            item.category = jsonObject.getJSONObject("category").getString(Constants.CATEGORY);
            item.status = jsonObject.getString("status");
            item.purpose = jsonObject.getString("purpose");
            item.price = (float)jsonObject.getDouble(Constants.PRICE);
            item.formattedPrice = Utils.formatFloat(item.price);
            item.discountedPrice = (float)jsonObject.optDouble(Constants.DISCOUNTED_PRICE);
            item.stars_required = jsonObject.getInt(Constants.STARS_REQUIRED);
            item.picture = jsonObject.getString(Constants.PICTURE);
            item.dateApproved = Utils.parseDate(jsonObject.optLong("date_approved"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static ArrayList<Item> allItems(JSONArray jsonArray){
        ArrayList<Item> items = new ArrayList<Item>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonItem = null;
            try {
                jsonItem = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Item item = Item.getItem(jsonItem);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
