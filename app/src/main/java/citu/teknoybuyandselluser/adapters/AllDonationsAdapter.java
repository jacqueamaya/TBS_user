package citu.teknoybuyandselluser.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.models.AvailableDonation;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 ** Created by jack on 9/02/16.
 */
public class AllDonationsAdapter extends RealmBaseAdapter<AvailableDonation> {
    private LayoutInflater inflater;
    private RealmResults<AvailableDonation> mItems;

    public AllDonationsAdapter(Context context, RealmResults<AvailableDonation> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        inflater = LayoutInflater.from(context);
        mItems = realmResults;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        AvailableDonation donation = getItem(position);
        View v = view;
        SimpleDraweeView picture;
        TextView name;
        TextView quantity;

        if (v == null) {
            v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.txtItemName, v.findViewById(R.id.txtItemName));
            v.setTag(R.id.quantity, v.findViewById(R.id.quantity));
        }

        picture = (SimpleDraweeView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.txtItemName);
        quantity = (TextView) v.getTag(R.id.quantity);

        picture.setImageURI(Uri.parse(donation.getPicture()));
        name.setText(donation.getName());
        String strQuantity = "" + donation.getQuantity();
        quantity.setText(strQuantity);

        return v;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case Constants.Item.PRICE:
                mItems.sort(Constants.Item.PRICE, Sort.ASCENDING);
                break;
            case Constants.Item.NAME:
                mItems.sort(Constants.Item.NAME, Sort.ASCENDING);
                break;
            default:
                mItems.sort(Constants.Item.DATE_APPROVED, Sort.DESCENDING);
                break;
        }
    }

    public void filter(CharSequence constraint) {
        String splitString[] = constraint.toString().split(",");
        if(splitString.length == 2) {
            updateRealmResults(
                    Realm.getDefaultInstance()
                            .where(AvailableDonation.class)
                            .contains(Constants.Item.NAME, constraint.toString(), Case.INSENSITIVE)
                            .contains(Constants.Item.CATEGORY_CATEGORY_NAME, constraint.toString())
                            .findAll()
            );
        } else {
            updateRealmResults(
                    Realm.getDefaultInstance()
                            .where(AvailableDonation.class)
                            .contains(Constants.Item.NAME, constraint.toString(), Case.INSENSITIVE)
                            .or()
                            .contains(Constants.Item.CATEGORY_CATEGORY_NAME, constraint.toString())
                            .findAll()
            );
        }
    }
}
