package citu.teknoybuyandselluser.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import citu.teknoybuyandselluser.Constants;

public class Category {

    private static final String TAG = "Category";

    public static String[] getAllCategories(JSONArray jsonArray){
        String[] categories = new String[jsonArray.length() + 1];
        categories[0] = "All";

        for (int i = 1; i < categories.length; i++) {
            try {
                JSONObject category = jsonArray.getJSONObject(i-1);
                categories[i] = category.getString(Constants.CATEGORY);
            } catch (JSONException e) {
                Log.e(TAG, "Exception while getting category name", e);
            }
        }

        return categories;
    }
}
