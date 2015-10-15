package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class PendingItemActivity extends BaseActivity {

    private static final String TAG = "Pending Item";

    private EditText mTxtItem;
    private EditText mTxtDescription;
    private EditText mTxtPrice;
    private ImageView mImgPreview;
    private ProgressDialog mProgressDialog;

    private int mItemId;
    private int mStarsRequired;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;

    private Button mBtnBrowse;
    private ProgressBar mProgressBar;
    private ImageInfo mImgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);
        mPicture = intent.getStringExtra(Constants.PICTURE);

        mTxtItem = (EditText) findViewById(R.id.txtItem);
        mTxtDescription = (EditText) findViewById(R.id.txtDescription);
        mTxtPrice = (EditText) findViewById(R.id.txtPrice);
        mImgPreview = (ImageView) findViewById(R.id.preview);

        mProgressDialog = new ProgressDialog(this);

        mTxtItem.setText(mItemName);
        mTxtDescription.setText(mDescription);
        if(mPrice == 0.0) {
            mTxtPrice.setText("(To Donate)");
            mTxtPrice.setEnabled(false);
        } else {
            mTxtPrice.setText("" + mPrice);
            mTxtPrice.setEnabled(true);
        }

        Picasso.with(this)
                .load(mPicture)
                .into(mImgPreview);

        setTitle(mItemName);

        mProgressBar = (ProgressBar) findViewById(R.id.progressUpload);
        mProgressBar.setVisibility(View.INVISIBLE);

        mBtnBrowse = (Button) findViewById(R.id.btnBrowse);
        mImgPreview =  (ImageView) findViewById(R.id.preview);
        mBtnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_pending_items;
    }

    public void onEditItem(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        data.put(Constants.OWNER, user);
        data.put(Constants.ID, "" + mItemId);
        data.put(Constants.NAME, mTxtItem.getText().toString());
        data.put(Constants.DESCRIPTION, mTxtDescription.getText().toString());
        data.put(Constants.PRICE, mTxtPrice.getText().toString());
        data.put(Constants.IMAGE_URL, mImgInfo.getLink());

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        Server.editItem(data, mProgressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                JSONObject json = null;
                try {
                    json = new JSONObject(responseBody);
                    int status = json.getInt("status");
                    if(status == 201) {
                        Toast.makeText(PendingItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Edit Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(PendingItemActivity.this, "Edit Item ERROR: " + statusCode + " " + responseBody + " " + statusText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeleteItem(View view){
        AlertDialog.Builder deleteItem= new AlertDialog.Builder(this);
        deleteItem.setTitle("Delete Item");
        deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
        deleteItem.setMessage("Are you sure you want to delete this item?");
        deleteItem.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Map<String, String> data = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
                String user = prefs.getString("username", "");
                Log.d(TAG, "user: " + user);
                data.put(Constants.OWNER, user);
                data.put(Constants.ID, "" + mItemId);

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Please wait. . .");

                Server.deleteItem(data, mProgressDialog, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.d(TAG, "Delete Item success");
                        Toast.makeText(PendingItemActivity.this, "Delete Item success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(int statusCode, String responseBody, String statusText) {
                        Log.d(TAG, "Delete Item error " + statusCode + " " + responseBody + " " + statusText);
                        Toast.makeText(PendingItemActivity.this, "Delete Item ERROR: " + mItemId + statusText, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        deleteItem.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = deleteItem.create();
        alert.show();
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
                Server.upload(picturePath, mProgressBar, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.v(TAG, "successfully posted");
                        Log.v(TAG, responseBody);

                        JSONObject json = null;
                        try {
                            json = new JSONObject(responseBody);
                            ImageInfo image = new ImageInfo();
                            mImgInfo = image.getImageInfo(json);
                            Picasso.with(PendingItemActivity.this)
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
                    }

                });

            }
        }
    }
}
