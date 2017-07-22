package com.jugarte.gourmet.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImpl;
import com.jugarte.gourmet.data.prefs.AppPreferencesHelper;
import com.jugarte.gourmet.data.prefs.PreferencesHelper;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.domine.user.RemoveUser;
import com.jugarte.gourmet.domine.user.SaveUser;
import com.jugarte.gourmet.ui.balance.BalancePresenter;
import com.jugarte.gourmet.ui.balance.BalancePresenterImpl;
import com.jugarte.gourmet.ui.balance.BalanceScreen;
import com.jugarte.gourmet.ui.login.LoginPresenter;
import com.jugarte.gourmet.ui.login.LoginPresenterImpl;
import com.jugarte.gourmet.ui.login.LoginScreen;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    Context provideContext() {
        return activity.getApplicationContext();
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    LoginPresenter<LoginScreen> provideLoginPresenter(GetUser getUser, SaveUser saveUser, ThreadManager threadManager) {
        return new LoginPresenterImpl(getUser, saveUser, threadManager);
    }

    @Provides
    BalancePresenter<BalanceScreen> provideBalancePresenter(Context context,
                                                            GetGourmet getGourmet,
                                                            GetUser getUser, RemoveUser removeUser,
                                                            ThreadManager threadManager) {
        return new BalancePresenterImpl(context, getGourmet, getUser, removeUser, threadManager);
    }

    @Provides
    GetGourmet provideGetGourmet() {
        return new GetGourmet();
    }

    @Provides
    GetUser provideGetUser(PreferencesHelper preferencesHelper) {
        return new GetUser(preferencesHelper);
    }

    @Provides
    SaveUser provideSaveUser(PreferencesHelper preferencesHelper) {
        return new SaveUser(preferencesHelper);
    }

    @Provides
    RemoveUser provideRemoveUser(PreferencesHelper preferencesHelper) {
        return new RemoveUser(preferencesHelper);
    }

    @Provides
    PreferencesHelper getPreferenceHelper(Context context) {
        return new AppPreferencesHelper(context);
    }

    @Provides
    ThreadManager provideThreadManager() {
        return new ThreadManagerImpl();
    }

}
