package com.jugarte.gourmet.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.jugarte.gourmet.BuildConfig;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImpl;
import com.jugarte.gourmet.data.prefs.AppPreferencesHelper;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.ui.balance.BalanceActivity;

public class NotificationService extends Service implements GetGourmet.OnGourmetResponse {

    public static final int SERVICE_REQUEST_CODE = 1;
    public static final int NOTIFICATION_ID = 1;

    private ThreadManager tm;
    GetGourmet getGourmet;
    GetUser getUser;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getGourmet = new GetGourmet();
        getUser = new GetUser(new AppPreferencesHelper(getApplicationContext()));
        tm = new ThreadManagerImpl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (getUser.isLogged()) {
            checkDataChanges();
        }
        stopSelf(startId);
        return Service.START_NOT_STICKY;
    }

    private void checkDataChanges() {

        final String user = getUser.getUser();
        final String password = getUser.getPassword();
        tm.runOnBackground(new Runnable() {
            @Override
            public void run() {
                getGourmet.execute(user, password, NotificationService.this);
            }
        });
    }

    @Override
    public void success(final Gourmet gourmet) {
        tm.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                showNotification(gourmet);
            }
        });
    }

    @Override
    public void notConnection(Gourmet cacheGourmet) {

    }

    @Override
    public void notUserFound() {

    }

    private void showNotification(Gourmet gourmet) {
        if (!BuildConfig.DEBUG && gourmet.getNewOperations() == 0) {
            return;
        }

        String title = getResources().getQuantityString(R.plurals.new_operations_title,
                gourmet.getNewOperations());

        String text;
        if (gourmet.isIncreaseOfBalance()) {
            text = getString(R.string.new_update_balance);
        } else {
            text = String.format(getResources().getQuantityString(R.plurals.new_operations_text,
                    gourmet.getNewOperations()),
                    gourmet.getNewOperations());
        }

        Intent intent = new Intent(this, BalanceActivity.class);
        intent.putExtra(BalanceActivity.EXTRA_GOURMET, gourmet);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker(title)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.accent))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
