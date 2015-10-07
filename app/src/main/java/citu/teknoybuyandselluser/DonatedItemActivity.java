package citu.teknoybuyandselluser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DonatedItemActivity extends BaseActivity {

    private static final String TAG = "Get Donated Item";

    private TextView mTxtTitle;
    private TextView mTxtDescription;
    private TextView mTxtNumStars;
    private ImageView mBtnGetItem;
    private ProgressDialog mProgressDialog;

    private int mItemId;
    private int mStarsRequired;
    private String mItemName;
    private String mDescription;
    private String mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_item);
        setupUI();

        Intent intent;
        intent = getIntent();
        mItemId = intent.getIntExtra(Constants.ID, 0);
        mItemName = intent.getStringExtra(Constants.ITEM_NAME);
        mDescription = intent.getStringExtra(Constants.DESCRIPTION);
        mStarsRequired = intent.getIntExtra(Constants.STARS_REQUIRED, 0);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtDescription = (TextView) findViewById(R.id.txtDetails);
        mTxtNumStars = (TextView) findViewById(R.id.txtNumStars);
        mBtnGetItem = (ImageView) findViewById(R.id.btnGetItem);

        mProgressDialog = new ProgressDialog(this);

        mTxtTitle.setText(mItemName);
        mTxtDescription.setText(mDescription);
        mTxtNumStars.setText("" + mStarsRequired);

        mBtnGetItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetItem(v);
            }
        });

        setTitle(mItemName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donated_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_stars_collected;
    }

    public void onGetItem(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Error: " + statusText);
            }
        });
    }
}
