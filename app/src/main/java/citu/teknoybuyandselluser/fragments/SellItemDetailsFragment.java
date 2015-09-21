package citu.teknoybuyandselluser.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import citu.teknoybuyandselluser.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellItemDetailsFragment extends Fragment {
    private static final String TAG = "SellItemDetailsFragment";
    private View view = null;

    private EditText txtItem;
    private EditText txtDescription;
    private EditText txtPrice;

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

    public static SellItemDetailsFragment newInstance(String itemName, String description, float price, String picture, int stars_required) {
        SellItemDetailsFragment fragment = new SellItemDetailsFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sell_item_details, container, false);

        txtItem = (EditText) view.findViewById(R.id.txtItem);
        txtDescription = (EditText) view.findViewById(R.id.txtDescription);
        txtPrice = (EditText) view.findViewById(R.id.txtPrice);

        txtItem.setText(itemName);
        txtDescription.setText(description);
        txtPrice.setText("" + price);

        return view;
    }

}
