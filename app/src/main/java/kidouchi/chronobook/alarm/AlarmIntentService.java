package kidouchi.chronobook.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import kidouchi.chronobook.R;

/**
 * Created by iuy407 on 12/20/15.
 */
public class AlarmIntentService extends IntentService {

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Create notification
        NotificationCompat.Builder mNotification =
                new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_time_grey_36dp)
                    .setContentTitle(intent.getStringExtra("eventTitle") + " has started!")
                    .setAutoCancel(true);

        NotificationManager mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        // Build and issue notification
        mNotifyManager.notify(1, mNotification.build());
    }
}
