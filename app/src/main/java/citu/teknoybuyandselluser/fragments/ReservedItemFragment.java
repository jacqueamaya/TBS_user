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
 * Use the {@link ReservedItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservedItemFragment extends Fragment {
    private static final String TAG = "ReservedItemFragment";

    private static final String ITEM_NAME = "item_name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String PICTURE = "picture";
    private static final String RESERVED_DATE = "reserved_date";
    private static final String BUYER = "buyer";
    private static final String ITEM_ID = "item_id";
    private static final String RESERVATION_ID = "reservation_id";


    private View view = null;

    private int itemId;
    private int reservationId;
    private String itemName;
    private String description;
    private float price;
    private String picture;
    private String reservedDate;

    private TextView txtItem;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtReservedDate;

    private Button btnCancel;
    public static ReservedItemFragment newInstance(int itemId, int reservationId, String itemName, String description, float price, String picture, String reserved_date) {
        ReservedItemFragment fragment = new ReservedItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_ID, itemId);
        bundle.putInt(RESERVATION_ID, reservationId);
        bundle.putString(ITEM_NAME, itemName);
        bundle.putString(DESCRIPTION, description);
        bundle.putFloat(PRICE, price);
        bundle.putString(PICTURE, picture);
        bundle.putString(RESERVED_DATE, reserved_date);
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
            itemId = bundle.getInt(ITEM_ID, 0);
            reservationId = bundle.getInt(RESERVATION_ID, 0);
            itemName = bundle.getString(ITEM_NAME, "No item name defined");
            description = bundle.getString(DESCRIPTION, "No description");
            price = bundle.getFloat(PRICE, 0);
            picture = bundle.getString(PICTURE, "No picture");
            reservedDate = bundle.getString(RESERVED_DATE, "No reserved date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reserved_item, container, false);
        txtItem = (TextView) view.findViewById(R.id.txtItem);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtReservedDate = (TextView) view.findViewById(R.id.txtReservedDate);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("" + price);
        txtReservedDate.setText(reservedDate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelReservedItem(v);
            }
        });

        return view;
    }

    public void onCancelReservedItem(View v) {
        Map<String, String> data = new HashMap<>();
        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        data.put(BUYER, user);
        data.put(ITEM_ID, "" + itemId);
        data.put(RESERVATION_ID, "" + reservationId);

        Server.cancelBuyItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                Log.d(TAG, "Cancel Item Reservation success");
                Toast.makeText(getActivity().getBaseContext(), "Your reservation for " + itemName + " has been canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.d(TAG, "Cancel Item Reservation error " + statusCode + " " + responseBody + " " + statusText);
            }
        });
    }
}
