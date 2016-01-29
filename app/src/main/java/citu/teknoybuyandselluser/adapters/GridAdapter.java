package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models.Item;

/**
 ** Created by Janna Tapitha on 1/20/2016.
 */
public class GridAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater inflater;

    private ArrayList<citu.teknoybuyandselluser.models.Item> mOriginalValues;
    private ArrayList<citu.teknoybuyandselluser.models.Item> mDisplayedValues;

    public GridAdapter(Context context, ArrayList<Item> list) {
        inflater = LayoutInflater.from(context);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        Item item  = mDisplayedValues.get(position);

        View v = view;
        SimpleDraweeView picture;
        TextView name;
        TextView quantity;

        if (v == null) {
            v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.quantity, v.findViewById(R.id.quantity));
        }

        picture = (SimpleDraweeView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        quantity = (TextView) v.getTag(R.id.quantity);

        picture.setImageURI(Uri.parse(item.getPicture()));
        name.setText(item.getName());
        quantity.setText("" + item.getQuantity());

        return v;
    }

    public List<Item> getDisplayView() {
        return mDisplayedValues;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Item> FilteredArrList = new ArrayList<>();
                String searchByCategory[] = constraint.toString().split(",");

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
                        if(searchByCategory.length == 2) {
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

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
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
