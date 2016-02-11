package kidouchi.chronobook.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kidouchi.chronobook.alarm.AlarmIntentService;

/**
 * Created by iuy407 on 12/20/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "kidouchi.chronobook.alarm";

    // Triggered by alarm and periodically starts service to run task
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, AlarmIntentService.class);
        alarmIntent.putExtra("eventTitle", intent.getStringExtra("eventTitle"));
        context.startService(alarmIntent);

    }
}
