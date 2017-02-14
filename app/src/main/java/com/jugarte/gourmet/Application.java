package com.jugarte.gourmet;

import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.database.FirebaseDatabase;
import com.crashlytics.android.Crashlytics;
import com.jugarte.gourmet.tracker.Tracker;

import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Tracker.getInstance(getApplicationContext());
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }
}
