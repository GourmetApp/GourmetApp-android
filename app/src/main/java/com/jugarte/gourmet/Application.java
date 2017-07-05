package com.jugarte.gourmet;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.jugarte.gourmet.di.component.ApplicationComponent;
import com.jugarte.gourmet.di.component.DaggerApplicationComponent;
import com.jugarte.gourmet.di.module.ApplicationModule;
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

    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
