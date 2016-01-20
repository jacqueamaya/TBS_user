package citu.teknoybuyandselluser;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ExpirationCheckerService extends IntentService {

    public static final String TAG = "ExpirationCheckerSvc";
    public static final String ACTION = ExpirationCheckerService.class.getCanonicalName();

    public ExpirationCheckerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String username = intent.getStringExtra("username");
        try {
            URL url = new URL("http://tbs-admin.herokuapp.com/api/admin_check_expiration");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            writer.write("username=" + URLEncoder.encode(username, "UTF-8"));

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Successful");
            } else {
                Log.e(TAG, "Failed : " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
