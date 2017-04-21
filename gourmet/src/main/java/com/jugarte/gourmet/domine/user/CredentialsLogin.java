package com.jugarte.gourmet.domine.user;

import android.content.Context;
import android.content.SharedPreferences;

class CredentialsLogin {

    private static String PREFERENCE_ID = "credentials";

    private static String USER_KEY = "user";
    private static String PASS_KEY = "pass";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
    }

    static String getUserCredential(Context context) {
        return getSharedPreferences(context).getString(USER_KEY, null);
    }

    static String getPasswordCredential(Context context) {
        return getSharedPreferences(context).getString(PASS_KEY, null);
    }

    static void saveCredentials(String user, String password, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_KEY, user);
        editor.putString(PASS_KEY, password);
        editor.apply();
    }

    static void removeCredentials(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(PASS_KEY);
        editor.apply();
    }

}
