package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Map;

public class Server {

    public static void register (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(MainActivity.ID_NUMBER) ||
                ! data.containsKey(MainActivity.FIRST_NAME) ||
                ! data.containsKey(MainActivity.LAST_NAME) ||
                ! data.containsKey(MainActivity.USERNAME) ||
                ! data.containsKey(MainActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_REGISTER, progressDialog, data, callbacks);
    }

    public static void login (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(LoginActivity.USERNAME) ||
                ! data.containsKey(LoginActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_LOGIN, progressDialog, data, callbacks);
    }

    public static void changePassword (Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(ChangePasswordActivity.USERNAME) ||
                ! data.containsKey(ChangePasswordActivity.OLD_PASSWORD) ||
                ! data.containsKey(ChangePasswordActivity.NEW_PASSWORD) ||
                !data.containsKey(ChangePasswordActivity.CONFIRM_PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.put(Constants.URL_CHANGE_PASSWORD, data, callbacks);
    }

    public static void getUser (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_USER + "/?username=" + username, progress, callbacks);
    }

    public static void sellItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.NAME) ||
                ! data.containsKey(Constants.DESCRIPTION) ||
                ! data.containsKey(Constants.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_SELL_ITEM, progressDialog, data, callbacks);
    }

    public static void editItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.ID) ||
                ! data.containsKey(Constants.NAME) ||
                ! data.containsKey(Constants.DESCRIPTION) ||
                ! data.containsKey(Constants.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_EDIT_ITEM, progressDialog, data, callbacks);
    }

    public static void deleteItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_DELETE_ITEM, progressDialog, data, callbacks);
    }

    public static void buyItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.BUYER) ||
                ! data.containsKey(Constants.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_BUY_ITEM, progressDialog, data, callbacks);
    }

    public static void cancelBuyItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.BUYER) ||
                ! data.containsKey(Constants.ID) ||
                ! data.containsKey(Constants.RESERVATION_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_CANCEL_BUY_ITEM, progressDialog, data, callbacks);
    }

    public static void getItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.BUYER) ||
                ! data.containsKey(Constants.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_GET_ITEM, progressDialog, data, callbacks);
    }

    public static void donateItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.OWNER) ||
                ! data.containsKey(Constants.NAME) ||
                ! data.containsKey(Constants.DESCRIPTION)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_DONATE_ITEM, progressDialog, data, callbacks);
    }

    public static void getNotifications (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_NOTIFICATION + "/?username=" + username, progress, callbacks);
    }

    public static void getItemsToSell (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_ITEMS_TO_SELL + "/?username=" + username, progress, callbacks);
    }

    public static void getPendingItems (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_PENDING_ITEMS + "/?username=" + username, progress, callbacks);
    }

    public static void getAvailableItems (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_AVAILABLE_ITEMS + "/?username=" + username, progress, callbacks);
    }

    public static void getItemsToDonate (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_ITEMS_TO_DONATE + "/?username=" + username, progress, callbacks);
    }

    public static void getAllDonations (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_ALL_DONATIONS + "/?username=" + username, progress, callbacks);
    }

    public static void getAllReservations (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_RESERVED_ITEMS + "/?username=" + username, progress, callbacks);
    }

    public static void upload (String imagePath,ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.upload(Constants.URL_UPLOAD, imagePath, progress, callbacks);
    }

    public static void getCategories (ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_CATEGORIES, progress, callbacks);
    }
}