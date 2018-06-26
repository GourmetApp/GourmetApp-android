package com.jugarte.gourmet.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencesHelper implements PreferencesHelper {

    private static String PREFERENCE_ID = "credentials";

    private static String USER_KEY = "user";
    private static String CARD_NUMBER_KEY = "cardNumber";
    private static String PASS_KEY = "pass";

    private final SharedPreferences prefs;

    public AppPreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
    }

    @Override
    public String getUser() {
        return prefs.getString(USER_KEY, null);
    }

    @Override
    public String getPassword() {
        return prefs.getString(PASS_KEY, null);
    }

    @Override
    public String getCardNumber() {
        return prefs.getString(CARD_NUMBER_KEY, null);
    }

    @Override
    public void saveUser(String user, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_KEY, user);
        editor.putString(PASS_KEY, password);
        editor.apply();
    }

    @Override
    public void saveCardNumber(String cardNumber) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CARD_NUMBER_KEY, cardNumber);
        editor.apply();
    }

    @Override
    public void removeUser() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PASS_KEY);
        editor.remove(CARD_NUMBER_KEY);
        editor.apply();
    }
}
