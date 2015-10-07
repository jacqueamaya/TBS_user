package citu.teknoybuyandselluser.listAdapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.models.Item;

/**
 * Created by Jacquelyn on 9/20/2015.
 */
public class ItemsListAdapter extends ArrayAdapter<Item>{
    private Context mContext;
    private int id;
    private ArrayList<Item> items ;
    private String notificationDate;
    private Date reserved_date;

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
                    .placeholder(R.drawable.notif_user)
                    .resize(50,50)
                    .centerCrop()
                    .into(image);

            String message;
            message = "<b>"+items.get(position).getItemName()+"</b>";
            text.setText(Html.fromHtml(message));
        }

        return mView;
    }
}
