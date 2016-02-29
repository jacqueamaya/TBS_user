package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citu.teknoybuyandselluser.models.ImageInfo;

public class ForRentItemActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE_REQUEST_CODE = 1;
    private static final String TAG = "ForRentItemActivity";

    private SimpleDraweeView mImgPreview;
    private ImageInfo mImgInfo;

    private String mItemName;
    private Uri outputFileUri;

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
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "TBS" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressUpload);
        progressBar.setVisibility(View.INVISIBLE);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                String picturePath = "";
                final boolean isCamera;
                isCamera = data == null || MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction());

                Uri selectedImageUri;
                if (isCamera) {
                    picturePath = outputFileUri.getPath();
                } else {
                    selectedImageUri = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImageUri, filePath, null, null, null);
                    if(c != null) {
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        picturePath = c.getString(columnIndex);
                        c.close();
                    }
                }

                if(!picturePath.equals("")) {
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
    }

    public void onForRent(View view) {
        EditText txtItem = (EditText) findViewById(R.id.inputItem);
        EditText txtDescription = (EditText) findViewById(R.id.inputDescription);
        EditText txtPrice = (EditText) findViewById(R.id.inputPrice);
        EditText txtQuantity = (EditText) findViewById(R.id.inputQuantity);
        EditText txtRentDuration = (EditText) findViewById(R.id.inputRentDuration);

        Map<String, String> data = new HashMap<>();
        mItemName = txtItem.getText().toString().trim();
        String desc = txtDescription.getText().toString().trim();
        String price = txtPrice.getText().toString().trim();
        String quantity = txtQuantity.getText().toString().trim();
        String rentDuration = txtRentDuration.getText().toString().trim();

        if (!mItemName.equals("")
                && !desc.equals("")
                && !price.equals("")
                && !quantity.equals("")
                && !rentDuration.equals("")
                && mImgInfo != null) {
            data.put(Constants.Item.OWNER, getUserName());
            data.put(Constants.Item.NAME, mItemName);
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
                        if (json.getInt("status") == 201) {
                            //Toast.makeText(ForRentItemActivity.this, "Item has been created", Toast.LENGTH_SHORT).show();
                            showAlertDialog();
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

    private void showAlertDialog() {
        AlertDialog.Builder forRentItem = new AlertDialog.Builder(this);
        forRentItem.setTitle("Item For Rent Reminder");
        forRentItem.setMessage("Your item, " + mItemName + ", is now pending for TBS Admin approval. " +
                "Please show your item for rent to the TBS Admin within three(3) days. " +
                "Otherwise, your item will expire and will be deleted from Pending list.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        ForRentItemActivity.this.finish();
                    }
                });
        forRentItem.create().show();
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
