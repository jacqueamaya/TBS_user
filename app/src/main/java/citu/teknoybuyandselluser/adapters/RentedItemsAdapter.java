package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.PendingItemActivity;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models.RentedItem;
import io.realm.RealmResults;

/**
 ** Created by Batistil on 1/20/2016.
 */
public class RentedItemsAdapter extends RecyclerView.Adapter<RentedItemsAdapter.ItemViewHolder> {
    private static final String TAG = "RentedItemsAdapter";

    private RealmResults<RentedItem> mItems;

    public RentedItemsAdapter(RealmResults<RentedItem> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        RentedItem rentedItem = mItems.get(position);
        holder.itemImage.setImageURI(Uri.parse(rentedItem.getItem().getPicture()));
        holder.itemName.setText(Utils.capitalize(rentedItem.getItem().getName()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView itemImage;
        TextView itemName;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemImage = (SimpleDraweeView) itemView.findViewById(R.id.image);
            itemName = (TextView) itemView.findViewById(R.id.textViewItem);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            int position = getAdapterPosition();
            RentedItem rentedItem = mItems.get(position);
            Intent intent;
            intent = new Intent(context, PendingItemActivity.class);
            intent.putExtra(Constants.Item.ID, rentedItem.getId());
            intent.putExtra(Constants.Item.ITEM_NAME, rentedItem.getItem().getName());
            intent.putExtra(Constants.Item.DESCRIPTION, rentedItem.getItem().getDescription());
            intent.putExtra(Constants.Item.PICTURE, rentedItem.getItem().getPicture());
            intent.putExtra(Constants.Item.FORMAT_PRICE, Utils.formatFloat(rentedItem.getItem().getPrice()));
            intent.putExtra(Constants.Item.PENALTY, rentedItem.getPenalty());
            intent.putExtra(Constants.Item.QUANTITY, rentedItem.getQuantity());
            intent.putExtra(Constants.Item.RENT_DATE, rentedItem.getRent_date());
            intent.putExtra(Constants.Item.RENT_EXPIRATION, rentedItem.getRent_expiration());

            context.startActivity(intent);
        }
    }
}
