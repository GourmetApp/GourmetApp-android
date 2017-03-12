package com.jugarte.gourmet.balance;

import com.jugarte.gourmet.beans.Gourmet;

public interface BalanceScreen {

    void showOfflineMode();

    void showGourmetData(Gourmet gourmet);

    void showLoading(boolean display);

    void openUrl(String url);

    void shareText(String text);

}
