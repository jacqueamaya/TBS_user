package citu.teknoybuyandselluser.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.Utils;
import citu.teknoybuyandselluser.models.Notification;
import io.realm.RealmResults;

/**
 ** Created by jack on 2/02/16.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private static final String TAG = "NotificationsAdapter";

    private RealmResults<Notification> mNotifications;

    public NotificationsAdapter(RealmResults<Notification> notifications) {
        mNotifications = notifications;
    }

    public void updateData (RealmResults<Notification> notifications) {
        mNotifications = notifications;
        notifyDataSetChanged();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        holder.userAvatar.setImageResource(R.drawable.notif_user);
        holder.notificationText.setText(Utils.capitalize(notification.getMessage()));
        holder.notificationDate.setText(Utils.parseDate(notification.getNotification_date()));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView userAvatar;
        TextView notificationText;
        TextView notificationDate;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            userAvatar = (SimpleDraweeView) itemView.findViewById(R.id.image);
            notificationText = (TextView) itemView.findViewById(R.id.textView);
            notificationDate = (TextView) itemView.findViewById(R.id.txtDate);
        }
    }
}
