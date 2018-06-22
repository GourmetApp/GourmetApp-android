package com.jugarte.gourmet.data.prefs;

public interface PreferencesHelper {

    String getUser();

    String getPassword();

    String getCardNumber();

    void saveUser(String user, String password);

    void saveCardNumber(String cardNumber);

    void removeUser();

}
