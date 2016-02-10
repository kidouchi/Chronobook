package kidouchi.chronobook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import kidouchi.chronobook.EventCardTouchHelperAdapter;
import kidouchi.chronobook.EventCardTouchViewHolder;
import kidouchi.chronobook.R;
import kidouchi.chronobook.models.Event;
import kidouchi.chronobook.models.Location;

/**
 * Created by iuy407 on 11/21/15.
 */
public class RVEventAdapter extends RecyclerView.Adapter<RVEventAdapter.ViewHolder>
        implements EventCardTouchHelperAdapter {

    public static Context context;
    private List<Event> mEvents;
    private Realm realm;
    public static int SELECT_ANIM_DURATION = 200;
    private RecyclerView mRecyclerView;

    public RVEventAdapter(List<Event> events, Context context, RecyclerView recyclerView) {
        this.mEvents = events;
        this.context = context;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        realm = Realm.getDefaultInstance();

        // Inflate event item layout
        View eventView = inflater.inflate(R.layout.event_list_item, parent, false);

        return new ViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Set view based on event data
        holder.bindEventToView(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @Override
    public void onItemDismiss(final int pos, final View itemView) {
        notifyItemRemoved(pos);
    }

    @Override
    public void onItemDismissUndone(int pos) {
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos+1, getItemCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements EventCardTouchViewHolder {

        private ImageView mPlaceholderImageView;
        private TextView mEventTitle;
        private TextView mEventDate;
        private TextView mEventTime;
        private TextView mEventLocation;

        private RelativeLayout mCardHeader;
        private LinearLayout mCardBody;
        private RelativeLayout mCard;

        private TextView mEventAlarm; // TODO
//        private TextView mEventDescription;

        private Bitmap bitmap;

        public ViewHolder(View itemView) {
            super(itemView);

            mPlaceholderImageView = (ImageView) itemView.findViewById(R.id.placeholder_image_view);
            mEventTitle = (TextView) itemView.findViewById(R.id.event_title);
            mEventDate = (TextView) itemView.findViewById(R.id.event_date);
            mEventTime = (TextView) itemView.findViewById(R.id.event_time);
            mEventLocation = (TextView) itemView.findViewById(R.id.event_location);

            mCard = (RelativeLayout) itemView.findViewById(R.id.event_card);
            mCardHeader = (RelativeLayout) itemView.findViewById(R.id.card_header);
            mCardBody = (LinearLayout) itemView.findViewById(R.id.card_body);
//            mEventAlarm = (TextView) itemView.findViewById(R.id.event_alarm);
//            mEventDescription = (TextView) itemView.findViewById(R.id.event_description);
        }

        public void bindEventToView(Event event) {

            // Set header image
            Picasso.with(context).load(new File(event.getPlaceHolderFilepath()))
                    .into(mPlaceholderImageView);

            mEventTitle.setText(event.getTitle());
            if (event.getCategoryDrawable() != 0) {
                mEventTitle.setCompoundDrawablesWithIntrinsicBounds(
                        event.getCategoryDrawable(), 0, 0, 0);
            }

            // Converting timestamp in date and time format
            Date startDateTime = new Date(event.getStartDateTime());
            Date endDateTime = new Date(event.getEndDateTime());

            SimpleDateFormat formatDate = new SimpleDateFormat("MMM dd");
            SimpleDateFormat formatTimeMeridiem = new SimpleDateFormat("hh:mm a");

            /** Format to date and time **/
            String startDateFormatted = formatDate.format(startDateTime);
            String startTimeFormatted = formatTimeMeridiem.format(startDateTime);

            String endDateFormatted = formatDate.format(endDateTime);
            String endTimeFormatted = formatTimeMeridiem.format(endDateTime);

            mEventDate.setText(startDateFormatted + " - " + endDateFormatted);
            mEventTime.setText(startTimeFormatted + " - " + endTimeFormatted);

            Location loc = event.getLocation();
            mEventLocation.setText(loc.getStreet() + " " + loc.getCity() + ", " +
                    loc.getState() + " " + loc.getZipcode());

//            mEventDescription.setText(event.getDescription());
        }

        @Override
        public void onItemSelected() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setElevation(itemView, 15.0f);
            } else {
                itemView.animate().setDuration(SELECT_ANIM_DURATION).translationZ(15.0f);
                itemView.setElevation(15.0f);
            }
        }

        @Override
        public void onItemClear() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setElevation(itemView, 2.0f);
            } else {
                itemView.animate().setDuration(SELECT_ANIM_DURATION).translationZ(2.0f);
                itemView.setElevation(2.0f);
            }
        }
    }
}
