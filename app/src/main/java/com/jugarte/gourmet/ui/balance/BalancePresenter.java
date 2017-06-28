package com.jugarte.gourmet.ui.balance;

import android.content.Context;

import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.ui.base.Presenter;

public interface BalancePresenter<V extends BalanceScreen> extends Presenter<V> {

    void bind(Context context);

    void setGourmet(Gourmet gourmet);

    void login();

    void logout();

    void clickCardNumber();

    void clickUpdate();

    void clickSearch();

    void clickShare();

    void clickOpenSource();

    void clickOpenWebSite();

    void clickLogout();

}
