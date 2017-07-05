package com.jugarte.gourmet.ui.login;

import com.jugarte.gourmet.domine.beans.Gourmet;

public interface LoginScreen {

    void navigateToBalanceWithAnimation(Gourmet gourmet);
    void navigateToBalance(Gourmet gourmet);

    void showUser(String user);

    void showLoading();
    void hideLoading();

    void hideKeyboard();

    void showErrorNotConnection();
    void showErrorEmptyFields();
    void showErrorNotUserFound();

}
