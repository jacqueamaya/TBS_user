package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DonatedItemActivity extends BaseActivity {

    private static final String TAG = "Get Donated Item";

    private ProgressDialog mProgressDialog;
    private int mItemId;
    private int mStarsRequired;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_item);
        setupUI();

        mPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        String itemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        mStarsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);
        String picture = intent.getStringExtra(Constants.PICTURE);

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) findViewById(R.id.txtDetails);
        TextView txtNumStars = (TextView) findViewById(R.id.txtNumStars);
        ImageView btnGetItem = (ImageView) findViewById(R.id.btnGetItem);
        ImageView imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        mProgressDialog = new ProgressDialog(this);

        txtTitle.setText(itemName);
        txtDescription.setText(description);
        txtNumStars.setText("" + mStarsRequired);

        Picasso.with(this)
                .load(picture)
                .into(imgThumbnail);

        btnGetItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getStars() >= mStarsRequired) {
                    onGetItem(v);
                } else {
                    Toast.makeText(DonatedItemActivity.this, "Not enough stars", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTitle(itemName);
    }

    private int getStars() {
        mPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        return mPreferences.getInt(Constants.STARS_COLLECTED, 0);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_stars_collected;
    }

    public void onGetItem(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + mItemId);

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait. . .");

        Server.getItem(data, mProgressDialog, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String statusText = json.getString("statusText");
                    Log.d(TAG, responseBody);
                    Toast.makeText(DonatedItemActivity.this, statusText, Toast.LENGTH_SHORT).show();
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Error: " + statusText);
                Toast.makeText(DonatedItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
