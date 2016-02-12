package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DonateItemActivity extends AppCompatActivity {

    private SimpleDraweeView mImgPreview;
    private ProgressDialog mProgressDialog;

    private ImageInfo mImgInfo;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_donate_item);

        setupToolbar();

        mProgressDialog = new ProgressDialog(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressUpload);
        mProgressBar.setVisibility(View.INVISIBLE);

        Button mBtnBrowse = (Button) findViewById(R.id.btnBrowse);
        mImgPreview =  (SimpleDraweeView) findViewById(R.id.preview);
        mBtnBrowse.setOnClickListener(new View.OnClickListener() {
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

                        JSONObject json;
                        try {
                            json = new JSONObject(responseBody);
                            mImgInfo = ImageInfo.getImageInfo(json);
                            Picasso.with(DonateItemActivity.this)
                                    .load(mImgInfo.getLink())
                                    .placeholder(R.drawable.thumbsq_24dp)
                                    .into(mImgPreview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(int statusCode, String responseBody, String statusText) {
                        Toast.makeText(DonateItemActivity.this, "Unable to upload the image", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        }
    }

    public void onDonate(View view) {
        Map<String, String> data = new HashMap<>();

        EditText txtItem = (EditText) findViewById(R.id.inputItem);
        EditText txtDescription = (EditText) findViewById(R.id.inputDescription);
        EditText txtQuantity = (EditText) findViewById(R.id.inputQuantity);

        String name = txtItem.getText().toString().trim();
        String desc = txtDescription.getText().toString().trim();
        String quantity = txtQuantity.getText().toString().trim();

        if(!name.equals("")
                && !desc.equals("")
                && !quantity.equals("")
                && mImgInfo != null) {
            data.put(Constants.Item.OWNER, getUserName());
            data.put(Constants.Item.NAME, name);
            data.put(Constants.Item.DESCRIPTION, desc);
            data.put(Constants.Item.QUANTITY, quantity);
            data.put(Constants.Item.IMAGE_URL, mImgInfo.getLink());

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait. . .");

            Server.donateItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    JSONObject json;
                    try {
                        json = new JSONObject(responseBody);
                        String response = json.getString("statusText");
                        if (response.equals("Item created")) {
                            Toast.makeText(DonateItemActivity.this, "Item has been created", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DonateItemActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        Toast.makeText(DonateItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(DonateItemActivity.this, "Some input parameters are missing", Toast.LENGTH_SHORT).show();
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
