package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
    public static final String CATEGORY = "category";
    public static final String STATUS = "status";
    public static final String PURPOSE = "purpose";
    public static final String PRICE = "price";
    public static final String PICTURE = "picture";
    public static final String STARS_REQUIRED = "stars_required";

    private static final String TAG = "SellItemFragment";

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment DonateItemFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_donate_item, container, false);
        View view = null;
        view = inflater.inflate(R.layout.fragment_donate_item, container, false);

        txtItem = (EditText) view.findViewById(R.id.txtItem);
        txtDescription = (EditText) view.findViewById(R.id.txtDescription);
        txtPrice = (EditText) view.findViewById(R.id.txtPrice);

        Button btnSell = (Button) view.findViewById(R.id.btnDonateItem);
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSell(v);
            }
        });
        return view;
    }

    public void onSell(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = this.getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "No name defined");
        txtItem.setText(user);
        data.put(OWNER, user);
        data.put(NAME, txtItem.getText().toString());
        data.put(DESCRIPTION, txtDescription.getText().toString());
        data.put(STATUS, "Pending");
        data.put(PURPOSE, "Sell");
        data.put(PRICE, txtPrice.getText().toString());

        Server.sellItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Sell Item success");
                Toast.makeText(getActivity().getBaseContext(), "Sell Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "LOGIN error " + responseBody);
                Toast.makeText(getActivity().getBaseContext(), "Sell Item ERROR: " + responseBody, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
