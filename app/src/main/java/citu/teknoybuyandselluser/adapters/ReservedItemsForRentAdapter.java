package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.ReservedItemActivity;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models.ReservedItemForRent;
import io.realm.RealmResults;

/**
 ** Created by jack on 5/02/16.
 */
public class ReservedItemsForRentAdapter extends RecyclerView.Adapter<ReservedItemsForRentAdapter.ItemViewHolder> {

    private RealmResults<ReservedItemForRent> mItems;

    public ReservedItemsForRentAdapter(RealmResults<ReservedItemForRent> items) {
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
        ReservedItemForRent reservedItemForRent = mItems.get(position);
        holder.itemImage.setImageURI(Uri.parse(reservedItemForRent.getItem().getPicture()));
        holder.itemName.setText(Utils.capitalize(reservedItemForRent.getItem().getName()));
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
            ReservedItemForRent reservedItemForRent = mItems.get(position);
            Intent intent;
            intent = new Intent(context, ReservedItemActivity.class);
            intent.putExtra(Constants.Item.ID, reservedItemForRent.getId());
            intent.putExtra(Constants.Item.RESERVATION_ID, reservedItemForRent.getId());
            intent.putExtra(Constants.Item.ITEM_CODE, reservedItemForRent.getItem_code());
            intent.putExtra(Constants.Item.ITEM_NAME, reservedItemForRent.getItem().getName());
            intent.putExtra(Constants.Item.DESCRIPTION, reservedItemForRent.getItem().getDescription());
            intent.putExtra(Constants.Item.PAYMENT, reservedItemForRent.getPayment());
            intent.putExtra(Constants.Item.PICTURE, reservedItemForRent.getItem().getPicture());
            intent.putExtra(Constants.Item.QUANTITY, reservedItemForRent.getQuantity());
            intent.putExtra(Constants.Item.RESERVED_DATE, reservedItemForRent.getReserved_date());
            intent.putExtra(Constants.Item.STARS_REQUIRED, reservedItemForRent.getItem().getStars_required());
            intent.putExtra(Constants.Item.STARS_TO_USE, reservedItemForRent.getStars_to_use());

            context.startActivity(intent);
        }
    }
}
