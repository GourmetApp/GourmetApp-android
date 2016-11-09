package com.jugarte.gourmet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.fragments.LoginFragment;
import com.jugarte.gourmet.fragments.MainFragment;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.VolleySingleton;
import com.jugarte.gourmet.tracker.Tracker;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Tracker.getInstance(getApplicationContext());

        VolleySingleton.getVolleyLoader().initializeVolley(this);

        if (savedInstanceState == null) {
            if (CredentialsLogin.isCredential(getApplicationContext())) {
                navigateToMain(null);
            } else {
                navigateToLogin();
            }
        }

    }

    public void navigateToLogin() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(
                android.R.id.content, loginFragment).commit();
    }

    public void navigateToMain(Gourmet gourmet) {
        MainFragment mainFragment = new MainFragment();
        if (gourmet != null) {
            Bundle bundleParams = new Bundle();
            bundleParams.putParcelable(MainFragment.ARG_GOURMET, gourmet);
            mainFragment.setArguments(bundleParams);
        }
        getSupportFragmentManager().beginTransaction().add(
                android.R.id.content, mainFragment).commit();
    }

    //region menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //endregion

}