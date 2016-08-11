package com.jugarte.gourmet.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by javiergon on 18/05/15.
 */
public class CredentialsLogin {

    private static String PREFERENCE_ID = "credentials";

    private static String USER_KEY = "user";
    private static String PASS_KEY = "pass";
    private static String CREDENTIAL_KEY = "credential";

    private static  SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
    }

    public static boolean isCredential(Context context) {
        return getSharedPreferences(context).getBoolean(CREDENTIAL_KEY, false);
    }

    public static String getUserCredential(Context context) {
        return getSharedPreferences(context).getString(USER_KEY, null);
    }

    public static String getPasswordCredential(Context context) {
        return getSharedPreferences(context).getString(PASS_KEY, null);
    }

    public static void saveCredential(String user, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_KEY, user);
        editor.apply();
    }

    public static void saveCredentials(String user, String password, boolean persistent, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_KEY, user);
        editor.putString(PASS_KEY, password);
        editor.putBoolean(CREDENTIAL_KEY, persistent);
        editor.apply();
    }

    public static void removeCredentials(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(PASS_KEY);
        editor.putBoolean(CREDENTIAL_KEY, false);
        editor.apply();
    }

}
