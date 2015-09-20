package citu.teknoybuyandselluser;

import android.os.Looper;
import android.util.Log;

import java.util.Map;

import citu.teknoybuyandselluser.fragments.SellItemFragment;

public class Server {

    private static final String URL_REGISTER = "http://192.168.0.12:8000/api/register";
    private static final String URL_LOGIN = "http://192.168.0.12:8000/api/login";
    private static final String URL_CHANGE_PASSWORD = "http://192.168.0.12:8000/api/change_password";
    private static final String URL_USER = "http://192.168.0.12:8000/api-x/profile";
    private static final String URL_SELL_ITEM = "http://192.168.0.12:8000/api/sell_item";
    private static final String URL_NOTIFICATION = "http://192.168.0.12:8000/api-x/notifications";
    private static final String URL_PENDING_ITEMS = "http://192.168.0.12:8000/api-x/pending_items";
    private static final String URL_AVAILABLE_ITEMS = "http://192.168.0.12:8000/api-x/available_items";
    private static final String URL_OWNED_ITEMS = "http://192.168.0.12:8000/api-x/owned_items";
    private static final String URL_DONATED_ITEMS = "http://192.168.0.12:8000/api-x/donated_items";

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
        if ( ! data.containsKey(SellItemFragment.OWNER) ||
                ! data.containsKey(SellItemFragment.NAME) ||
                ! data.containsKey(SellItemFragment.DESCRIPTION) ||
                ! data.containsKey(SellItemFragment.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_SELL_ITEM, data, callbacks);
    }

    public static void getNotifications (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_NOTIFICATION + "/?username=" + username, callbacks);
    }

    public static void getPendingItems (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_PENDING_ITEMS + "/?username=" + username, callbacks);
    }

    public static void getAvailableItems (Ajax.Callbacks callbacks) {
        Ajax.get(URL_AVAILABLE_ITEMS, callbacks);
    }

    public static void getOwnedItems (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_OWNED_ITEMS + "/?username=" + username, callbacks);
    }

    public static void getDonatedItems (Ajax.Callbacks callbacks) {
        Ajax.get(URL_DONATED_ITEMS, callbacks);
    }
}
