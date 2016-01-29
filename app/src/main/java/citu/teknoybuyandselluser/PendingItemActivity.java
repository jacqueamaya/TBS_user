package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

public class PendingItemActivity extends BaseActivity {

    private static final String TAG = "PendingItemActivity";

    private EditText mTxtItem;
    private EditText mTxtDescription;
    private EditText mTxtPrice;
    private ImageView mImgPreview;
    private ProgressDialog mProgressDialog;

    private int mItemId;
    private float mPrice;
    private String mDescription;
    private String mItemName;
    private String mPicture;
    private String mPurpose;

    private ProgressBar mProgressBar;
    private ImageInfo mImgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_pending_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mPrice = intent.getFloatExtra(Constants.PRICE, 0);
        mPicture = intent.getStringExtra(Constants.PICTURE);
        String formatPrice = intent.getStringExtra(Constants.FORMAT_PRICE);
        mPurpose = intent.getStringExtra("purpose");

        mTxtItem = (EditText) findViewById(R.id.txtItem);
        mTxtDescription = (EditText) findViewById(R.id.txtDescription);
        mTxtPrice = (EditText) findViewById(R.id.txtPrice);
        mImgPreview = (SimpleDraweeView) findViewById(R.id.preview);

        mProgressDialog = new ProgressDialog(this);

        mTxtItem.setText(mItemName);
        mTxtDescription.setText(mDescription);
        if(mPrice == 0.0) {
            mTxtPrice.setText("(To Donate)");
            mTxtPrice.setEnabled(false);
        } else {
            mTxtPrice.setText("" + formatPrice);
            mTxtPrice.setEnabled(true);
        }

        Picasso.with(this)
                .load(mPicture)
                .into(mImgPreview);

        setTitle(mItemName);

        mProgressBar = (ProgressBar) findViewById(R.id.progressUpload);
        mProgressBar.setVisibility(View.INVISIBLE);

        Button btnBrowse = (Button) findViewById(R.id.btnBrowse);
        mImgPreview =  (SimpleDraweeView) findViewById(R.id.preview);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_my_items;
    }

    public void onEditItem(View view) {

        String name = mTxtItem.getText().toString().trim();
        String desc = mTxtDescription.getText().toString().trim();
        String priceStr = mTxtPrice.getText().toString().trim();

        if(mPurpose.equals("Sell")) {

            if(!name.equals("")
                    && !desc.equals("")
                    && (mImgInfo != null || !mPicture.equals(""))
                    && !priceStr.equals("")) {

                float price = Float.parseFloat(mTxtPrice.getText().toString().trim());
                editItem(name, desc, price);
            } else {
                Log.v(TAG,"missing input");
                Toast.makeText(PendingItemActivity.this, "Some input parameters are missing", Toast.LENGTH_SHORT).show();
            }
         }else {
            if(!name.equals("")
                && !desc.equals("")
                && (mImgInfo != null || !mPicture.equals(""))) {
                    editItem(name, desc, 0);
            } else {
                Toast.makeText(PendingItemActivity.this, "Some input parameters are missing", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void editItem(String name, String desc, float price) {
        Map<String, String> data = new HashMap<>();
        String user = getUserName();

        if(name.equals(mItemName) && desc.equals(mDescription) && price == mPrice && mImgInfo == null) {
            Toast.makeText(PendingItemActivity.this, "No changes have been made", Toast.LENGTH_SHORT).show();
        } else {
            data.put(Constants.OWNER, user);
            data.put(Constants.ID, ""+mItemId);

            data.put(Constants.NAME, name);
            data.put(Constants.DESCRIPTION, desc);
            data.put(Constants.PRICE, "" + price);
            if(mImgInfo != null) {
                data.put(Constants.IMAGE_URL, mImgInfo.getLink());
            }

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait. . .");

            Server.editItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    JSONObject json;
                    try {
                        json = new JSONObject(responseBody);
                        Toast.makeText(PendingItemActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Log.d(TAG, "Edit Item error " + statusCode + " " + responseBody + " " + statusText);
                    Toast.makeText(PendingItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void onDeleteItem(View view){
        final AlertDialog.Builder deleteItem= new AlertDialog.Builder(this);
        deleteItem.setTitle("Delete Item");
        deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
        deleteItem.setMessage("Are you sure you want to delete this item?");
        deleteItem.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteItem();
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

    public void deleteItem () {
        Map<String, String> data = new HashMap<>();
        String userName = getUserName();
        data.put(Constants.OWNER, userName);
        data.put(Constants.ID, "" + mItemId);

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        Server.deleteItem(data, mProgressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Delete Item success");
                Toast.makeText(PendingItemActivity.this, "Delete Item Success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Delete Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(PendingItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PendingItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }
    }
}
