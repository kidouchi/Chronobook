package kidouchi.chronobook.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import kidouchi.chronobook.R;
import kidouchi.chronobook.adapters.RVEventAdapter;
import kidouchi.chronobook.models.Event;

public class EventListViewActivity extends Activity {

    private Realm realm;

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
                openFormIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openFormIntent);
            }
        });

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getInstance(realmConfig);

        RecyclerView rvEventList = (RecyclerView) findViewById(R.id.rv_event_list);

        RealmResults<Event> result = realm.where(Event.class).findAll();
        result.sort("startDateTime"); // Sorts in ascending order
        Event[] events = new Event[result.size()];
        RVEventAdapter adapter = new RVEventAdapter(Arrays.asList(result.toArray(events)), this);
        adapter.notifyDataSetChanged();

        rvEventList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        rvEventList.setLayoutManager(layoutManager);
        rvEventList.setNestedScrollingEnabled(false);
        rvEventList.setHasFixedSize(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }
}
