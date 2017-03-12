package com.jugarte.gourmet.balance;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jugarte.gourmet.ThreadManager;
import com.jugarte.gourmet.ThreadManagerImp;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.domine.GetGourmet;
import com.jugarte.gourmet.helpers.CredentialsLogin;
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

        threadManager.runOnBackground(new Runnable() {
            @Override
            public void run() {
                new GetGourmet().execute(user, pass, BalancePresenter.this);
            }
        });

        screen.showLoading(true);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setContext(context);
        loginRequest.setQueryParams(new HashMap<String, String>(3) {{
            put(Constants.SERVICE_PARAM_USER_KEY, user);
            put(Constants.SERVICE_PARAM_PASS_KEY, pass);
            put(Constants.SERVICE_PARAM_TOKEN_KEY, Constants.SERVICE_PARAM_TOKEN_RESPONSE);
        }});

        loginRequest.setResponseListener(new ServiceRequest.Listener<Gourmet>() {
            @Override
            public void onResponse(Gourmet gourmet) {
                screen.showLoading(false);
//                screen.showGourmetData(gourmet);
            }
        });

        loginRequest.setOnErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Tracker.getInstance().sendLoginResult(Tracker.Param.ERROR, "Volley error");
                Crash.report(error);
            }
        });

        loginRequest.launchConnection();
    }

    @Override
    public void success(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                // TODO: 12/3/17 añadir la fecha de modificación
                // TODO: 12/3/17 guardar los datos en firebase de nuevo 
                screen.showGourmetData(gourmet);
            }
        });
    }

    @Override
    public void notConnection(final Gourmet gourmet) {
        threadManager.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "notConnection", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "notUserFound", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
