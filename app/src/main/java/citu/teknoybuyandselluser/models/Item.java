package citu.teknoybuyandselluser.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jacquelyn on 9/20/2015.
 */
public class Item {
    private static final String TAG = "Item";
    private String owner;
    private String itemName;
    private String description;
    private String category;
    private String status;
    private String purpose;
    private float price;
    private String picture;
    private int stars_required;

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

    public static Item getItem(JSONObject jsonObject){
        Item item = new Item();
        JSONObject jsonItem;

        try {
            item.itemName = jsonObject.getString("name");
            item.description = jsonObject.getString("description");
            item.category = jsonObject.getJSONObject("category").getString("category_name");
            item.status = jsonObject.getString("status");
            item.purpose = jsonObject.getString("purpose");
            item.price = (float)jsonObject.getDouble("price");
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
