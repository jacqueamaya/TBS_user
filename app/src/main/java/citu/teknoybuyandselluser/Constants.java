package citu.teknoybuyandselluser;

/**
 ** Created by Jacquelyn on 9/25/2015.
 */
public class Constants {
    public static final String MY_PREFS_NAME = "UserPreferences";


    public static final class User {
        public static final String ID_NUMBER = "id_number";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String STARS_COLLECTED = "stars_collected";
        public static final String STUDENT = "student";

        public static final String NEW_USERNAME = "new_username";
        public static final String OLD_PASSWORD = "old_password";
        public static final String NEW_PASSWORD = "new_password";
        public static final String CONFIRM_PASSWORD = "confirm_password";
        public static final String PICTURE = "picture";

    }

    public static final class Item {
        public static final String OWNER_USER_USERNAME = "owner.user.username";
        public static final String BUYER_USER_USERNAME = "buyer.user.username";
        public static final String ITEM_OWNER_USER_USERNAME = "item.owner.user.username";
        public static final String BUYER = "buyer";
        public static final String RENTER = "renter";
        public static final String CATEGORY = "category_name";
        public static final String DESCRIPTION = "description";
        public static final String ID = "item_id";
        public static final String ITEM_CODE = "item_code";
        public static final String ITEM_NAME = "item_name";
        public static final String NAME = "name";
        public static final String OWNER = "owner";
        public static final String PAYMENT = "payment";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String DISCOUNTED_PRICE = "discounted_price";
        public static final String PENALTY = "penalty";
        public static final String PICTURE = "picture";
        public static final String PURPOSE = "purpose";
        public static final String RENT_DATE = "rent_date";
        public static final String RENT_EXPIRATION = "rent_expiration";
        public static final String RESERVED_DATE = "reserved_date";
        public static final String RESERVATION_ID = "reservation_id";
        public static final String STARS_REQUIRED = "stars_required";
        public static final String STARS_TO_USE = "stars_to_use";
        public static final String STATUS = "status";
        public static final String IMAGE_URL = "url";
        public static final String FORMAT_PRICE = "formatPrice";
    }

    public static final String BUYER = "buyer";
    public static final String RENTER = "renter";
    public static final String CATEGORY = "category_name";
    public static final String DESCRIPTION = "description";
    public static final String RENT_DURATION = "rentDuration";
    public static final String ID = "item_id";
    public static final String ITEM_CODE = "item_code";
    public static final String ITEM_NAME = "item_name";
    public static final String NAME = "name";
    public static final String OWNER = "owner";
    public static final String PAYMENT = "payment";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String DISCOUNTED_PRICE = "discounted_price";
    public static final String PENALTY = "penalty";
    public static final String PICTURE = "picture";
    public static final String PURPOSE = "purpose";
    public static final String RENT_DATE = "rent_date";
    public static final String RENT_EXPIRATION = "rent_expiration";
    public static final String RESERVED_DATE = "reserved_date";
    public static final String RESERVATION_ID = "reservation_id";
    public static final String STARS_REQUIRED = "stars_required";
    public static final String STARS_TO_USE = "stars_to_use";
    public static final String STATUS = "status";
    public static final String IMAGE_URL = "url";
    public static final String FORMAT_PRICE = "formatPrice";

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
    public static final String URL_EDIT_PROFILE = "http://"+URL+"/api/edit_profile";
    public static final String URL_SELL_ITEM = "http://"+URL+"/api/sell_item";
    public static final String URL_FOR_RENT_ITEM = "http://"+URL+"/api/for_rent_item";
    public static final String URL_EDIT_ITEM = "http://"+URL+"/api/edit_item";
    public static final String URL_DELETE_ITEM = "http://"+URL+"/api/delete_item";
    public static final String URL_BUY_ITEM = "http://"+URL+"/api/buy_item";
    public static final String URL_RENT_ITEM = "http://"+URL+"/api/rent_item";
    public static final String URL_CANCEL_BUY_ITEM = "http://"+URL+"/api/cancel_reserved_item";
    public static final String URL_GET_ITEM = "http://"+URL+"/api/get_donated_item";
    public static final String URL_DONATE_ITEM = "http://"+URL+"/api/donate_item";

