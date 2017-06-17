package com.jugarte.gourmet.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.balance.BalanceActivity;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.utils.FourDigitCardFormatWatcher;
import com.jugarte.gourmet.utils.TextFormatUtils;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class LoginActivity extends AppCompatActivity implements LoginScreen {

    @BindView(R.id.login_user)
    EditText userEditText;
    @BindView(R.id.login_pass)
    EditText passEditText;
    @BindView(R.id.btn_circular_progress_button)
    CircularProgressButton btnLogin;

    LoginPresenter presenter = new LoginPresenter();

    public static Intent newStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        ButterKnife.bind(this);
        userEditText.addTextChangedListener(new FourDigitCardFormatWatcher());
        presenter.bind(this, this);

    }

    private void launchLogin() {

        String user = TextFormatUtils.formatRemoveSpaces(userEditText.getText().toString());
        String pass = passEditText.getText().toString();
        presenter.login(user, pass);

    }

    @Override
    public void navigateToBalanceWithAnimation(final Gourmet gourmet) {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(this, R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_done));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToBalance(gourmet);
            }
        }, 1000);

    }

    @Override
    public void navigateToBalance(Gourmet gourmet) {
        startActivity(BalanceActivity.newStartIntent(this, gourmet));
    }

    @Override
    public void showUser(String user) {
        userEditText.setText(user);
        passEditText.requestFocus();
    }

    @Override
    public void showLoading() {
        btnLogin.startAnimation();
    }

    @Override
    public void hideLoading() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnLogin.revertAnimation();
            }
        }, 1000);
    }

    @Override
    public void showErrorNotConnection() {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(this, R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error));
        Toast.makeText(this, R.string.error_connection_code3, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorEmptyFields() {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(this, R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error));
        Toast.makeText(this, R.string.error_not_user_or_pass_code1, Toast.LENGTH_SHORT).show();
    }

    @Override

    public void showErrorNotUserFound() {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(this, R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error));
        Toast.makeText(this, R.string.error_user_or_password_incorrect_code2, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_circular_progress_button)
    public void loginClick() {
        launchLogin();
    }

    @OnEditorAction(R.id.login_pass)
    public boolean loginAction() {
        launchLogin();
        return false;
    }

}