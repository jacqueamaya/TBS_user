package citu.teknoybuyandselluser;

import android.util.Log;

import java.util.Map;

public class Server {

    private static final String URL_REGISTER = "http://10.0.3.2:8000/api/register";
    private static final String URL_LOGIN = "http://10.0.3.2:8000/api/login";

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
        if (  ! data.containsKey(MainActivity.USERNAME) ||
                ! data.containsKey(MainActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_LOGIN, data, callbacks);
    }

}
