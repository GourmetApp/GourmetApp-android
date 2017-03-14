package com.jugarte.gourmet.login;

import com.jugarte.gourmet.beans.Gourmet;

public interface LoginScreen {

    void navigateToMain(Gourmet gourmet);
    void showLoading();
    void hideLoading();
    void showErrorNotConnection();
    void showErrorEmptyFields();
    void showErrorNotUserFound();

    void saveCredentials(String user, String password);
}
