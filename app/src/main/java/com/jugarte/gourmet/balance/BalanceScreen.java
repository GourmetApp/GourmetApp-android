package com.jugarte.gourmet.balance;

import com.jugarte.gourmet.beans.Gourmet;

public interface BalanceScreen {

    void showGourmetData(Gourmet gourmet);

    void showOfflineMode(String modificationDate);

    void navigateToLogin();
    void navigateToSearch();

    void showError();

    void showLoading(boolean display);

    void openUrl(String url);

    void showNumberCardSuccess();
    void share(String text);

}
