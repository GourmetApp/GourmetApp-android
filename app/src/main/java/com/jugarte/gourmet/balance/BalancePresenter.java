package com.jugarte.gourmet.balance;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.requests.LoginRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.tracker.Crash;
import com.jugarte.gourmet.tracker.Tracker;

import java.util.HashMap;

public class BalancePresenter {

    private Context context;
    private BalanceScreen screen;

    public void bind(Context context, BalanceScreen screen) {
        this.context = context;
        this.screen = screen;
    }

    public void login() {
        final String user = CredentialsLogin.getUserCredential(context);
        final String pass = CredentialsLogin.getPasswordCredential(context);

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
                screen.showGourmetData(gourmet);
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

}
