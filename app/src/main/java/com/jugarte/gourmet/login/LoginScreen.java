package com.jugarte.gourmet.login;

import com.jugarte.gourmet.domine.beans.Gourmet;

public interface LoginScreen {

    void navigateToBalanceWithAnimation(Gourmet gourmet);
    void navigateToBalance(Gourmet gourmet);

    void showUser(String user);

    void showLoading();
    void hideLoading();

    void showErrorNotConnection();
    void showErrorEmptyFields();
    void showErrorNotUserFound();

}
