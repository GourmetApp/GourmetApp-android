package com.jugarte.gourmet.domine.user;

import com.jugarte.gourmet.data.prefs.PreferencesHelper;

public class SaveUser {

    private final PreferencesHelper preferencesHelper;

    public SaveUser(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public void saveUser(String user, String password) {
        preferencesHelper.saveUser(user, password);
    }
}
