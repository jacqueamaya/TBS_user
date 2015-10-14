package citu.teknoybuyandselluser;

/**
 * Created by Jacquelyn on 9/25/2015.
 */
public class Constants {
    public static final String USERNAME = "username";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String STARS_COLLECTED = "stars_collected";

    public static final String BUYER = "buyer";
    public static final String CATEGORY = "category_name";
    public static final String DESCRIPTION = "description";
    public static final String ID = "item_id";
    public static final String ITEM_NAME = "item_name";
    public static final String NAME = "name";
    public static final String OWNER = "owner";
    public static final String PRICE = "price";
    public static final String DISCOUNTED_PRICE = "discounted_price";
    public static final String PICTURE = "picture";
    public static final String RESERVED_DATE = "reserved_date";
    public static final String RESERVATION_ID = "reservation_id";
    public static final String STARS_REQUIRED = "stars_required";
    public static final String IMAGE_URL = "url";
    public static final int INDEX_USER_IMAGE = (int) (Math.random()*10);
    public static final int USER_IMAGES[] =
            {
                    R.drawable.user_1,
                    R.drawable.user_2,
                    R.drawable.user_3,
                    R.drawable.user_4,
                    R.drawable.user_5,
                    R.drawable.user_6,
                    R.drawable.user_7,
                    R.drawable.user_8,
                    R.drawable.user_9,
                    R.drawable.user_10
            };
    public static final String URL = "tbs-admin.herokuapp.com";
    public static final String URL_UPLOAD = "https://api.imgur.com/3/image";

    public static final String URL_REGISTER = "http://"+URL+"/api/register";
    public static final String URL_LOGIN = "http://"+URL+"/api/login";
    public static final String URL_CHANGE_PASSWORD = "http://"+URL+"/api/change_password";
    public static final String URL_SELL_ITEM = "http://"+URL+"/api/sell_item";
    public static final String URL_EDIT_ITEM = "http://"+URL+"/api/edit_item";
    public static final String URL_DELETE_ITEM = "http://"+URL+"/api/delete_item";
    public static final String URL_BUY_ITEM = "http://"+URL+"/api/buy_item";
    public static final String URL_CANCEL_BUY_ITEM = "http://"+URL+"/api/cancel_reserved_item";
    public static final String URL_GET_ITEM = "http://"+URL+"/api/get_donated_item";
    public static final String URL_DONATE_ITEM = "http://"+URL+"/api/donate_item";

    public static final String URL_USER = "http://"+URL+"/api-x/profile";
    public static final String URL_NOTIFICATION = "http://"+URL+"/api-x/user_notifications";
    public static final String URL_ITEMS_TO_SELL = "http://"+URL+"/api-x/items_to_sell";
    public static final String URL_PENDING_ITEMS = "http://"+URL+"/api-x/pending_items";
    public static final String URL_AVAILABLE_ITEMS = "http://"+URL+"/api-x/available_items";
    public static final String URL_ITEMS_TO_DONATE = "http://"+URL+"/api-x/items_to_donate";
    public static final String URL_ALL_DONATIONS = "http://"+URL+"/api-x/all_donations";
    public static final String URL_RESERVED_ITEMS = "http://"+URL+"/api-x/reservation_requests";
    public static final String URL_CATEGORIZE = "http://"+URL+"/api-x/categorize";
    public static final String URL_CATEGORIES = "http://"+URL+"/api-x/categories/";
}
