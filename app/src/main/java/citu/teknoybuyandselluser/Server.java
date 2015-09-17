package citu.teknoybuyandselluser;

import android.os.Looper;
import android.util.Log;

import java.util.Map;

public class Server {

    private static final String URL_REGISTER = "http://192.168.1.100:8000/api/register";
    private static final String URL_LOGIN = "http://192.168.1.100:8000/api/login";
    private static final String URL_CHANGE_PASSWORD = "http://192.168.1.100:8000/api/change_password";

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
        if (  ! data.containsKey(ChangePasswordActivity.USERNAME) ||
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

}
