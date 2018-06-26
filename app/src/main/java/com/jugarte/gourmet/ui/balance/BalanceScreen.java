package com.jugarte.gourmet.ui.balance;

import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.ui.balance.model.BalanceVM;

public interface BalanceScreen {

    void showGourmetData(BalanceVM balance);
    void updateGourmetData(BalanceVM balance);

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
