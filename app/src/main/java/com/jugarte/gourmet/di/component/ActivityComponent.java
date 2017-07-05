package com.jugarte.gourmet.di.component;

import com.jugarte.gourmet.di.module.ActivityModule;
import com.jugarte.gourmet.ui.balance.BalanceActivity;
import com.jugarte.gourmet.ui.login.LoginActivity;
import com.jugarte.gourmet.ui.search.SearchActivity;

import dagger.Component;

@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity activity);

    void inject(BalanceActivity activity);

    void inject(SearchActivity activity);

}
