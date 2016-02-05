package citu.teknoybuyandselluser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import citu.teknoybuyandselluser.services.ExpirationCheckerService;

public class StarsCollectedActivity extends BaseActivity {

    private static final String TAG = "StarsCollected";

    private TextView mTxtNumberStars;
    private ProgressBar mProgressBar;
    private SharedPreferences prefs;
    private String mStrUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_stars_collected);
        setupUI();

        prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        mStrUsername = prefs.getString(Constants.User.USERNAME, "");
        mTxtNumberStars = (TextView) findViewById(R.id.txtNumberStars);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetStars);
        getUserStarsCollected();
        showStars();

        Button mBtnClaimAward = (Button) findViewById(R.id.btnClaimAward);
        mBtnClaimAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(StarsCollectedActivity.this, DonationsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_stars_collected;
    }

    private void showStars() {
        getUser();
        int starsCollected = getUserStarsCollected();
        if (starsCollected < 2) {
            mTxtNumberStars.setText(starsCollected + " star");
        } else {
            mTxtNumberStars.setText(starsCollected + " stars");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showStars();

        Intent service = new Intent(StarsCollectedActivity.this, ExpirationCheckerService.class);
        service.putExtra("username", mStrUsername);
        startService(service);
    }

    public void getUser () {
        Server.getUser(mStrUsername, mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() != 0) {
                        JSONObject json = jsonArray.getJSONObject(0);
                        prefs.edit().putInt(Constants.User.STARS_COLLECTED, json.getInt(Constants.User.STARS_COLLECTED)).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Error: " + statusCode + " " + responseBody);
                Toast.makeText(StarsCollectedActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
