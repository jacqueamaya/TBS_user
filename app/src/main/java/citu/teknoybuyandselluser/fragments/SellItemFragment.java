package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import citu.teknoybuyandselluser.DashboardActivity;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellItemFragment extends Fragment {
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
     * @return A new instance of fragment SellItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellItemFragment newInstance(String user) {
        SellItemFragment fragment = new SellItemFragment();
        Bundle args = new Bundle();
        args.putString("userId", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_sell_item, container, false);

        ((DashboardActivity) getActivity()).setActionBarTitle("Sell Item");

        txtItem = (EditText) view.findViewById(R.id.inputItem);
        txtDescription = (EditText) view.findViewById(R.id.inputDescription);
        txtPrice = (EditText) view.findViewById(R.id.inputPrice);

        Button btnSell = (Button) view.findViewById(R.id.btnSellItem);
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
        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(OWNER, user);
        data.put(NAME, txtItem.getText().toString());
        data.put(DESCRIPTION, txtDescription.getText().toString());
        data.put(PRICE, txtPrice.getText().toString());

        Server.sellItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Sell Item success");
                Toast.makeText(getActivity().getBaseContext(), "Sell Item success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Sell Item error " + statusCode + " " + responseBody + " " + statusText);
                Toast.makeText(getActivity().getBaseContext(), "Sell Item ERROR: " + statusCode + " " + responseBody + " " + statusText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
