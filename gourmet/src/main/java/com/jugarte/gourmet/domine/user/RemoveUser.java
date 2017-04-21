package com.jugarte.gourmet.domine.user;

import android.content.Context;

public class RemoveUser {

    private final Context context;

    public RemoveUser(Context context) {
        this.context = context;
    }

    public void execute() {
        CredentialsLogin.removeCredentials(context);
    }

}
