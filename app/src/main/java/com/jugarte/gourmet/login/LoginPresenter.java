package com.jugarte.gourmet.login;

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

public class LoginPresenter {

    private Context context;
    private LoginScreen screen;

    void bind(Context context, LoginScreen screen) {
        this.context = context;
        this.screen = screen;
    }

    void login(final String user, final String password) {

        CredentialsLogin.saveCredential(user, context);
        if (user == null || user.length() == 0 || password == null || password.length() == 0) {
            screen.showError("1");
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setContext(context);
        loginRequest.setQueryParams(new HashMap<String, String>(3) {{
            put(Constants.SERVICE_PARAM_USER_KEY, user);
            put(Constants.SERVICE_PARAM_PASS_KEY, password);
            put(Constants.SERVICE_PARAM_TOKEN_KEY, Constants.SERVICE_PARAM_TOKEN_RESPONSE);
        }});

        loginRequest.setResponseListener(new ServiceRequest.Listener<Gourmet>() {
            @Override
            public void onResponse(Gourmet gourmet) {
                screen.hideLoading();
                if (gourmet != null) {
                    if (gourmet.getErrorCode() != null && gourmet.getErrorCode().equalsIgnoreCase("0")) {

                        screen.saveCredentials(user, password);
                        screen.navigateToMain(gourmet);

                    } else {
                        screen.showError(gourmet.getErrorCode());
                    }
                }
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
        screen.showLoading();
    }

}
