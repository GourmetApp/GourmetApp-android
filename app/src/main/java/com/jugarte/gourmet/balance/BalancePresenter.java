package com.jugarte.gourmet.balance;

import android.content.Context;
import android.widget.Toast;

import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.domine.gourmet.GetGourmet;
import com.jugarte.gourmet.domine.gourmet.SaveGourmet;
import com.jugarte.gourmet.helpers.CredentialsLogin;

public class BalancePresenter implements GetGourmet.OnGourmetResponse {

    private Context context;
    private BalanceScreen screen;

    private final ThreadManager threadManager = new ThreadManagerImp();

    public void bind(Context context, BalanceScreen screen) {
        this.context = context;
        this.screen = screen;
    }

    public void login() {
        final String user = CredentialsLogin.getUserCredential(context);
        final String pass = CredentialsLogin.getPasswordCredential(context);

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


}
