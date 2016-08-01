package com.jugarte.gourmet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.helpers.GourmetSqliteHelper;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.requests.GitHubRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.fragments.LoginFragment;
import com.jugarte.gourmet.fragments.MainFragment;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.helpers.VolleySingleton;
import com.jugarte.gourmet.tracker.Tracker;

public class MainActivity extends AppCompatActivity {

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

    public void logout() {
        CredentialsLogin.removeCredentials();
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(getApplicationContext());
        sqliteHelper.resetTables();
        navigateToLogin();
    }

    public void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void shareText(String textToShare) {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, textToShare);
        startActivity(Intent.createChooser(intent ,getResources().getString(R.string.dialog_share_title)));
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

        Tracker.getInstance(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share_app:
                shareText(Constants.getShareText(this));
                break;
            case R.id.action_open_source:
                openUrl(Constants.getUrlGitHubProject());
                break;
            case R.id.action_web_site:
                openUrl(Constants.getUrlHomePage());
                break;
            case R.id.action_logout:
                logout();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}