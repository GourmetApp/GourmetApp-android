package com.jugarte.gourmet;

import com.google.firebase.database.FirebaseDatabase;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }
}
