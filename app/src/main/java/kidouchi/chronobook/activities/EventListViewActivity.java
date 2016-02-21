package kidouchi.chronobook.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import kidouchi.chronobook.EventCardTouchCallback;
import kidouchi.chronobook.EventItemClickListener;
import kidouchi.chronobook.R;
import kidouchi.chronobook.adapters.RVEventAdapter;
import kidouchi.chronobook.models.Event;

public class EventListViewActivity extends Activity {

    private Realm realm;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<Event> mEvents;

    private Button mGettingStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openFormIntent = new Intent(EventListViewActivity.this,
                        EventFormActivity.class);
                openFormIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                openFormIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openFormIntent);
            }
        });
        mGettingStartedBtn = (Button) findViewById(R.id.getting_started_btn);

        // Setup Realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getInstance(realmConfig);

        RecyclerView rvEventList = (RecyclerView) findViewById(R.id.rv_event_list);

        RealmResults<Event> result = realm.where(Event.class).findAll();
        result.sort("startDateTime"); // Sorts in ascending order
        Event[] events = new Event[result.size()];
        mEvents = new ArrayList<>(Arrays.asList(result.toArray(events)));
        RVEventAdapter adapter = new RVEventAdapter(mEvents, this, rvEventList);
        adapter.notifyDataSetChanged();

        // When there are events, then hide "getting started" view
        if (events.length > 0) {
            findViewById(android.R.id.empty).setVisibility(View.GONE);
        }

        rvEventList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        rvEventList.setLayoutManager(layoutManager);
        rvEventList.setNestedScrollingEnabled(false);
        rvEventList.setHasFixedSize(false);
        rvEventList.addOnItemTouchListener(new EventItemClickListener(this,
                new EventItemClickListener.OnItemInteraction() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Event event = mEvents.get(position);
                        Intent eventViewIntent = new Intent(EventListViewActivity.this,
                                EventViewScrollingActivity.class);

                        startActivity(eventViewIntent);
                    }
                }));
//        rvEventList.setItemAnimator(new SlideInUpAnimator());
//        rvEventList.addOnItemTouchListener(new EventItemClickListener());

        ItemTouchHelper.Callback cb = new EventCardTouchCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(cb);
        mItemTouchHelper.attachToRecyclerView(rvEventList);
    }

    public void gettingStarted(View v) {
        Intent startIntent = new Intent(this, EventViewScrollingActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }
}
