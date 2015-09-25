package citu.teknoybuyandselluser.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselluser.Ajax;
import citu.teknoybuyandselluser.DashboardActivity;
import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Server;
import citu.teknoybuyandselluser.listAdapters.ItemsListAdapter;
import citu.teknoybuyandselluser.models.Item;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private static final String TAG = "BuyItemsFragment";
    private View view = null;

    private int item_id;
    private String itemName;
    private String description;
    private float price;
    private String picture;
    private int stars_required;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment BuyItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyItemsFragment newInstance(String user) {
        BuyItemsFragment fragment = new BuyItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_buy_items, container, false);

        view = inflater.inflate(R.layout.fragment_buy_items, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");

        Server.getAvailableItems(user, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Item> availableItems = new ArrayList<Item>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
                        txtMessage.setText("No available items to buy.");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        availableItems = Item.allItems(jsonArray);

                        ListView lv = (ListView) view.findViewById(R.id.listViewBuyItems);
                        ItemsListAdapter listAdapter = new ItemsListAdapter(getActivity().getBaseContext(), R.layout.activity_item, availableItems);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = (Item) parent.getItemAtPosition(position);
                                Log.d(TAG, item.getItemName());

                                item_id = item.getId();
                                itemName = item.getItemName();
                                description = item.getDescription();
                                price = item.getPrice();
                                picture = item.getPicture();
                                stars_required = item.getStars_required();

                                Fragment fragment = null;
                                fragment = BuyItemFragment.newInstance(item_id, itemName, description, price, picture, stars_required);
                                ((DashboardActivity) getActivity()).setActionBarTitle(itemName);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.flContent, fragment);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.commit();
                            }
                        });
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
            }
        });

        return view;
    }
}
