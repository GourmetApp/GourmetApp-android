package com.jugarte.gourmet.domine.user;

import android.content.Context;

import com.jugarte.gourmet.helpers.CredentialsLogins;

public class RemoveUser {

    private final Context context;

    public RemoveUser(Context context) {
        this.context = context;
    }

    public void execute() {
        CredentialsLogins.removeCredentials(context);
    }

}
