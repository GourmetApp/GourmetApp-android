package com.jugarte.gourmet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.jugarte.gourmet.di.component.ApplicationComponent;
import com.jugarte.gourmet.di.component.DaggerApplicationComponent;
import com.jugarte.gourmet.di.module.ApplicationModule;
import com.jugarte.gourmet.services.GourmetReceiver;
import com.jugarte.gourmet.tracker.Tracker;

import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Tracker.getInstance(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        applicationComponent.inject(this);

        Intent alarm = new Intent(getApplicationContext(), GourmetReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 4 * 60 * 1000, pendingIntent);
        }

    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
