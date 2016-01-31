package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models.Item;

/**
 ** Created by Jacquelyn on 9/20/2015.
 */

public class ItemsListAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private int id;

    private ArrayList<Item> mOriginalValues;
    private ArrayList<Item> mDisplayedValues;

    public ItemsListAdapter(Context context, int textViewResourceId, ArrayList<Item> list) {
        mContext = context;
        id = textViewResourceId;
        mOriginalValues = list;
        mDisplayedValues = list;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        Item item  = mDisplayedValues.get(position);
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);
        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Picasso.with(mContext)
                .load(item.getPicture())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        String message;
        message = "<b>" + item.getName() + "</b>";
        text.setText(Html.fromHtml(message));

        return mView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Item> FilteredArrList = new ArrayList<>();
                String searchByCategory[] = constraint.toString().split(",");

                if (getCount() != 0) {

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    if (constraint == "" || constraint.length() == 0 || searchByCategory.length == 0) {
                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String name = mOriginalValues.get(i).getName();
                            String category = mOriginalValues.get(i).getCategory().getCategory_name();
                            if (searchByCategory.length == 2) {
                                if (category.equals(searchByCategory[1]) && name.toLowerCase().contains(searchByCategory[0].toLowerCase())) {
                                    FilteredArrList.add(mOriginalValues.get(i));
                                }
                            } else {
                                if (category.equals(constraint.toString()) || name.toLowerCase().contains(searchByCategory[0].toLowerCase())) {
                                    FilteredArrList.add(mOriginalValues.get(i));
                                }
                            }
                            // set the Filtered result to return
                            results.count = FilteredArrList.size();
                            results.values = FilteredArrList;
                        }
                    }
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<Item> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                Comparator<Item> priceComparator = new Comparator<Item>() {
                    public int compare(Item obj1, Item obj2) {
                        return obj1.getPrice() < obj2.getPrice() ? -1 : obj1.getPrice() > obj2.getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, priceComparator);
                break;
            case "name":
                Comparator<Item> nameComparator = new Comparator<Item>() {
                    public int compare(Item obj1, Item obj2) {
                        return obj1.getName().compareTo(obj2.getName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<Item> dateComparator = new Comparator<Item>() {
                    public int compare(Item obj1, Item obj2) {
                        return Utils.parseDate(obj1.getDate_approved()).compareTo(Utils.parseDate(obj2.getDate_approved()));
                    }
                };
                Collections.sort(mDisplayedValues, Collections.reverseOrder(dateComparator));
                break;
        }
        notifyDataSetChanged();
    }
}
