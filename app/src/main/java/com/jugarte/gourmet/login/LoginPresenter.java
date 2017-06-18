package com.jugarte.gourmet.login;

import android.content.Context;

import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.domine.user.SaveUser;

public class LoginPresenter implements GetGourmet.OnGourmetResponse {

    private LoginScreen screen;

    private String user, password;
    private SaveUser saveUser;
    private GetUser getUser;

    private final ThreadManager threadManager = new ThreadManagerImp();

    void bind(Context context, LoginScreen screen) {
        this.screen = screen;
        this.getUser = new GetUser(context);
        this.saveUser = new SaveUser(context);

        if (getUser.isLogged()) {
            screen.navigateToBalance(null);
        }

        screen.showUser(getUser.getUser());
    }

    void login(final String user, final String password) {
        this.user = user;
        this.password = password;

        if (user == null || user.length() == 0
                || password == null || password.length() == 0) {
            screen.showErrorEmptyFields();
            return;
        }

        screen.hideKeyboard();
        saveUser.saveUser(user, null);

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
                    saveUser.saveUser(user, password);
                    screen.navigateToBalanceWithAnimation(gourmet);
                } else {
                    screen.showErrorNotConnection();
                }
            }
        });
    }

    @Override
    public void notConnection(Gourmet cacheGourmet) {
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
