package com.jugarte.gourmet.domine.user;

import android.content.Context;

import com.jugarte.gourmet.helpers.CredentialsLogins;

public class GetUser {

    private final Context context;

    public GetUser(Context context) {
        this.context = context;
    }

    public String getUser() {
        return CredentialsLogins.getUserCredential(context);
    }

    public String getPassword() {
        return CredentialsLogins.getPasswordCredential(context);
    }

    public boolean isLogged() {
        return CredentialsLogins.isCredential(context);
    }
}
