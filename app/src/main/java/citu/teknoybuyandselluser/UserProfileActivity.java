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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.models.ImageInfo;


public class UserProfileActivity extends BaseActivity {
    private static final String TAG = "UserProfileActivity";
    private ImageInfo mImgInfo;
    private SimpleDraweeView mProfilePic;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_user_profile);
        setupUI();
        TextView txtUsername = (TextView) findViewById(R.id.txtUsername);

        //Input fields
        mProfilePic = (SimpleDraweeView) findViewById(R.id.profpic);
        Button btnBrowse = (Button) findViewById(R.id.btnBrowse);

        setTitle(getUserFullName());
        txtUsername.setText(getUserName());

        //User's Profile Picture
        String picture = getUserPicture();

        if(null == picture || (picture.isEmpty()) || picture.equals("")) {
            mProfilePic.setImageResource(Constants.USER_IMAGES[Constants.INDEX_USER_IMAGE]);
        } else {
            mProfilePic.setImageURI(Uri.parse(picture));
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
        EditText inputUsername = (EditText) findViewById(R.id.inputNewUsername);
        EditText inputCurrentPassword = (EditText) findViewById(R.id.inputCurrentPassword);
        EditText inputNewPassword = (EditText) findViewById(R.id.inputNewPassword);
        EditText inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);

        String newUsername = inputUsername.getText().toString().trim();
        String currentPassword = inputCurrentPassword.getText().toString();
        String newPassword = inputNewPassword.getText().toString();
        String confirmNewPassword = inputConfirmPassword.getText().toString();

        editProfile(newUsername, currentPassword, newPassword, confirmNewPassword);
    }

    public void editProfile(String newUsername, String currentPassword, String newPassword, String confirmNewPassword){
        Map<String, String> data = new HashMap<>();
        data.put(Constants.User.USERNAME, getUserName());
        data.put(Constants.User.NEW_USERNAME, newUsername);
        data.put(Constants.User.OLD_PASSWORD, currentPassword);
        data.put(Constants.User.NEW_PASSWORD, newPassword);
        data.put(Constants.User.CONFIRM_PASSWORD, confirmNewPassword);
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
                    if(json.getInt("status") == 200) {
                        Intent intent;
                        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
                        prefs.edit().clear().apply();
                        intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
        //Progress bar while uploading the picture
        ProgressBar progressUpload = (ProgressBar) findViewById(R.id.progressUpload);
        progressUpload.setVisibility(View.INVISIBLE);

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
                Server.upload(picturePath, progressUpload, new Ajax.Callbacks() {
                    @Override
                    public void success(String responseBody) {
                        Log.v(TAG, "successfully posted");
                        Log.v(TAG, responseBody);

                        JSONObject json;
                        try {
                            json = new JSONObject(responseBody);
                            mImgInfo = ImageInfo.getImageInfo(json);
                            mProfilePic.setImageURI(Uri.parse(mImgInfo.getLink()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(int statusCode, String responseBody, String statusText) {
                        Toast.makeText(UserProfileActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        }
    }
}
