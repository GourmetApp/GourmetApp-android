package com.jugarte.gourmet.ui.login;

import com.jugarte.gourmet.ui.base.Presenter;

public interface LoginPresenter<V extends LoginScreen> extends Presenter<V> {

    void login(String user, String password);

}
