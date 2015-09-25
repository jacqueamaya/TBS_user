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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyItemFragment extends Fragment {

    private static final String TAG = "BuyItemFragment";
    private View view = null;

    private static final String ITEM_NAME = "item_name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String PICTURE = "picture";
    private static final String STARS_REQUIRED = "stars_required";
    private static final String BUYER = "buyer";
    private static final String ID = "item_id";

    private int id;
    private String itemName;
    private String description;
    private float price;
    private String picture;
    private int stars_required;

    private TextView txtItem;
    private TextView txtDescription;
    private TextView txtPrice;

    private ImageView btnBuyItem;

    public static BuyItemFragment newInstance(int id, String itemName, String description, float price, String picture, int stars_required) {
        BuyItemFragment fragment = new BuyItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putString(ITEM_NAME, itemName);
        bundle.putString(DESCRIPTION, description);
        bundle.putFloat(PRICE, price);
        bundle.putString(PICTURE, picture);
        bundle.putInt(STARS_REQUIRED, stars_required);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.d(TAG, "arguments is null");
        } else {
            Log.d(TAG, "Bundle: " + bundle);
            id = bundle.getInt(ID, 0);
            itemName = bundle.getString(ITEM_NAME, "No item name defined");
            description = bundle.getString(DESCRIPTION, "No description");
            price = bundle.getFloat(PRICE, 0);
            picture = bundle.getString(PICTURE, "No picture");
            stars_required = bundle.getInt(STARS_REQUIRED, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buy_item, container, false);

        txtItem = (TextView) view.findViewById(R.id.txtItem);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        btnBuyItem = (ImageView) view.findViewById(R.id.btnBuyItem);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("Php " + price);
        btnBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuy(v);
            }
        });

        return view;
    }

    public void onBuy(View view) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(BUYER, user);
        data.put(ID, "" + id);

        Server.buyItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Buy Item success");
                Toast.makeText(getActivity().getBaseContext(), itemName + " has been reserved.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Buy Item error " + statusCode + " " + responseBody + " " + statusText);
            }
        });
    }
}
