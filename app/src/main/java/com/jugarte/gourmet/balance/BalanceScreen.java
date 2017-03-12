package com.jugarte.gourmet.balance;

import com.jugarte.gourmet.beans.Gourmet;

public interface BalanceScreen {

    void showGourmetData(Gourmet gourmet);

    void showOfflineMode(String modificationDate);

    void showLoading(boolean display);

    void openUrl(String url);

    void shareText(String text);

}
