package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.models.Item;

/**
 * Created by Jacquelyn on 9/20/2015.
 */
public class ItemsListAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "ItemListAdapter";
    private Context mContext;
    private int id;
    private String notificationDate;
    private Date reserved_date;

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

        Log.v(TAG,"URL picture: "+item.getPicture());
        Picasso.with(mContext)
                .load(item.getPicture())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        String message;
        message = "<b>" + item.getItemName() + "</b>";
        text.setText(Html.fromHtml(message));

        return mView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Item> FilteredArrList = new ArrayList<Item>();
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Item>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String name = mOriginalValues.get(i).getItemName();
                        String category = mOriginalValues.get(i).getCategory();
                        if (category.equals(constraint.toString()) || name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
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

}
