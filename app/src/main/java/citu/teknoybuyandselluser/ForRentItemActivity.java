package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ImageInfo;

public class ForRentItemActivity extends AppCompatActivity {
    private static final String TAG = "ForRentItemActivity";

    private SimpleDraweeView mImgPreview;
    private ImageInfo mImgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_for_rent_item);

        setupToolbar();

        Button btnBrowse = (Button) findViewById(R.id.btnBrowse);
        mImgPreview = (SimpleDraweeView) findViewById(R.id.preview);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressUpload);
        progressBar.setVisibility(View.INVISIBLE);
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
                Server.upload(picturePath, progressBar, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.v(TAG, "successfully posted");
                        Log.v(TAG, responseBody);

                        JSONObject json;
                        try {
                            json = new JSONObject(responseBody);
                            mImgInfo = ImageInfo.getImageInfo(json);
                            Picasso.with(ForRentItemActivity.this)
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
                        Toast.makeText(ForRentItemActivity.this, "Unable to upload the image", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        }
    }

    public void onForRent(View view) {
        EditText txtItem = (EditText) findViewById(R.id.inputItem);
        EditText txtDescription = (EditText) findViewById(R.id.inputDescription);
        EditText txtPrice = (EditText) findViewById(R.id.inputPrice);
        EditText txtQuantity = (EditText) findViewById(R.id.inputQuantity);
        EditText txtRentDuration = (EditText) findViewById(R.id.inputRentDuration);

        Map<String, String> data = new HashMap<>();
        String name = txtItem.getText().toString().trim();
        String desc = txtDescription.getText().toString().trim();
        String price = txtPrice.getText().toString().trim();
        String quantity = txtQuantity.getText().toString().trim();
        String rentDuration = txtRentDuration.getText().toString().trim();

        if (!name.equals("")
                && !desc.equals("")
                && !price.equals("")
                && !quantity.equals("")
                && !rentDuration.equals("")
                && mImgInfo != null) {
            data.put(Constants.Item.OWNER, getUserName());
            data.put(Constants.Item.NAME, name);
            data.put(Constants.Item.DESCRIPTION, desc);
            data.put(Constants.Item.PRICE, price);
            data.put(Constants.Item.QUANTITY, quantity);
            data.put(Constants.Item.RENT_DURATION, rentDuration);
            data.put(Constants.Item.IMAGE_URL, mImgInfo.getLink());

            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait. . .");

            Server.forRentItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    JSONObject json;
                    try {
                        json = new JSONObject(responseBody);
                        String response = json.getString("statusText");
                        if (response.equals("Item created")) {
                            //Utils.alertInfo(ForRentItemActivity.this, "You have successfully sent your request to put your item for rent. Please go to the TBS office and show your item for the administrator's approval.");
                            //Toast.makeText(ForRentItemActivity.this, "Item has been created", Toast.LENGTH_SHORT).show();
                            //finish();
                            new AlertDialog.Builder(ForRentItemActivity.this)
                                    .setMessage("You have successfully sent your request to put your item for rent. Please go to the TBS office and show your item for the administrator's approval.")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            Toast.makeText(ForRentItemActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Log.d(TAG, "For Rent Item error " + statusCode + " " + responseBody + " " + statusText);
                    Toast.makeText(ForRentItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ForRentItemActivity.this, "Some input parameters are missing", Toast.LENGTH_SHORT).show();
        }
    }

    public String getUserName() {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        return mSharedPreferences.getString(Constants.User.USERNAME, "");
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
