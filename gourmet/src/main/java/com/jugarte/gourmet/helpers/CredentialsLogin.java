package com.jugarte.gourmet.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by javiergon on 18/05/15.
 */
public class CredentialsLogin {

    private static String USER_KEY = "user";
    private static String PASS_KEY = "pass";

    private static Activity mActivity = null;

    private static SharedPreferences getSharedPreferences() {
        Activity activity = mActivity;
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static boolean isCredential() {
        return (getSharedPreferences().getString(PASS_KEY, null) != null);
    }

    public static String getUserCredential() {
        return getSharedPreferences().getString(USER_KEY, null);
    }

    public static String getPasswordCredential() {
        return getSharedPreferences().getString(PASS_KEY, null);
    }

    public static void saveCredential(String user) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(USER_KEY, user);
        editor.commit();
    }

    public static void saveCredentials(String user, String password) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(USER_KEY, user);
        editor.putString(PASS_KEY, password);
        editor.commit();
    }

    public static void removeCredentials() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(PASS_KEY);
        editor.commit();
    }

}
