package citu.teknoybuyandselluser.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Category {

    private static final String TAG = "Category";

    private String categoryName;
    private int categoryId;

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static String[] getAllCategories(JSONArray jsonArray){
        String[] categories = new String[jsonArray.length()];

        for (int i = 0; i < categories.length; i++) {
            try {
                JSONObject category = jsonArray.getJSONObject(i);
                categories[i] = category.getString("category_name");
            } catch (JSONException e) {
                Log.e(TAG, "Exception while getting category name", e);
            }
        }

        return categories;
    }
}
