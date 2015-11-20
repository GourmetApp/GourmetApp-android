package com.jugarte.gourmet.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.datamanagers.GitHubRequest;
import com.jugarte.gourmet.datamanagers.ServiceRequest;
import com.jugarte.gourmet.fragments.LoginFragment;
import com.jugarte.gourmet.fragments.MainFragment;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.helpers.VolleySingleton;

public class MainActivity extends ActionBarActivity {

    /**********************
     * 					  *
     *		INTERNAL	  *
     *					  *
     **********************/

    private void checkNewVersion() {
        GitHubRequest gitHubRequest = new GitHubRequest();
        gitHubRequest.setContext(this);
        gitHubRequest.setResponseListener(new ServiceRequest.Listener<LastVersion>() {
            @Override
            public void onResponse(LastVersion lastVersion) {
                boolean isEqualsVersion = LastVersionHelper.isEqualsVersion(
                        lastVersion.nameTagVersion,
                        LastVersionHelper.getCurrentVersion(MainActivity.this));

                if (!isEqualsVersion) {
                    LastVersionHelper.showDialog(MainActivity.this, lastVersion);
                }

            }
        });

        gitHubRequest.launchConnection();
    }

    /**********************
     * 					  *
     *		PUBLIC 		  *
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
     *	   OVERRIDE	      *
     *					  *
     **********************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        VolleySingleton.getVolleyLoader().initializeVolley(this);

        CredentialsLogin.setActivity(this);
        if (savedInstanceState == null) {
            if (CredentialsLogin.isCredential()) {
                navigateToMain(null);
            } else {
                navigateToLogin();
            }
        }

        // Check a new version
        checkNewVersion();
    }

}