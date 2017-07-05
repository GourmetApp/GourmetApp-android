package com.jugarte.gourmet.domine.user;

import com.jugarte.gourmet.data.prefs.PreferencesHelper;

public class GetUser {

    private final PreferencesHelper preferencesHelper;

    public GetUser(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public String getUser() {
        return preferencesHelper.getUser();
    }

    public String getPassword() {
        return preferencesHelper.getPassword();
    }

    public boolean isLogged() {
        return getPassword() != null;
    }
}
