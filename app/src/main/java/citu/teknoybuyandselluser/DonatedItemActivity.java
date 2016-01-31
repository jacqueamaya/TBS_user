package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DonatedItemActivity extends AppCompatActivity {

    private Intent intent;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_donated_item);

        setupToolbar();

        mSharedPreferences = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);

        intent = getIntent();
        String itemName = intent.getStringExtra(Constants.ITEM_NAME);
        String description = intent.getStringExtra(Constants.DESCRIPTION);
        String picture = intent.getStringExtra(Constants.PICTURE);

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) findViewById(R.id.txtDetails);
        TextView txtNumStars = (TextView) findViewById(R.id.txtNumStars);
        ImageView btnGetItem = (ImageView) findViewById(R.id.btnGetItem);
        ImageView imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        mProgressDialog = new ProgressDialog(this);

        txtTitle.setText(itemName);
        txtDescription.setText(description);
        txtNumStars.setText("" + getStarsRequired());

        Picasso.with(this)
                .load(picture)
                .into(imgThumbnail);

        btnGetItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getUserStarsCollected() >= getStarsRequired()) {
                    onGetItem(v);
                } else {
                    Toast.makeText(DonatedItemActivity.this, "Not enough stars", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTitle(itemName);
    }

    private int getStarsRequired() {
        return (intent.getIntExtra(Constants.STARS_REQUIRED, 0));
    }

    public void onGetItem(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity);

        int quantity = Integer.parseInt(txtQuantity.getText().toString());
        int itemId = intent.getIntExtra(Constants.ID, 0);
        int itemQuantity = intent.getIntExtra(Constants.QUANTITY, 1);

        data.put(Constants.BUYER, user);
        data.put(Constants.ID, "" + itemId);

        if(quantity <= itemQuantity && quantity > 0) {
            data.put(Constants.QUANTITY, quantity + "");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait. . .");

            Server.getItem(data, mProgressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String statusText = json.getString("statusText");
                        Toast.makeText(DonatedItemActivity.this, statusText, Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(int statusCode, String responseBody, String statusText) {
                    Toast.makeText(DonatedItemActivity.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DonatedItemActivity.this, "Invalid quantity", Toast.LENGTH_SHORT).show();
        }
    }

    public String getUserName() {
        return mSharedPreferences.getString(Constants.User.USERNAME, "");
    }

    private int getUserStarsCollected() {
        return mSharedPreferences.getInt(Constants.User.STARS_COLLECTED, 0);
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
