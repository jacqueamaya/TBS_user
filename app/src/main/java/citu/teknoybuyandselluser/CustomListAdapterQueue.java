package citu.teknoybuyandselluser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapterQueue extends ArrayAdapter{
    private Context mContext;
    private int id;
    private List<String> items ;

    public CustomListAdapterQueue(Context context, int textViewResourceId, List<String> list)
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

        TextView text = (TextView) mView.findViewById(R.id.textViewPendingSItem);

        if(items.get(position) != null )
        {
            text.setText(items.get(position));
        }

        return mView;
    }
}
