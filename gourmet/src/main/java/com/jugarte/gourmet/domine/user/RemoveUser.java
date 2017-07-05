package com.jugarte.gourmet.domine.user;

import com.jugarte.gourmet.data.prefs.PreferencesHelper;

public class RemoveUser {

    private final PreferencesHelper preferencesHelper;

    public RemoveUser(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public void removeUser() {
        preferencesHelper.removeUser();
    }

}
