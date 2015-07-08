package com.jugarte.gourmet.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.fragments.LoginFragment;
import com.jugarte.gourmet.fragments.MainFragment;
import com.jugarte.gourmet.helpers.CredentialsLogin;

public class MainActivity extends ActionBarActivity {

    /**********************
     * 					  *
     *		INTERNAL	  *
     *					  *
     **********************/
    public void navigateToLogin() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(
                android.R.id.content, loginFragment).commit();
    }

    public void navigateToMain(String params) {
        MainFragment mainFragment = new MainFragment();
        if (params != null && params.length() > 0) {
            Bundle bundleParams = new Bundle();
            bundleParams.putString("params", params);
            mainFragment.setArguments(bundleParams);
        }
        getSupportFragmentManager().beginTransaction().add(
                android.R.id.content, mainFragment).commit();
    }

    /**********************
     * 					  *
     *		PUBLIC 		  *
     *					  *
     **********************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        CredentialsLogin.setActivity(this);
        if (savedInstanceState == null) {
            if (CredentialsLogin.isCredential()) {
                navigateToMain(null);
            } else {
                navigateToLogin();
            }
        }
    }

}