package com.jugarte.gourmet.balance;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.domine.GetGourmet;
import com.jugarte.gourmet.domine.SaveGourmet;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.DateHelper;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.requests.LoginRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.tracker.Crash;
import com.jugarte.gourmet.tracker.Tracker;

import java.util.HashMap;

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
            // TODO: 12/3/17 Marcar error
//            screen.showError();
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
