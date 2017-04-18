package com.jugarte.gourmet.login;

import com.jugarte.gourmet.beans.Gourmet;

public interface LoginScreen {

    void navigateToMain(Gourmet gourmet);

    void showUser(String user);

    void showLoading();
    void hideLoading();

    void showErrorNotConnection();
    void showErrorEmptyFields();
    void showErrorNotUserFound();

}
