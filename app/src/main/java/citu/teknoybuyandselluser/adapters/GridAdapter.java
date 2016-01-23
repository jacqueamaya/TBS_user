package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselluser.R;

/**
 * Created by Janna Tapitha on 1/20/2016.
 */
public class GridAdapter extends BaseAdapter {

    private List<Item> items = new ArrayList<>();
    private LayoutInflater inflater;
    private SimpleDraweeView picture;

    public GridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items.add(new Item("Turquoise Jewelry Set", "2", R.drawable.accessories));
        items.add(new Item("Lemonney Snicket Book Set", "1", R.drawable.books));
        items.add(new Item("Crop Top", "5", R.drawable.clothes));
        items.add(new Item("Jade Laptop", "1", R.drawable.gadgets));
        items.add(new Item("Rubber Shoes", "3", R.drawable.shoes));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
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

        Item item = (Item) getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);
        quantity.setText(item.quantity);

        return v;
    }

    private class Item {
        final String name;
        final String quantity;
        final int drawableId;

        Item(String name, String quantity, int drawableId) {
            this.name = name;
            this.quantity = quantity;
            this.drawableId = drawableId;
        }
    }
}
