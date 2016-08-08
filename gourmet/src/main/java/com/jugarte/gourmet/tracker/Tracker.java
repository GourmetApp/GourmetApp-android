package com.jugarte.gourmet.tracker;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Copyright 2016 Javier González
 * All right reserved.
 */
public class Tracker {

    public static class Param {
        public static final String OK = "ok";
        public static final String FAIL = "fail";
        public static final String ERROR = "error";

        public static final String MESSAGE = "message";
    }

    private static Tracker ourInstance = null;
    private static FirebaseAnalytics firebaseAnalytics = null;

    public static Tracker getInstance() {
        return ourInstance;
    }

    public static Tracker getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new Tracker();
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        return ourInstance;
    }

    public void sendMenuEvent(String menu) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.VALUE, menu);
        firebaseAnalytics.logEvent("menu" , bundle);
    }

    public void sendLoginResult(String result) {
        sendLoginResult(result, null);
    }

    public void sendLoginResult(String result, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.VALUE, result);
        if (message != null) {
            bundle.putString(Tracker.Param.MESSAGE, message);
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

}
