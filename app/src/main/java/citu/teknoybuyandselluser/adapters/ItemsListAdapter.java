package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
public class ItemsListAdapter extends ArrayAdapter<Item> implements Filterable{
    private Context mContext;
    private int id;
    private ArrayList<Item> items ;
    private String notificationDate;
    private Date reserved_date;

    private List<Item> mOriginalValues;
    private List<Item> mDisplayedValues;

    public ItemsListAdapter(Context context, int textViewResourceId, ArrayList<Item> list)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);
        ImageView image = (ImageView) mView.findViewById(R.id.image);

        if(items.get(position) != null )
        {
            Picasso.with(mContext)
                    .load(items.get(position).getPicture())
                    .placeholder(R.drawable.thumbsq_24dp)
                    .resize(50,50)
                    .centerCrop()
                    .into(image);

            String message;
            message = "<b>"+items.get(position).getItemName()+"</b>";
            text.setText(Html.fromHtml(message));
        }

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

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String name = mOriginalValues.get(i).getItemName();
                        String data = mOriginalValues.get(i).getCategory();
                        if (data.equals(constraint.toString()) || name.toLowerCase().contains(constraint.toString().toLowerCase())) {
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

}
