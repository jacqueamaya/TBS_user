package citu.teknoybuyandselluser;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StarsCollectedActivity extends BaseActivity {

    private static final String TAG = "StarsCollected";

    private TextView txtNumberStars;
    private Button btnClaimAward;


    public void deleteDialogBox(View view){
        AlertDialog.Builder deleteItem= new AlertDialog.Builder(this);
        deleteItem.setTitle("Delete Item");
        deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
        deleteItem.setMessage("Are you sure you want to delete this item?");
        deleteItem.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(StarsCollectedActivity.this,"Item successfully deleted.", Toast.LENGTH_LONG).show();
            }
        });
        deleteItem.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { finish(); }
        });
        AlertDialog alert = deleteItem.create();
        alert.show();
    }
    public void buyDialogBox(View view){

        AlertDialog.Builder buyItem= new AlertDialog.Builder(this);
        buyItem.setTitle("Collected stars");
        buyItem.setIcon(R.drawable.ic_star_black_24dp);
        buyItem.setMessage("You only have 50 stars left.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = buyItem.create();
        alert.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_collected);
        setupUI();

        txtNumberStars = (TextView) findViewById(R.id.txtNumberStars);
        if(getStars() < 2) {
            txtNumberStars.setText(getStars()+" star");
        } else {
            txtNumberStars.setText(getStars()+" stars");
        }

        btnClaimAward = (Button) findViewById(R.id.btnClaimAward);
        btnClaimAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(StarsCollectedActivity.this, AllDonationsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stars_collected, menu);
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

    public int getStars(){
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("stars_collected", 0);
    }
}
