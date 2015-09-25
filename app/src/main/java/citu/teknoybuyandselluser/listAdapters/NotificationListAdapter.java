package citu.teknoybuyandselluser.listAdapters;

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
import citu.teknoybuyandselluser.models.Notification;


public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private static final String TAG = "NotificationListAdapter";
    private Context mContext;
    private int id;
    private ArrayList<Notification> items ;
    private String notificationDate;
    private Date notif_date;

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
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);
        ImageView image = (ImageView) mView.findViewById(R.id.image);

        if(items.get(position) != null )
        {
            try {
                notif_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(items.get(position).getNotification_date());
               // String notificationDate = new SimpleDateFormat("yyyy-MM-dd").format(notif_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String message;
            switch(items.get(position).getNotification_type()){
                case "sell": message = "<b>"+items.get(position).getMakerUserName()+"</b> approved your request to <b>sell</b> your <b>"+items.get(position).getItemName()+"</b>.<br><small> Date: "+notif_date+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                case "donate": message = "<b>"+items.get(position).getMakerUserName()+"</b> approved your request to <b>donate</b> your <b>"+items.get(position).getItemName()+"</b>.<br><small> Date:"+notif_date+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                case "buy": message = "<b>"+items.get(position).getMakerUserName()+"</b> wants to <b>buy</b> your <b>"+items.get(position).getItemName()+"</b>.<br><small> Date:"+notif_date+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                default: message = "<i>This is a default notification message</i>";
                    text.setText(Html.fromHtml(message));
                    break;
            }

            image.setImageResource(R.drawable.notif_user);
        }

        return mView;
    }

}
