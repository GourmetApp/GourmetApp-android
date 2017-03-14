package com.jugarte.gourmet.login;

import android.content.Context;

import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.helpers.CredentialsLogin;

public class LoginPresenter implements GetGourmet.OnGourmetResponse {

    private Context context;
    private LoginScreen screen;

    private String user, password;

    private final ThreadManager threadManager = new ThreadManagerImp();

    void bind(Context context, LoginScreen screen) {
        this.context = context;
        this.screen = screen;
    }

    void login(final String user, final String password) {
        this.user = user;
        this.password = password;

        CredentialsLogin.saveCredential(user, context);
        if (user == null || user.length() == 0
                || password == null || password.length() == 0) {
            screen.showErrorEmptyFields();
            return;
        }

        screen.showLoading();
        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                new GetGourmet().execute(user, password, LoginPresenter.this);
            }
        });

    }

    @Override
    public void success(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.hideLoading();
                if (gourmet != null) {
                    screen.saveCredentials(user, password);
                    screen.navigateToMain(gourmet);
                } else {
                    screen.showErrorNotConnection();
                }
            }
        });
    }

    @Override
    public void notConnection(Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.hideLoading();
                screen.showErrorNotConnection();
            }
        });
    }

    @Override
    public void notUserFound() {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.hideLoading();
                screen.showErrorNotUserFound();
            }
        });
    }
}
