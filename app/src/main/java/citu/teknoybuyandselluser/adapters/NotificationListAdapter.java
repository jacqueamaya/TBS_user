package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models.Notification;


public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private Context mContext;
    private int id;
    private ArrayList<Notification> items ;

    public NotificationListAdapter(Context context, int textViewResourceId, ArrayList<Notification> list)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        Notification notification = items.get(position);

        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);
        ImageView image = (ImageView) mView.findViewById(R.id.image);

        if(items.get(position) != null )
        {
            text.setText(Html.fromHtml(Utils.capitalize(notification.getMessage())));
            image.setImageResource(R.drawable.notif_user);
        }

        return mView;
    }

}
