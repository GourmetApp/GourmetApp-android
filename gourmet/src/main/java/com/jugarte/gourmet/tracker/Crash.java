package com.jugarte.gourmet.tracker;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Copyright 2016 Javier González
 * All right reserved.
 */
public class Crash {

    public static void report(Throwable throwable) {
        FirebaseCrash.report(throwable);
    }

    public static void log(String string) {
        FirebaseCrash.log(string);
    }
}
