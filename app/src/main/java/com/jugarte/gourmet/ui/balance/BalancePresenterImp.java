package com.jugarte.gourmet.ui.balance;

import android.content.Context;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.gourmet.SaveGourmet;
import com.jugarte.gourmet.domine.newversion.CheckNewVersion;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.domine.user.RemoveUser;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.ui.base.BasePresenter;
import com.jugarte.gourmet.utils.ClipboardUtils;

import javax.inject.Inject;

public class BalancePresenterImp<V extends BalanceScreen> extends BasePresenter<V>
        implements BalancePresenter<V>, GetGourmet.OnGourmetResponse, CheckNewVersion.OnCheckNewVersion {

    private final ThreadManager threadManager;
    private final Context context;

    private GetGourmet getGourmet;

    private GetUser getUser;
    private SaveGourmet saveGourmet;
    private RemoveUser removeUser;

    private Gourmet gourmet;

    @Inject
    public BalancePresenterImp(Context context,
                               GetGourmet getGourmet, SaveGourmet saveGourmet,
                               GetUser getUser, RemoveUser removeUser,
                               ThreadManager threadManager) {
        this.context = context;
        this.getGourmet = getGourmet;
        this.getUser = getUser;
        this.saveGourmet = saveGourmet;
        this.removeUser = removeUser;
        this.threadManager = threadManager;
    }

    @Override
    public void onAttach(V screen) {
        super.onAttach(screen);
        checkNewVersion();
    }

    @Override
    public void setGourmet(Gourmet gourmet) {
        this.gourmet = gourmet;
        if (gourmet != null) {
            getScreen().showGourmetData(gourmet);
        }
    }

    @Override
    public void login() {
        final String user = getUser.getUser();
        final String pass = getUser.getPassword();

        if (user == null || user.length() == 0 ||
                pass == null || pass.length() == 0) {
            logout();
            return;
        }

        getScreen().showLoading(true);
        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                getGourmet.execute(user, pass, BalancePresenterImp.this);
            }
        });
    }

    @Override
    public void logout() {
        removeUser.removeUser();

        Tracker.getInstance().sendMenuEvent("logout");
        getScreen().navigateToLogin();
    }

    @Override
    public void success(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                getScreen().showLoading(false);
                setGourmet(gourmet);
                saveGourmet.execute(gourmet);
            }
        });
    }

    @Override
    public void notConnection(final Gourmet cacheGourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                getScreen().showLoading(false);
                setGourmet(cacheGourmet);
                getScreen().showOfflineMode(cacheGourmet.getModificationDate());
            }
        });
    }

    @Override
    public void notUserFound() {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                getScreen().showLoading(false);
                getScreen().showError(context.getString(R.string.error_user_or_password_incorrect_code2));
            }
        });
    }

    private void checkNewVersion() {
        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                new CheckNewVersion().execute(BalancePresenterImp.this);
            }
        });
    }

    @Override
    public void newVersion(final LastVersion lastVersion) {
        if (lastVersion != null && lastVersion.getNameTagVersion() != null) {

            final boolean isEqualsVersion = LastVersionHelper.isEqualsVersion(
                    lastVersion.getNameTagVersion(),
                    LastVersionHelper.getCurrentVersion(context));

            boolean shouldShowDialog = LastVersionHelper.shouldShowDialog(
                    lastVersion.getNameTagVersion(), context);

            if (!isEqualsVersion && shouldShowDialog) {
                threadManager.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        getScreen().showDialogNewVersion(lastVersion);
                    }
                });
            }

            threadManager.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    getScreen().showUpdateIcon(!isEqualsVersion);
                }
            });
        }
    }

    @Override
    public void clickCardNumber() {
        ClipboardUtils.copyToClipboard(context,
                getUser.getUser());

        Tracker.getInstance().sendMenuEvent("copy_clipboard");

        getScreen().showNumberCardSuccess();
    }

    @Override
    public void clickUpdate() {
        Tracker.getInstance().sendMenuEvent("download");
        getScreen().openUrl(Constants.getUrlHomePage());
    }

    @Override
    public void clickSearch() {
        if (gourmet == null) {
            getScreen().showError(context.getString(R.string.search_error));
            return;
        }

        Tracker.getInstance().sendMenuEvent("search");
        getScreen().navigateToSearch(gourmet);
    }

    @Override
    public void clickShare() {
        Tracker.getInstance().sendMenuEvent("share");
        getScreen().share(Constants.getShareText(context));
    }

    @Override
    public void clickOpenSource() {
        Tracker.getInstance().sendMenuEvent("open_source");
        getScreen().openUrl(Constants.getUrlGitHubProject());
    }

    @Override
    public void clickOpenWebSite() {
        Tracker.getInstance().sendMenuEvent("web_site");
        getScreen().openUrl(Constants.getUrlHomePage());
    }

    @Override
    public void clickLogout() {
        Tracker.getInstance().sendMenuEvent("logout");
        logout();
    }

}