    public static final String URL_USER = "http://"+URL+"/api-x/profile";
    public static final String URL_NOTIFICATION = "http://"+URL+"/api-x/user_notifications";
    public static final String URL_ITEMS_TO_SELL = "http://"+URL+"/api-x/items_to_sell";
    public static final String URL_ITEMS_FOR_RENT = "http://"+URL+"/api-x/items_for_rent";
    public static final String URL_PENDING_ITEMS = "http://"+URL+"/api-x/pending_items";
    public static final String URL_AVAILABLE_ITEMS_TO_SELL = "http://"+URL+"/api-x/available_items_to_sell";
    public static final String URL_AVAILABLE_ITEMS_FOR_RENT = "http://"+URL+"/api-x/available_items_for_rent";
    public static final String URL_ITEMS_TO_DONATE = "http://"+URL+"/api-x/items_to_donate";
    public static final String URL_ALL_DONATIONS = "http://"+URL+"/api-x/all_donations";
    public static final String URL_RENTED_ITEMS = "http://"+URL+"/api-x/rented_items";
    public static final String URL_RESERVED_ITEMS = "http://"+URL+"/api-x/reservation_requests";
    public static final String URL_RESERVED_ITEMS_ON_SALE = "http://"+URL+"/api-x/reserved_items_on_sale";
    public static final String URL_RESERVED_ITEMS_FOR_RENT = "http://"+URL+"/api-x/reserved_items_for_rent";
    public static final String URL_RESERVED_ITEMS_FOR_DONATION = "http://"+URL+"/api-x/reserved_items_for_donation";
    public static final String URL_CATEGORIES = "http://"+URL+"/api-x/categories/";

    public static final class UrlUser {
        public static final String BASE_URL = "http://tbs-admin.herokuapp.com/";
        public static final String IMG_UPLOAD = "https://api.imgur.com/3/image";

        public static final String REGISTER = "api/register";
        public static final String LOGIN = "api/login";
        public static final String EDIT_PROFILE = "api/edit_profile";
        public static final String SELL_ITEM = "api/sell_item";
        public static final String BUY_ITEM = "api/buy_item";
        public static final String FOR_RENT_ITEM = "api/for_rent_item";
        public static final String RENT_ITEM = "hapi/rent_item";
        public static final String DONATE_ITEM = "api/donate_item";
        public static final String GET_DONATED_ITEM = "api/get_donated_item";
        public static final String EDIT_ITEM = "api/edit_item";
        public static final String DELETE_ITEM = "api/delete_item";
        public static final String CANCEL_RESERVED_ITEM = "api/cancel_reserved_item";
        public static final String CHECK_EXPIRATION = "api/admin_check_expiration";

        public static final String CATEGORIES = "api-x/categories/";
        public static final String PROFILE = "api-x/profile/";
        public static final String NOTIFICATION = "api-x/user_notifications/";
        public static final String PENDING_ITEMS = "api-x/pending_items/";
        public static final String ITEMS_TO_SELL = "api-x/items_to_sell/";
        public static final String ITEMS_FOR_RENT = "api-x/items_for_rent/";
        public static final String ITEMS_TO_DONATE = "api-x/items_to_donate/";
        public static final String AVAILABLE_ITEMS_TO_SELL = "api-x/available_items_to_sell/";
        public static final String AVAILABLE_ITEMS_FOR_RENT = "api-x/available_items_for_rent/";
        public static final String ALL_DONATIONS = "api-x/all_donations/";
        public static final String RENTED_ITEMS = "api-x/rented_items/";
        public static final String RESERVED_ITEMS_ON_SALE = "api-x/reserved_items_on_sale/";
        public static final String RESERVED_ITEMS_FOR_RENT = "api-x/reserved_items_for_rent/";
        public static final String RESERVED_ITEMS_FOR_DONATION = "api-x/reserved_items_for_donation/";
    }

    public static final String RESULT = "result";
    public static final String RESPONSE = "response";

    public static final class Sort {
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String PRICE = "price";
    }
}
