package com.jugarte.gourmet.ui.login;

import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.domine.user.SaveUser;
import com.jugarte.gourmet.ui.base.BasePresenter;

import javax.inject.Inject;

public class LoginPresenter<V extends LoginScreen> extends BasePresenter<V>
        implements GetGourmet.OnGourmetResponse {

    private String user, password;

    private final SaveUser saveUser;
    private final GetUser getUser;

    private final ThreadManager threadManager;

    @Inject
    public LoginPresenter(GetUser getUser, SaveUser saveUser, ThreadManager threadManager) {
        this.getUser = getUser;
        this.saveUser = saveUser;
        this.threadManager = threadManager;
    }

    @Override
    public void onAttach(V screen) {
        super.onAttach(screen);

        if (getUser.isLogged()) {
            getScreen().navigateToBalance(null);
        }

        getScreen().showUser(getUser.getUser());
    }

    void login(final String user, final String password) {
        this.user = user;
        this.password = password;

        if (user == null || user.length() == 0
                || password == null || password.length() == 0) {
            getScreen().showErrorEmptyFields();
            return;
        }

        getScreen().hideKeyboard();
        saveUser.saveUser(user, null);

        getScreen().showLoading();
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
                getScreen().hideLoading();
                if (gourmet != null) {
                    saveUser.saveUser(user, password);
                    getScreen().navigateToBalanceWithAnimation(gourmet);
                } else {
                    getScreen().showErrorNotConnection();
                }
            }
        });
    }

    @Override
    public void notConnection(Gourmet cacheGourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                getScreen().hideLoading();
                getScreen().showErrorNotConnection();
            }
        });
    }

    @Override
    public void notUserFound() {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                getScreen().hideLoading();
                getScreen().showErrorNotUserFound();
            }
        });
    }

}
