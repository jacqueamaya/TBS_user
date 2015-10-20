package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StarsCollectedActivity extends BaseActivity {

    private static final String TAG = "StarsCollected";

    private TextView mTxtNumberStars;
    private Button mBtnClaimAward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_collected);
        setupUI();

        mTxtNumberStars = (TextView) findViewById(R.id.txtNumberStars);
        if (getStars() < 2) {
            mTxtNumberStars.setText(getStars() + " star");
        } else {
            mTxtNumberStars.setText(getStars() + " stars");
        }

        mBtnClaimAward = (Button) findViewById(R.id.btnClaimAward);
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

    private int getStars() {
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(Constants.STARS_COLLECTED, 0);
    }
}
