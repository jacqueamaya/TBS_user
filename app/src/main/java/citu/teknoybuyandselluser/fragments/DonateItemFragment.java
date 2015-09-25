package citu.teknoybuyandselluser.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.DashboardActivity;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonateItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateItemFragment extends Fragment {
    public static final String OWNER = "owner";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String PICTURE = "picture";
    public static final String STARS_REQUIRED = "stars_required";

    private static final String TAG = "SellItemFragment";

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;
    
    public static DonateItemFragment newInstance(String user) {
        DonateItemFragment fragment = new DonateItemFragment();
        Bundle args = new Bundle();
        args.putString("userId", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_donate_item, container, false);

        ((DashboardActivity) getActivity()).setActionBarTitle("Donate Item");

        txtItem = (EditText) view.findViewById(R.id.txtItem);
        txtDescription = (EditText) view.findViewById(R.id.txtDescription);
        txtPrice = (EditText) view.findViewById(R.id.txtPrice);

        Button btnDonate = (Button) view.findViewById(R.id.btnDonateItem);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDonate(v);
            }
        });
        return view;
    }

    public void onDonate(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = this.getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(OWNER, user);
        data.put(NAME, txtItem.getText().toString());
        data.put(DESCRIPTION, txtDescription.getText().toString());

        Server.donateItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Donate Item success");
                Toast.makeText(getActivity().getBaseContext(), "Donate Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Donate Item error " + responseBody);
                Toast.makeText(getActivity().getBaseContext(), "Donate Item ERROR: " + responseBody, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
