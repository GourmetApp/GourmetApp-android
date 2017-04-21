package com.jugarte.gourmet.domine.user;

import android.content.Context;

public class GetUser {

    private final Context context;

    public GetUser(Context context) {
        this.context = context;
    }

    public String getUser() {
        return CredentialsLogin.getUserCredential(context);
    }

    public String getPassword() {
        return CredentialsLogin.getPasswordCredential(context);
    }

    public boolean isLogged() {
        return CredentialsLogin.getPasswordCredential(context) != null;
    }
}
