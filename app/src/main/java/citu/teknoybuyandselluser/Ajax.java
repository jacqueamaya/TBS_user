package citu.teknoybuyandselluser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public final class Ajax {

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_PUT = "PUT";
    public static final String REQUEST_METHOD_DELETE = "DELETE";

    public static final int CONNECT_TIMEOUT = 10000;
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;

    public static void get(String url, final Map<String, String> data, final Callbacks callbacks) {

    }

    public static void post(String url, final Map<String, String> data, final Callbacks callbacks) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);

                    writeToStream(connection.getOutputStream(), serialize(data));

                    switch (connection.getResponseCode()) {
                        case HTTP_OK:
                        case HTTP_CREATED:
                            String responseBody = readStream(connection.getInputStream());
                            callbacks.success(responseBody);
                            Log.v("Ajax","successs   "+ connection.getResponseCode());

                        default:
                            int statusCode = connection.getResponseCode();
                            callbacks.error(statusCode, "ERROR " + statusCode, connection.getResponseMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }
        }.execute(url);
    }

    public static void put(String url, Map<String, String> data, Callbacks callbacks) {

    }

    public static void delete(String url, Map<String, String> data, Callbacks callbacks) {

    }

    public static String serialize (Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (String key : data.keySet()) {
            if (first) {
                builder.append(key).append('=').append(data.get(key));
                first = false;
            } else {
                builder.append('&').append(key).append('=').append(data.get(key));
            }
        }

        return builder.toString();
    }

    public static String readStream (InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            is.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void writeToStream (OutputStream os, String whatToWrite) {
        PrintWriter writer = new PrintWriter(os);
        try {
            writer.write(whatToWrite);
        } finally {
            writer.flush();
            writer.close();
        }
    }

    public interface Callbacks {
        void success(String responseBody);
        void error(int statusCode, String responseBody, String statusText);
    }

}
