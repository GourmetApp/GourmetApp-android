package com.jugarte.gourmet.login;

import com.jugarte.gourmet.beans.Gourmet;

public interface LoginScreen {

    void navigateToMain(Gourmet gourmet);
    void showLoading();
    void hideLoading();
    void showError(String errorCode);

    void saveCredentials(String user, String password);
}
