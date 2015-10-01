package citu.teknoybuyandselluser;

import java.util.Map;

public class Server {
    private static final String URL = "tbs-admin.herokuapp.com";
    private static final String URL_REGISTER = "http://"+URL+"/api/register";
    private static final String URL_LOGIN = "http://"+URL+"/api/login";
    private static final String URL_CHANGE_PASSWORD = "http://"+URL+"/api/change_password";
    private static final String URL_USER = "http://"+URL+"/api-x/profile";
    private static final String URL_SELL_ITEM = "http://"+URL+"/api/sell_item";
    private static final String URL_EDIT_ITEM = "http://"+URL+"/api/edit_item";
    private static final String URL_DELETE_ITEM = "http://"+URL+"/api/delete_item";
    private static final String URL_BUY_ITEM = "http://"+URL+"/api/buy_item";
    private static final String URL_CANCEL_BUY_ITEM = "http://"+URL+"/api/cancel_reserved_item";
    private static final String URL_GET_ITEM = "http://"+URL+"/api/get_donated_item";
    private static final String URL_DONATE_ITEM = "http://"+URL+"/api/donate_item";
    private static final String URL_NOTIFICATION = "http://"+URL+"/api-x/user_notifications";
    private static final String URL_ITEMS_TO_SELL = "http://"+URL+"/api-x/items_to_sell";
    private static final String URL_PENDING_ITEMS = "http://"+URL+"/api-x/pending_items";
    private static final String URL_AVAILABLE_ITEMS = "http://"+URL+"/api-x/available_items";
    private static final String URL_ITEMS_TO_DONATE = "http://"+URL+"/api-x/items_to_donate";
    private static final String URL_ALL_DONATIONS = "http://"+URL+"/api-x/all_donations";
    private static final String URL_RESERVED_ITEMS = "http://"+URL+"/api-x/reservation_requests";
    private static final String URL_UPLOAD = "https://api.imgur.com/3/image";
    private static final String URL_CATEGORIZE = "http://"+URL+"/api-x/categorize";

    public static void register (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(MainActivity.ID_NUMBER) ||
                ! data.containsKey(MainActivity.FIRST_NAME) ||
                ! data.containsKey(MainActivity.LAST_NAME) ||
                ! data.containsKey(MainActivity.USERNAME) ||
                ! data.containsKey(MainActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_REGISTER, data, callbacks);
    }

    public static void login (Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(LoginActivity.USERNAME) ||
                ! data.containsKey(LoginActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_LOGIN, data, callbacks);
    }

    public static void changePassword (Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(ChangePasswordActivity.USERNAME) ||
                ! data.containsKey(ChangePasswordActivity.OLD_PASSWORD) ||
                ! data.containsKey(ChangePasswordActivity.NEW_PASSWORD) ||
                !data.containsKey(ChangePasswordActivity.CONFIRM_PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.put(URL_CHANGE_PASSWORD, data, callbacks);
    }

    public static void getUser (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_USER + "/?username=" + username, callbacks);
    }

    public static void sellItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.NAME) ||
                ! data.containsKey(Constants.DESCRIPTION) ||
                ! data.containsKey(Constants.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_SELL_ITEM, data, callbacks);
    }

    public static void editItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.ID) ||
                ! data.containsKey(Constants.NAME) ||
                ! data.containsKey(Constants.DESCRIPTION) ||
                ! data.containsKey(Constants.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_EDIT_ITEM, data, callbacks);
    }

    public static void deleteItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_DELETE_ITEM, data, callbacks);
    }

    public static void buyItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.BUYER) ||
                ! data.containsKey(Constants.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_BUY_ITEM, data, callbacks);
    }

    public static void cancelBuyItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.BUYER) ||
                ! data.containsKey(Constants.ID) ||
                ! data.containsKey(Constants.RESERVATION_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_CANCEL_BUY_ITEM, data, callbacks);
    }

    public static void getItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.BUYER) ||
                ! data.containsKey(Constants.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_GET_ITEM, data, callbacks);
    }

    public static void donateItem (Map<String, String> data, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.NAME) ||
                ! data.containsKey(Constants.DESCRIPTION)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_DONATE_ITEM, data, callbacks);
    }

    public static void getNotifications (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_NOTIFICATION + "/?username=" + username, callbacks);
    }

    public static void getItemsToSell (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_ITEMS_TO_SELL + "/?username=" + username, callbacks);
    }

    public static void getPendingItems (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_PENDING_ITEMS + "/?username=" + username, callbacks);
    }

    public static void getAvailableItems (String username, Ajax.Callbacks callbacks) {
        Ajax.get(URL_AVAILABLE_ITEMS + "/?username=" + username, callbacks);
    }

    public static void getItemsToDonate (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_ITEMS_TO_DONATE + "/?username=" + username, callbacks);
    }

    public static void getAllDonations (String username, Ajax.Callbacks callbacks) {
        Ajax.get(URL_ALL_DONATIONS + "/?username=" + username, callbacks);
    }

    public static void getAllReservations (String username, Ajax.Callbacks callbacks) {
        Ajax.get(URL_RESERVED_ITEMS + "/?username=" + username, callbacks);
    }

    public static void upload (String data, Ajax.Callbacks callbacks) {
        Ajax.upload(URL_UPLOAD, data, callbacks);
    }

    public static void categorize (String category, Ajax.Callbacks callbacks) {
        Ajax.get(URL_CATEGORIZE + "/?category=" + category, callbacks);
    }
}