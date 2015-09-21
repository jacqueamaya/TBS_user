package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import citu.teknoybuyandselluser.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyItemFragment extends Fragment {

    private static final String TAG = "SellItemDetailsFragment";
    private View view = null;

    private static final String ITEM_NAME = "item_name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String PICTURE = "picture";
    private static final String STARS_REQUIRED = "stars_required";

    private String itemName;
    private String description;
    private float price;
    private String picture;
    private int stars_required;

    private TextView txtItem;
    private TextView txtDescription;
    private TextView txtPrice;

    public static BuyItemFragment newInstance(String itemName, String description, float price, String picture, int stars_required) {
        BuyItemFragment fragment = new BuyItemFragment();
        Bundle bundle = new Bundle();
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

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText(""+price);

        return view;
    }
}
