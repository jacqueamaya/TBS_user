package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ImageInfo;

public class SellItemActivity extends BaseActivity {

    private static final String TAG = "Sell Item";

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;

    private Button btn;
    private ImageView imgpreview;
    private ProgressBar progress;
    ImageInfo imgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);
        setupUI();

        txtItem = (EditText) findViewById(R.id.inputItem);
        txtDescription = (EditText) findViewById(R.id.inputDescription);
        txtPrice = (EditText) findViewById(R.id.inputPrice);

        progress = (ProgressBar) findViewById(R.id.progressUpload);
        progress.setVisibility(View.INVISIBLE);

        btn = (Button) findViewById(R.id.btnBrowse);
        imgpreview =  (ImageView) findViewById(R.id.preview);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }


    private void selectImage() {
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Server.upload(picturePath,progress, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.v(TAG, "successfully posted");
                        Log.v(TAG, responseBody);

                        JSONObject json = null;
                        try {
                            json = new JSONObject(responseBody);
                            ImageInfo image = new ImageInfo();
                            imgInfo = image.getImageInfo(json);
                            Picasso.with(SellItemActivity.this)
                                    .load(imgInfo.getLink())
                                    .placeholder(R.drawable.thumbsq_24dp)
                                    .into(imgpreview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(int statusCode, String responseBody, String statusText) {
                        Log.v(TAG, "Request error");
                    }

                });

            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_sell_items;
    }

    public void onSell(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        Log.v(TAG,imgInfo.getLink());

        data.put(Constants.OWNER, user);
        data.put(Constants.NAME, txtItem.getText().toString());
        data.put(Constants.DESCRIPTION, txtDescription.getText().toString());
        data.put(Constants.PRICE, txtPrice.getText().toString());
        data.put(Constants.IMAGE_URL,imgInfo.getLink());


        Server.sellItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Sell Item success");
                Toast.makeText(SellItemActivity.this, "Sell Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Sell Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(SellItemActivity.this, "Sell Item ERROR: " + statusCode + " " + responseBody + " " + statusText, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
