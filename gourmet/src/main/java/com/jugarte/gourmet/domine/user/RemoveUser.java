package com.jugarte.gourmet.domine.user;

import com.jugarte.gourmet.helpers.CredentialsLogin;

public class RemoveUser {

    public void execute() {
        CredentialsLogin.removeCredentials(null);
    }

}
