package com.jugarte.gourmet.login;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.utils.FourDigitCardFormatWatcher;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class LoginFragment extends Fragment implements LoginScreen {

    @BindView(R.id.login_user)
    EditText userEditText;
    @BindView(R.id.login_pass)
    EditText passEditText;
    @BindView(R.id.btn_circular_progress_button)
    CircularProgressButton btnLogin;

    LoginPresenter presenter = new LoginPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.login_fragment, null);

        ButterKnife.bind(this, view);

        userEditText.addTextChangedListener(new FourDigitCardFormatWatcher());

        presenter.bind(getContext(), this);

        return view;
    }

    private void launchLogin() {
        String user = userEditText.getText().toString().replaceAll(" ", "");
        String pass = passEditText.getText().toString();
        presenter.login(user, pass);

    }

    @Override
    public void navigateToMain(final Gourmet gourmet) {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(getContext(), R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_done));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity activity = (MainActivity) getActivity();
                activity.navigateToMain(gourmet);
            }
        }, 1000);

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
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(getContext(), R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error));
        Toast.makeText(getContext(), R.string.error_connection_code3, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorEmptyFields() {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(getContext(), R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error));
        Toast.makeText(getContext(), R.string.error_not_user_or_pass_code1, Toast.LENGTH_SHORT).show();
    }

    @Override

    public void showErrorNotUserFound() {
        btnLogin.doneLoadingAnimation(ContextCompat.getColor(getContext(), R.color.accent),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error));
        Toast.makeText(getContext(), R.string.error_user_or_password_incorrect_code2, Toast.LENGTH_SHORT).show();
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