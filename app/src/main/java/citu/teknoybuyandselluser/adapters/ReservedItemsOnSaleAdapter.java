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
import citu.teknoybuyandselluser.models.ReservedItemOnSale;
import io.realm.RealmResults;

/**
 ** Created by jack on 5/02/16.
 */
public class ReservedItemsOnSaleAdapter extends RecyclerView.Adapter<ReservedItemsOnSaleAdapter.ItemViewHolder> {

    private RealmResults<ReservedItemOnSale> mItems;

    public ReservedItemsOnSaleAdapter(RealmResults<ReservedItemOnSale> items) {
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
        ReservedItemOnSale reservedItemOnSale = mItems.get(position);
        holder.itemImage.setImageURI(Uri.parse(reservedItemOnSale.getItem().getPicture()));
        holder.itemName.setText(Utils.capitalize(reservedItemOnSale.getItem().getName()));
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
            ReservedItemOnSale reservedItemOnSale = mItems.get(position);
            Intent intent;
            intent = new Intent(context, ReservedItemActivity.class);
            intent.putExtra(Constants.Item.ID, reservedItemOnSale.getItem().getId());
            intent.putExtra(Constants.Item.RESERVATION_ID, reservedItemOnSale.getId());
            intent.putExtra(Constants.Item.ITEM_CODE, reservedItemOnSale.getItem_code());
            intent.putExtra(Constants.Item.ITEM_NAME, reservedItemOnSale.getItem().getName());
            intent.putExtra(Constants.Item.DESCRIPTION, reservedItemOnSale.getItem().getDescription());
            intent.putExtra(Constants.Item.PAYMENT, reservedItemOnSale.getPayment());
            intent.putExtra(Constants.Item.PICTURE, reservedItemOnSale.getItem().getPicture());
            intent.putExtra(Constants.Item.QUANTITY, reservedItemOnSale.getQuantity());
            intent.putExtra(Constants.Item.RESERVED_DATE, reservedItemOnSale.getReserved_date());
            intent.putExtra(Constants.Item.STARS_REQUIRED, reservedItemOnSale.getItem().getStars_required());
            intent.putExtra(Constants.Item.STARS_TO_USE, reservedItemOnSale.getStars_to_use());

            context.startActivity(intent);
        }
    }
}
