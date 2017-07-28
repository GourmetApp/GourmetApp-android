package com.jugarte.gourmet.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class NotificationUtils {
    private NotificationUtils() {
    }

    static PendingIntent getNotificationPendingIntent(@NonNull Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        return PendingIntent.getService(context, NotificationService.SERVICE_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void startNotificationService(@NonNull Context context) {
        context.sendBroadcast(new Intent(NotificationServiceReceiver.ACTION_NOTIFICATION_SERVICE_START));
    }

    public static void stopNotificationService(@NonNull Context context) {
        context.sendBroadcast(new Intent(NotificationServiceReceiver.ACTION_NOTIFICATION_SERVICE_STOP));
    }

}
