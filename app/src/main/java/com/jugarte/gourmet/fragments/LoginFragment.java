package com.jugarte.gourmet.fragments;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.requests.LoginRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.tracker.Crash;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.utils.ErrorMessageUtils;

import java.util.HashMap;

public class LoginFragment extends BaseFragment {

    private EditText mUserEditText = null;
    private EditText mPassEditText = null;
    private CheckBox mPassRemember = null;
    private Button mLoginButton= null;

    private void bindingViews(View view) {
        if (view != null) {
            mUserEditText = (EditText) view.findViewById(R.id.login_user);
            mPassEditText = (EditText) view.findViewById(R.id.login_pass);
            mPassRemember = (CheckBox) view.findViewById(R.id.login_remember_password);
            mPassRemember = (CheckBox) view.findViewById(R.id.login_remember_password);
            mLoginButton =  (Button)   view.findViewById(R.id.login_button);
        }
    }

    private void showError(String errorCode) {
        if (errorCode != null) {
            String errorMessage = ErrorMessageUtils.getErrorMessageWithCode(getActivity(), errorCode);
            Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            Tracker.getInstance().sendLoginResult(Tracker.Param.ERROR, errorMessage);
        }
    }

    private void saveCredentials(String user, String pass) {
        CredentialsLogin.removeCredentials(getContext());
        CredentialsLogin.saveCredentials(user, pass, mPassRemember.isChecked(), getContext());
    }

    private void launchLogin() {
        String user = mUserEditText.getText().toString().replaceAll(" ", "");
        String pass = mPassEditText.getText().toString();
        CredentialsLogin.saveCredential(user, getContext());
        if (user != null && user.length() > 0 && pass != null && pass.length() > 0) {
            loginRequest(user, pass);
        } else {
            showError("1");
        }
    }

    private void loginRequest(final String user, final String pass) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setContext(getContext());
        loginRequest.setQueryParams(new HashMap<String, String>(3){{
            put(Constants.SERVICE_PARAM_USER_KEY, user);
            put(Constants.SERVICE_PARAM_PASS_KEY, pass);
            put(Constants.SERVICE_PARAM_TOKEN_KEY, Constants.SERVICE_PARAM_TOKEN_RESPONSE);
        }});

        loginRequest.setResponseListener(new ServiceRequest.Listener<Gourmet>() {
            @Override
            public void onResponse(Gourmet gourmet) {
                showLoading(false);
                if (gourmet != null) {
                    if (gourmet.getErrorCode() != null && gourmet.getErrorCode().equalsIgnoreCase("0")) {

                        Tracker.getInstance().sendLoginResult(Tracker.Param.OK);

                        LoginFragment.this.saveCredentials(user, pass);

                        MainActivity activity = (MainActivity) getActivity();
                        activity.navigateToMain(gourmet);
                    } else {
                        showError(gourmet.getErrorCode());
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
        showLoading(true);
    }

    @Override
    protected void fragmentInit(View view) {
        bindingViews(view);

        mUserEditText.addTextChangedListener(new FourDigitCardFormatWatcher());

        if (CredentialsLogin.getUserCredential(getContext()) != null) {
            mUserEditText.setText(CredentialsLogin.getUserCredential(getContext()));
            mPassEditText.requestFocus();
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchLogin();
            }
        });

        mPassEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                launchLogin();
                return false;
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.login_fragment;
    }

    public static class FourDigitCardFormatWatcher implements TextWatcher {

        private static final char space = ' ';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove all spacing char
            int pos = 0;
            while (pos < s.length()) {
                if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                    s.delete(pos, pos + 1);
                } else {
                    pos++;
                }
            }

            // Insert char where needed.
            pos = 4;
            while (pos < s.length()) {
                final char c = s.charAt(pos);
                // Only if its a digit where there should be a space we insert a space
                if ("0123456789".indexOf(c) >= 0) {
                    s.insert(pos, "" + space);
                }
                pos += 5;
            }
        }
    }

}