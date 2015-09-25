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
 * Use the {@link DonateItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateItemDetailsFragment extends Fragment {
    private static final String TAG = "DonatedItemFragment";
    private View view = null;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtNumStars;

    private static final String ITEM_NAME = "item_name";
    private static final String DESCRIPTION = "description";
    private static final String PICTURE = "picture";
    private static final String STARS_REQUIRED = "stars_required";

    private String itemName;
    private String description;
    private String picture;
    private int stars_required;

    public static DonateItemDetailsFragment newInstance(String itemName, String description, String picture, int stars_required) {
        DonateItemDetailsFragment fragment = new DonateItemDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_NAME, itemName);
        bundle.putString(DESCRIPTION, description);
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
            stars_required = bundle.getInt(STARS_REQUIRED, 0);
            picture = bundle.getString(PICTURE, "https://www.google.com.ph");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_donate_item_details, container, false);

        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtDescription = (TextView) view.findViewById(R.id.txtDetails);
        txtNumStars = (TextView) view.findViewById(R.id.txtNumStars);

        txtTitle.setText(itemName);
        txtDescription.setText(description);
        txtNumStars.setText("" + stars_required);

        return view;
    }
}
