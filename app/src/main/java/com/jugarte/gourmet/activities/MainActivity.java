package com.jugarte.gourmet.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.balance.BalanceFragment;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.domine.user.GetUser;
import com.jugarte.gourmet.login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        GetUser getUser = new GetUser(getApplicationContext());

        if (savedInstanceState == null) {
            if (getUser.isLogged()) {
                navigateToMain(null);
            } else {
                navigateToLogin();
            }
        }
    }

    public void navigateToLogin() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(
                android.R.id.content, loginFragment).commit();
    }

    public void navigateToMain(Gourmet gourmet) {
        hideKeyboard();

        BalanceFragment mainFragment = new BalanceFragment();
        if (gourmet != null) {
            Bundle bundleParams = new Bundle();
            bundleParams.putParcelable(BalanceFragment.ARG_GOURMET, gourmet);
            mainFragment.setArguments(bundleParams);
        }

        getSupportFragmentManager().beginTransaction().replace(
                android.R.id.content, mainFragment).commit();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}