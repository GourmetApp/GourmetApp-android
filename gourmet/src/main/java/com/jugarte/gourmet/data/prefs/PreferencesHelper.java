package com.jugarte.gourmet.data.prefs;

public interface PreferencesHelper {

    String getUser();

    String getPassword();

    void saveUser(String user, String password);

    void removeUser();

}
