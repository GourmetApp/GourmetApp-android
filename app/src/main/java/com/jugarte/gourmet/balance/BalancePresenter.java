package com.jugarte.gourmet.balance;

import android.content.Context;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.gourmet.SaveGourmet;
import com.jugarte.gourmet.domine.newversion.CheckNewVersion;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.domine.user.RemoveUser;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.utils.ClipboardUtils;

public class BalancePresenter implements GetGourmet.OnGourmetResponse, CheckNewVersion.OnCheckNewVersion {

    private Context context;
    private BalanceScreen screen;

    private final ThreadManager threadManager = new ThreadManagerImp();

    private GetGourmet getGourmet;

    private GetUser getUser;
    private SaveGourmet saveGourmet;
    private RemoveUser removeUser;

    private Gourmet gourmet;

    public void bind(Context context, BalanceScreen screen) {
        this.context = context;
        this.screen = screen;
        getUser = new GetUser(context);
        getGourmet = new GetGourmet();
        saveGourmet = new SaveGourmet();
        removeUser = new RemoveUser(context);

        checkNewVersion();
    }

    public void setGourmet(Gourmet gourmet) {
        this.gourmet = gourmet;
        screen.showGourmetData(gourmet);
    }

    public void login() {
        final String user = getUser.getUser();
        final String pass = getUser.getPassword();

        if (user == null || user.length() == 0 ||
                pass == null || pass.length() == 0) {
            logout();
            return;
        }

        screen.showLoading(true);
        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                getGourmet.execute(user, pass, BalancePresenter.this);
            }
        });
    }

    public void logout() {
        removeUser.execute();

        Tracker.getInstance().sendMenuEvent("logout");
        screen.navigateToLogin();
    }

    @Override
    public void success(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.showLoading(false);
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
                screen.showLoading(false);
                screen.showGourmetData(cacheGourmet);
                screen.showOfflineMode(cacheGourmet.getModificationDate());
            }
        });
    }

    @Override
    public void notUserFound() {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.showLoading(false);
                Toast.makeText(context, "notUserFound", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNewVersion() {
        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                new CheckNewVersion().execute(BalancePresenter.this);
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
                        screen.showDialogNewVersion(lastVersion);
                    }
                });
            }

            threadManager.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    screen.showUpdateIcon(!isEqualsVersion);
                }
            });
        }
    }

    @Override
    public void error() {

    }

    public void clickCardNumber() {
        ClipboardUtils.copyToClipboard(context,
                getUser.getUser());

        Tracker.getInstance().sendMenuEvent("copy_clipboard");

        screen.showNumberCardSuccess();
    }

    public void clickUpdate() {
        Tracker.getInstance().sendMenuEvent("download");
        screen.openUrl(Constants.getUrlHomePage());
    }

    public void clickSearch() {
        if (gourmet == null) {
            screen.showError(context.getString(R.string.search_error));
            return;
        }

        Tracker.getInstance().sendMenuEvent("search");
        screen.navigateToSearch(gourmet);
    }

    public void clickShare() {
        Tracker.getInstance().sendMenuEvent("share");
        screen.share(Constants.getShareText(context));
    }

    public void clickOpenSource() {
        Tracker.getInstance().sendMenuEvent("open_source");
        screen.openUrl(Constants.getUrlGitHubProject());
    }

    public void clickOpenWebSite() {
        Tracker.getInstance().sendMenuEvent("web_site");
        screen.openUrl(Constants.getUrlHomePage());
    }

    public void clickLogout() {
        Tracker.getInstance().sendMenuEvent("logout");
        logout();
    }

}
