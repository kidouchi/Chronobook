package kidouchi.chronobook;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by iuy407 on 12/20/15.
 */
public class AlarmIntentService extends IntentService {

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Launch alarm notification here
        NotificationCompat.Builder mNotification =
                new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_time_grey_36dp)
                    .setContentTitle("An Event Has Started!")
                    .setContentText(intent.getStringExtra("eventTitle"));

        mNotification.setAutoCancel(true);
    }
}
