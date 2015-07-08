package com.jugarte.gourmet.fragments;


import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.datamanagers.DataManager;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.google.gson.Gson;


/**
 * Created by javiergon on 06/05/15.
 */
public class LoginFragment extends BaseFragment {

    private EditText mUserEditText = null;
    private EditText mPassEditText = null;
    private CheckBox mPassRemember = null;

    private void showError(String errorCode, String errorMessage) {
        if (errorCode != null && errorMessage != null) {
            Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCredentials(String user, String pass) {
        CredentialsLogin.removeCredentials();
        if (mPassRemember.isChecked()) {
            CredentialsLogin.saveCredentials(user, pass);
        }
    }

    private void launchLogin() {
        String user = mUserEditText.getText().toString().replaceAll(" ", "");
        String pass = mPassEditText.getText().toString();
        CredentialsLogin.saveCredential(user);
        if (user != null && user.length() > 0 && pass != null && pass.length() > 0) {
            new LoginTask().execute(user, pass);
        } else {
            LoginFragment.this.showError("1", "Enter card number and password");
        }
    }

    @Override
    protected void fragmentInit() {
        View view = getView();
        this.mUserEditText = (EditText) view.findViewById(R.id.login_user);
        this.mPassEditText = (EditText) view.findViewById(R.id.login_pass);
        this.mPassRemember= (CheckBox) view.findViewById(R.id.login_remember_password);
        this.mUserEditText.addTextChangedListener(new FourDigitCardFormatWatcher());

        if (CredentialsLogin.getUserCredential() != null) {
            mUserEditText.setText(CredentialsLogin.getUserCredential());
            mPassEditText.requestFocus();
        }

        Button btnLogin = (Button) view.findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {

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

    private class LoginTask extends AsyncTask<String, Void, Object> {

        private String mUser = null;
        private String mPass = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(true);
        }

        protected Object doInBackground(String... params) {
            if (params.length == 2) {
                this.mUser = params[0];
                this.mPass = params[1];
                DataManager dm = new DataManager();
                return dm.login(this.mUser, this.mPass);
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            showLoading(false);
            if (result != null) {
                Gourmet gourmet = (Gourmet) result;
                if (gourmet != null && gourmet.errorCode != null && gourmet.errorCode.equalsIgnoreCase("0")) {
                    LoginFragment.this.saveCredentials(this.mUser, this.mPass);
                    Gson gson = new Gson();
                    String response = gson.toJson(result);
                    MainActivity activity = (MainActivity) LoginFragment.this.getActivity();
                    activity.navigateToMain(response);
                } else {
                    showError(gourmet.errorCode, gourmet.errorMessage);
                }
            }
        }
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