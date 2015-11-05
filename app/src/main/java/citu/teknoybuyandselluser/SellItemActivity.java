package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ImageInfo;

public class SellItemActivity extends BaseActivity {

    private static final String TAG = "SellItemActivity";

    private EditText mTxtItem;
    private EditText mTxtDescription;
    private EditText mTxtPrice;
    private ProgressDialog mProgressDialog;

    private ImageView mImgPreview;
    private ProgressBar mProgressBar;
    ImageInfo mImgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);
        setupUI();

        mTxtItem = (EditText) findViewById(R.id.inputItem);
        mTxtDescription = (EditText) findViewById(R.id.inputDescription);
        mTxtPrice = (EditText) findViewById(R.id.inputPrice);

        mProgressDialog = new ProgressDialog(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressUpload);
        mProgressBar.setVisibility(View.INVISIBLE);

        Button btnBrowse = (Button) findViewById(R.id.btnBrowse);
        mImgPreview =  (ImageView) findViewById(R.id.preview);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Server.upload(picturePath, mProgressBar, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.v(TAG, "successfully posted");
                        Log.v(TAG, responseBody);

                        JSONObject json;
                        try {
                            json = new JSONObject(responseBody);
                            mImgInfo = ImageInfo.getImageInfo(json);
                            Picasso.with(SellItemActivity.this)
                                    .load(mImgInfo.getLink())
                                    .placeholder(R.drawable.thumbsq_24dp)
                                    .into(mImgPreview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(int statusCode, String responseBody, String statusText) {
                        Log.v(TAG, "Request error");
                        Toast.makeText(SellItemActivity.this, "Unable to upload the image", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        }
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_sell_items;
    }

    public void onSell(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        String name = mTxtItem.getText().toString().trim();
        String desc = mTxtDescription.getText().toString().trim();
        String price = mTxtPrice.getText().toString().trim();

        if(!name.equals("")
                && !desc.equals("")
                && !price.equals("")
                && mImgInfo != null) {
            data.put(Constants.OWNER, user);
            data.put(Constants.NAME, name);
            data.put(Constants.DESCRIPTION, desc);
            data.put(Constants.PRICE, price);
            data.put(Constants.IMAGE_URL, mImgInfo.getLink());

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait. . .");

            Server.sellItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    JSONObject json;
                    try {
                        json = new JSONObject(responseBody);
                        String response = json.getString("statusText");
                        if (response.equals("Item created")) {
                            Toast.makeText(SellItemActivity.this, "Item has been created", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SellItemActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Log.d(TAG, "Sell Item error " + statusCode + " " + responseBody + " " + statusText);
                    Toast.makeText(SellItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SellItemActivity.this, "Some input parameters are missing", Toast.LENGTH_SHORT).show();
        }
    }
}
