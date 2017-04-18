package com.jugarte.gourmet.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class CredentialsLogins {

    private static String PREFERENCE_ID = "credentials";

    private static String USER_KEY = "user";
    private static String PASS_KEY = "pass";
    private static String CREDENTIAL_KEY = "credential";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
    }

    public static boolean isCredential(Context context) {
        return getSharedPreferences(context).getString(PASS_KEY, null) != null;
    }

    public static String getUserCredential(Context context) {
        return getSharedPreferences(context).getString(USER_KEY, null);
    }

    public static String getPasswordCredential(Context context) {
        return getSharedPreferences(context).getString(PASS_KEY, null);
    }

    public static void saveCredentials(String user, String password, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_KEY, user);
        editor.putString(PASS_KEY, password);
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
