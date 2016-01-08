package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ImageInfo;


public class UserProfileActivity extends BaseActivity {
    private static final String TAG = "UserProfileActivity";
    private EditText inputUsername;
    private EditText inputCurrentPassword;
    private EditText inputNewPassword;
    private EditText inputConfirmPassword;
    private ImageInfo mImgInfo;
    //private ImageView mProfilePic;
    private SimpleDraweeView mProfilePic;
    private ImageView mImgPreview;
    private ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;

    private String username;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupUI();
        TextView txtUsername = (TextView) findViewById(R.id.txtUsername);

        //Input fields
        inputUsername = (EditText) findViewById(R.id.inputNewUsername);
        inputCurrentPassword = (EditText) findViewById(R.id.inputCurrentPassword);
        inputNewPassword = (EditText) findViewById(R.id.inputNewPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        //mProfilePic =  (ImageView) findViewById(R.id.profpic);
        mProfilePic = (SimpleDraweeView) findViewById(R.id.profpic);
        mImgPreview = (ImageView) findViewById(R.id.preview);
        Button btnBrowse = (Button) findViewById(R.id.btnBrowse);


        //Progress bar while uploading the picture
        mProgressBar = (ProgressBar) findViewById(R.id.progressUpload);
        mProgressBar.setVisibility(View.INVISIBLE);

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(Constants.FIRST_NAME, null) + " " + prefs.getString(Constants.LAST_NAME, null);
        setTitle(name);
        //Username
        username = prefs.getString(Constants.USERNAME, null);
        txtUsername.setText(username);

        //User's Profile Picture
        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.profpic);
        draweeView.setImageURI(uri);
        String picture = prefs.getString(Constants.PICTURE, null);

        if(null == picture || !(picture.isEmpty()) || picture.equals("")) {
            mProfilePic.setImageResource(Constants.USER_IMAGES[Constants.INDEX_USER_IMAGE]);
        } else {
            Picasso.with(this)
                .load(picture)
                .into(mProfilePic);
        }

        //Browse and select an image
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        mProgressDialog = new ProgressDialog(this);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return true;
    }

    public void onEditProfile(View view) {
        //TODO: edit profile to database
        String newUsername = inputUsername.getText().toString().trim();
        String currentPassword = inputCurrentPassword.getText().toString();
        String newPassword = inputNewPassword.getText().toString();
        String confirmNewPassword = inputConfirmPassword.getText().toString();

        editProfile(newUsername, currentPassword, newPassword, confirmNewPassword);
    }

    public void editProfile(String newUsername, String currentPassword, String newPassword, String confirmNewPassword){
        Map<String, String> data = new HashMap<>();
        data.put(Constants.USERNAME, username);
        data.put(Constants.NEW_USERNAME, newUsername);
        data.put(Constants.OLD_PASSWORD, currentPassword);
        data.put(Constants.NEW_PASSWORD, newPassword);
        data.put(Constants.CONFIRM_PASSWORD, confirmNewPassword);
        if(mImgInfo != null) {
            data.put(Constants.PICTURE, mImgInfo.getLink());
        }

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        Server.editProfile(data, mProgressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                JSONObject json;
                try {
                    json = new JSONObject(responseBody);
                    Toast.makeText(UserProfileActivity.this, json.getString("statusText"), Toast.LENGTH_SHORT).show();
                    if(json.getInt("status") == 200 && mImgInfo != null) {
                        prefs.edit().putString(Constants.PICTURE, mImgInfo.getLink()).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Edit Profile error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(UserProfileActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
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
                            Picasso.with(UserProfileActivity.this)
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
                        Toast.makeText(UserProfileActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        }
    }
}
