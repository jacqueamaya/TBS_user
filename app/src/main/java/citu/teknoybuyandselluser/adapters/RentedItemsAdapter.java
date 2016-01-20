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
import citu.teknoybuyandselluser.models.RentedItem;

/**
 * Created by Batistil on 1/20/2016.
 */
public class RentedItemsAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private int id;

    private ArrayList<RentedItem> mOriginalValues;
    private ArrayList<RentedItem> mDisplayedValues;

    public RentedItemsAdapter(Context context, int textViewResourceId, ArrayList<RentedItem> list) {
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
        RentedItem item  = mDisplayedValues.get(position);
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);
        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Picasso.with(mContext)
                .load(item.getItem().getPicture())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        String message;
        message = "<b>" + item.getItem().getName() + "</b>";
        text.setText(Html.fromHtml(message));

        return mView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<RentedItem> FilteredArrList = new ArrayList<>();
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
                        String name = mOriginalValues.get(i).getItem().getName();
                        String category = mOriginalValues.get(i).getItem().getCategory().getCategory_name();
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
                mDisplayedValues = (ArrayList<RentedItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<RentedItem> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                Comparator<RentedItem> priceComparator = new Comparator<RentedItem>() {
                    public int compare(RentedItem obj1, RentedItem obj2) {
                        return obj1.getItem().getPrice() < obj2.getItem().getPrice() ? -1 : obj1.getItem().getPrice() > obj2.getItem().getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, priceComparator);
                break;
            case "name":
                Comparator<RentedItem> nameComparator = new Comparator<RentedItem>() {
                    public int compare(RentedItem obj1, RentedItem obj2) {
                        return obj1.getItem().getName().compareTo(obj2.getItem().getName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<RentedItem> dateComparator = new Comparator<RentedItem>() {
                    public int compare(RentedItem obj1, RentedItem obj2) {
                        return Utils.parseDate(obj1.getItem().getDate_approved()).compareTo(Utils.parseDate(obj2.getItem().getDate_approved()));
                    }
                };
                Collections.sort(mDisplayedValues, Collections.reverseOrder(dateComparator));
                break;
        }
        notifyDataSetChanged();
    }
}
