package com.jugarte.gourmet.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.jugarte.gourmet.BuildConfig.APPLICATION_ID;

public class NotificationServiceReceiver extends BroadcastReceiver {
    public static final String ACTION_NOTIFICATION_SERVICE_START = APPLICATION_ID + ".NOTIFICATION_SERVICE_START";
    public static final String ACTION_NOTIFICATION_SERVICE_STOP = APPLICATION_ID + ".NOTIFICATION_SERVICE_STOP";

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (ACTION_NOTIFICATION_SERVICE_START.equals(intent.getAction())) {
            startNotificationRepeatingAlarm();
        } else if (ACTION_NOTIFICATION_SERVICE_STOP.equals(intent.getAction())) {
            stopNotificationRepeatingAlarm();
        }
    }

    private void startNotificationRepeatingAlarm() {
        stopNotificationRepeatingAlarm();

        PendingIntent pendingIntent = NotificationUtils.getNotificationPendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                AlarmManager.INTERVAL_HOUR * 2, pendingIntent);
    }

    private void stopNotificationRepeatingAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(NotificationUtils.getNotificationPendingIntent(context));
    }
}
