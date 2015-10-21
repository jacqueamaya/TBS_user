package citu.teknoybuyandselluser.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        Date date = null;
        SimpleDateFormat df = null;

        try {
            item.id = jsonObject.getInt("id");
            item.itemName = jsonObject.getString("name");
            item.description = jsonObject.getString("description");
            item.category = jsonObject.getJSONObject("category").getString("category_name");
            item.status = jsonObject.getString("status");
            item.purpose = jsonObject.getString("purpose");
            item.price = (float)jsonObject.getDouble("price");
            item.formattedPrice = Utils.formatFloat(item.price);
            item.discountedPrice = (float)jsonObject.optDouble("discounted_price");
            item.stars_required = jsonObject.getInt("stars_required");
            item.picture = jsonObject.getString("picture");

            //try {
                //df = new SimpleDateFormat("yyyy-MM-dd");
                //date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.getString("date_approved"));
                item.dateApproved = Utils.parseDate(jsonObject.optLong("date_approved"));
            /*} catch (ParseException e) {
                e.printStackTrace();
            }*/
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
