package com.jugarte.gourmet.domine.user;

import android.content.Context;

public class SaveUser {

    private final Context context;

    public SaveUser(Context context) {
        this.context = context;
    }

    public void saveUser(String user, String password) {
        CredentialsLogin.saveCredentials(user, password, context);
    }
}
