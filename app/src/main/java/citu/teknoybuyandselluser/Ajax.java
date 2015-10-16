package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Ajax {

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_PUT = "PUT";
    private static final String TAG = "Ajax";

    public static final int CONNECT_TIMEOUT = 10000;
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;


    public static void get(String url, final ProgressBar progressBar, final Callbacks callbacks) {
        new AsyncTask<String, Void, HashMap<String, Object>>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    Log.v(TAG,"URL: "+url);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoInput(true);
                    connection.setRequestMethod(REQUEST_METHOD_GET);
                    connection.setUseCaches(false);

                    String responseBody = readStream(connection.getInputStream());
                    Log.v("Ajax","response body: "+responseBody);
                    Log.v("Ajax","response code: "+connection.getResponseCode());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("statusCode", connection.getResponseCode());
                    map.put("responseBody", responseBody);
                    return map;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> map) {
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
                super.onPostExecute(map);
                if (null == map) {
                    callbacks.error(0, null, null);
                } else {
                    int statusCode = (Integer) map.get("statusCode");
                    String responseBody = (String) map.get("responseBody");

                    if (statusCode == HTTP_OK) {
                        callbacks.success(responseBody);
                    } else {
                        callbacks.error(statusCode, responseBody, null);
                    }
                }
            }
        }.execute(url);
    }

    public static void post(String url, final ProgressDialog progressDialog, final Map<String, String> data, final Callbacks callbacks) {
        new AsyncTask<String, Void, HashMap<String, Object>>() {
            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod(REQUEST_METHOD_POST);
                    connection.setUseCaches(false);

                    writeToStream(connection.getOutputStream(), serialize(data));
                    connection.connect();

                    String responseBody = readStream(connection.getInputStream());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("statusCode", connection.getResponseCode());
                    map.put("responseBody", responseBody);
                    return map;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> map) {
                progressDialog.dismiss();
                super.onPostExecute(map);
                if (null == map) {
                    callbacks.error(0, null, null);
                } else {
                    int statusCode = (Integer) map.get("statusCode");
                    String responseBody = (String) map.get("responseBody");

                    if (statusCode == HTTP_OK || statusCode == HTTP_CREATED) {
                        callbacks.success(responseBody);
                    } else {
                        callbacks.error(statusCode, responseBody, null);
                    }
                }
            }
        }.execute(url);
    }

    public static void upload(String url, final String imagePath, final ProgressBar progressBar, final Callbacks callbacks) {
        new AsyncTask<String, Void, HashMap<String, Object>>() {
            @Override
            protected void onPreExecute(){
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    Log.v(TAG,"Connection: "+connection);

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod(REQUEST_METHOD_POST);
                    connection.setRequestProperty("Authorization", "Client-ID " + "6d3df1a4f2dfb26");
                    connection.setUseCaches(false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resize(imagePath).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                    String imageEncode = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imageEncoded,"UTF-8");

                    Log.v(TAG, "DATA: " + imageEncode);
                    writeToStream(connection.getOutputStream(), imageEncode);

                    String responseBody = readStream(connection.getInputStream());
                    Log.v(TAG, "Response: " + responseBody);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("statusCode", connection.getResponseCode());
                    map.put("responseBody", responseBody);
                    return map;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> map) {
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
                super.onPostExecute(map);
                if (null == map) {
                    callbacks.error(0, null, null);
                } else {
                    int statusCode = (Integer) map.get("statusCode");
                    String responseBody = (String) map.get("responseBody");

                    if (statusCode == HTTP_OK || statusCode == HTTP_CREATED) {
                        callbacks.success(responseBody);
                    } else {
                        callbacks.error(statusCode, responseBody, null);
                    }
                }
            }
        }.execute(url);
    }

    public static void put(String url, final Map<String, String> data, final Callbacks callbacks) {
        new AsyncTask<String, Void, HashMap<String, Object>>() {

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod(REQUEST_METHOD_PUT);
                    connection.setUseCaches(false);

                    writeToStream(connection.getOutputStream(), serialize(data));
                    connection.connect();

                    String responseBody = readStream(connection.getInputStream());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("statusCode", connection.getResponseCode());
                    map.put("responseBody", responseBody);
                    return map;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> map) {
                super.onPostExecute(map);
                if (null == map) {
                    callbacks.error(0, null, null);
                } else {
                    int statusCode = (Integer) map.get("statusCode");
                    String responseBody = (String) map.get("responseBody");

                    if (statusCode == HTTP_OK || statusCode == HTTP_CREATED) {
                        callbacks.success(responseBody);
                    } else {
                        callbacks.error(statusCode, responseBody, null);
                    }
                }
            }
        }.execute(url);
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

    public static Bitmap resize(String picturePath){
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(picturePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 320.0f;
        float maxWidth = 240.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(picturePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(picturePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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
