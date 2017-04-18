package com.jugarte.gourmet.balance;

import android.content.Context;
import android.widget.Toast;

import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.activities.SearchActivity;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.gourmet.SaveGourmet;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.utils.ClipboardUtils;

public class BalancePresenter implements GetGourmet.OnGourmetResponse {

    private Context context;
    private BalanceScreen screen;

    private final ThreadManager threadManager = new ThreadManagerImp();
    GetUser getUser;

    public void bind(Context context, BalanceScreen screen) {
        this.context = context;
        this.screen = screen;
        getUser = new GetUser(context);
    }

    public void login() {
        final String user = getUser.getUser();
        final String pass = getUser.getPassword();

        if (user == null || user.length() == 0 ||
                pass == null || pass.length() == 0) {
            // TODO: 14/3/17 logout
            // Crear interactor para hacer el logout este debería eliminar las credenciales
            screen.navigateToLogin();
            return;
        }

        screen.showLoading(true);
        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                new GetGourmet().execute(user, pass, BalancePresenter.this);
            }
        });
    }

    public void logout() {
        Tracker.getInstance().sendMenuEvent("logout");
        screen.navigateToLogin();
    }

    @Override
    public void success(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.showLoading(false);
                screen.showGourmetData(gourmet);
                new SaveGourmet().execute(gourmet);
            }
        });
    }

    @Override
    public void notConnection(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                screen.showLoading(false);
                screen.showGourmetData(gourmet);
                screen.showOfflineMode(gourmet.getModificationDate());
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
        Tracker.getInstance().sendMenuEvent("search");
        screen.navigateToSearch();
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
