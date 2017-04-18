package com.jugarte.gourmet.domine.user;

import android.content.Context;

import com.jugarte.gourmet.helpers.CredentialsLogins;


public class SaveUser {

    private final Context context;

    public SaveUser(Context context) {
        this.context = context;
    }

    public void saveUser(String user, String password) {
        CredentialsLogins.saveCredentials(user, password, context);
    }
}
