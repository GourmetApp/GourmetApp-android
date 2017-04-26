package com.jugarte.gourmet.balance;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;

public interface BalanceScreen {

    void showGourmetData(Gourmet gourmet);

    void showOfflineMode(String modificationDate);

    void navigateToLogin();
    void navigateToSearch(Gourmet gourmet);

    void showError(String text);

    void showLoading(boolean display);

    void openUrl(String url);

    void showNumberCardSuccess();
    void share(String text);

    void showDialogNewVersion(LastVersion lastVersion);

    void showUpdateIcon(boolean display);
}
