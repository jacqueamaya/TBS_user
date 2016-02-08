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
import citu.teknoybuyandselluser.models.ReservedItemToDonate;
import io.realm.RealmResults;

/**
 ** Created by jack on 5/02/16.
 */
public class ReservedItemsToDonateAdapter extends RecyclerView.Adapter<ReservedItemsToDonateAdapter.ItemViewHolder> {
    private static final String TAG = "ReservedItemsToDonate";

    private RealmResults<ReservedItemToDonate> mItems;

    public ReservedItemsToDonateAdapter(RealmResults<ReservedItemToDonate> items) {
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
        ReservedItemToDonate reservedItemToDonate = mItems.get(position);
        holder.itemImage.setImageURI(Uri.parse(reservedItemToDonate.getItem().getPicture()));
        holder.itemName.setText(Utils.capitalize(reservedItemToDonate.getItem().getName()));
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
            ReservedItemToDonate reservedItemToDonate = mItems.get(position);
            Intent intent;
            intent = new Intent(context, ReservedItemActivity.class);
            intent.putExtra(Constants.Item.ID, reservedItemToDonate.getId());
            intent.putExtra(Constants.Item.RESERVATION_ID, reservedItemToDonate.getId());
            intent.putExtra(Constants.Item.ITEM_CODE, reservedItemToDonate.getItem_code());
            intent.putExtra(Constants.Item.ITEM_NAME, reservedItemToDonate.getItem().getName());
            intent.putExtra(Constants.Item.DESCRIPTION, reservedItemToDonate.getItem().getDescription());
            intent.putExtra(Constants.Item.PAYMENT, reservedItemToDonate.getPayment());
            intent.putExtra(Constants.Item.PICTURE, reservedItemToDonate.getItem().getPicture());
            intent.putExtra(Constants.Item.QUANTITY, reservedItemToDonate.getQuantity());
            intent.putExtra(Constants.Item.RESERVED_DATE, reservedItemToDonate.getReserved_date());
            intent.putExtra(Constants.Item.STARS_REQUIRED, reservedItemToDonate.getItem().getStars_required());
            intent.putExtra(Constants.Item.STARS_TO_USE, reservedItemToDonate.getStars_to_use());

            context.startActivity(intent);
        }
    }
}
