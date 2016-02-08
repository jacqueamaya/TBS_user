package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.Map;

public class Server {

    private static final String TAG = "Server";

    public static void register (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.User.ID_NUMBER) ||
                ! data.containsKey(Constants.User.FIRST_NAME) ||
                ! data.containsKey(Constants.User.LAST_NAME) ||
                ! data.containsKey(Constants.User.USERNAME) ||
                ! data.containsKey(Constants.User.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_REGISTER, progressDialog, data, callbacks);
    }

    public static void login (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(Constants.User.USERNAME) ||
                ! data.containsKey(Constants.User.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_LOGIN, progressDialog, data, callbacks);
    }

    public static void editProfile (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(Constants.User.USERNAME) ||
                ! data.containsKey(Constants.User.OLD_PASSWORD) ||
                ! data.containsKey(Constants.User.NEW_PASSWORD) ||
                !data.containsKey(Constants.User.CONFIRM_PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_EDIT_PROFILE, progressDialog, data, callbacks);
    }

    public static void getUser (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_USER + "/?username=" + username, progress, callbacks);
    }

    public static void sellItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.OWNER) ||
                ! data.containsKey(Constants.Item.NAME) ||
                ! data.containsKey(Constants.Item.DESCRIPTION) ||
                ! data.containsKey(Constants.Item.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_SELL_ITEM, progressDialog, data, callbacks);
    }

    public static void forRentItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.OWNER) ||
                ! data.containsKey(Constants.Item.NAME) ||
                ! data.containsKey(Constants.Item.DESCRIPTION) ||
                ! data.containsKey(Constants.Item.PRICE) ||
                ! data.containsKey(Constants.Item.QUANTITY)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_FOR_RENT_ITEM, progressDialog, data, callbacks);
    }

    public static void editItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.OWNER) ||
                ! data.containsKey(Constants.Item.ID) ||
                ! data.containsKey(Constants.Item.NAME) ||
                ! data.containsKey(Constants.Item.DESCRIPTION) ||
                ! data.containsKey(Constants.Item.PRICE)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_EDIT_ITEM, progressDialog, data, callbacks);
    }

    public static void deleteItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.OWNER) ||
                ! data.containsKey(Constants.Item.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_DELETE_ITEM, progressDialog, data, callbacks);
    }

    public static void buyItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.BUYER) ||
                ! data.containsKey(Constants.Item.ID) ||
                ! data.containsKey(Constants.Item.QUANTITY)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_BUY_ITEM, progressDialog, data, callbacks);
    }

    public static void rentItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        Log.e(TAG,data.toString());
        if ( ! data.containsKey(Constants.Item.RENTER) ||
                ! data.containsKey(Constants.Item.ID) ||
                ! data.containsKey(Constants.Item.QUANTITY)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_RENT_ITEM, progressDialog, data, callbacks);
    }

    public static void cancelBuyItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.BUYER) ||
                ! data.containsKey(Constants.Item.ID) ||
                ! data.containsKey(Constants.Item.RESERVATION_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_CANCEL_BUY_ITEM, progressDialog, data, callbacks);
    }

    public static void getItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.BUYER) ||
                ! data.containsKey(Constants.Item.ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(Constants.URL_GET_ITEM, progressDialog, data, callbacks);
    }

    public static void donateItem (Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if ( ! data.containsKey(Constants.Item.OWNER) ||
                ! data.containsKey(Constants.Item.NAME) ||
                ! data.containsKey(Constants.Item.DESCRIPTION)) {
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

    public static void getItemsForRent (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_ITEMS_FOR_RENT + "/?username=" + username, progress, callbacks);
    }

    public static void getPendingItems (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_PENDING_ITEMS + "/?username=" + username, progress, callbacks);
    }

    public static void getAvailableItemsToSell (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_AVAILABLE_ITEMS_TO_SELL + "/?username=" + username, progress, callbacks);
    }

    public static void getAvailableItemsForRent (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_AVAILABLE_ITEMS_FOR_RENT + "/?username=" + username, progress, callbacks);
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

    public static void getRentedItems (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(Constants.URL_RENTED_ITEMS + "/?username=" + username, progress, callbacks);
    }

    public static void getReservedItemsOnSale (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_RESERVED_ITEMS_ON_SALE + "/?username=" + username, progress, callbacks);
    }

    public static void getReservedItemsForRent (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_RESERVED_ITEMS_FOR_RENT + "/?username=" + username, progress, callbacks);
    }

    public static void getReservedItemsForDonation (String username, ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_RESERVED_ITEMS_FOR_DONATION + "/?username=" + username, progress, callbacks);
    }

    public static void upload (String imagePath,ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.upload(Constants.URL_UPLOAD, imagePath, progress, callbacks);
    }

    public static void getCategories (ProgressBar progress, Ajax.Callbacks callbacks) {
        Ajax.get(Constants.URL_CATEGORIES, progress, callbacks);
    }
}