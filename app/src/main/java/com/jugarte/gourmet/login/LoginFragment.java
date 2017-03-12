package com.jugarte.gourmet.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.utils.ErrorMessageUtils;
import com.jugarte.gourmet.utils.FourDigitCardFormatWatcher;
import com.jugarte.gourmet.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class LoginFragment extends Fragment implements LoginScreen {

    @BindView(R.id.login_user) EditText userEditText;
    @BindView(R.id.login_pass) EditText passEditText;
    @BindView(R.id.login_remember_password) CheckBox passRemember;

    LoginPresenter presenter = new LoginPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.login_fragment, null);

        ButterKnife.bind(this, view);
        presenter.bind(getContext(), this);

        userEditText.addTextChangedListener(new FourDigitCardFormatWatcher());

        if (CredentialsLogin.getUserCredential(getContext()) != null) {
            userEditText.setText(CredentialsLogin.getUserCredential(getContext()));
            passEditText.requestFocus();
        }

        return view;
    }

    private void launchLogin() {
        String user = userEditText.getText().toString().replaceAll(" ", "");
        String pass = passEditText.getText().toString();
        presenter.login(user, pass);
    }

    @Override
    public void navigateToMain(Gourmet gourmet) {
        MainActivity activity = (MainActivity) getActivity();
        activity.navigateToMain(gourmet);
    }

    @Override
    public void showLoading() {
        showLoading(getView(), true);
    }

    @Override
    public void hideLoading() {
        showLoading(getView(), false);
    }

    @Override
    public void showError(String errorCode) {
        if (errorCode != null) {
            String errorMessage = ErrorMessageUtils.getErrorMessageWithCode(getActivity(), errorCode);
            Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();

            Tracker.getInstance().sendLoginResult(Tracker.Param.ERROR, errorMessage);
        }
    }

    @Override
    public void saveCredentials(String user, String password) {
        CredentialsLogin.removeCredentials(getContext());
        CredentialsLogin.saveCredentials(user, password, passRemember.isChecked(), getContext());
    }

    @OnClick(R.id.login_button)
    public void loginClick() {
        launchLogin();
    }

    @OnEditorAction(R.id.login_pass)
    public boolean loginAction() {
        launchLogin();
        return false;
    }

    public void showLoading(View view, boolean display) {
        if (view != null) {
            View loadingView = view.findViewById(com.jugarte.gourmet.lib.R.id.loading_view);
            int displayView = (display) ? View.VISIBLE : View.GONE;
            if (loadingView != null) {
                loadingView.setVisibility(displayView);
            } else {
                LogUtils.LOGE(this.getClass().getCanonicalName(), "View not found");
            }
        } else {
            LogUtils.LOGE(this.getClass().getCanonicalName(), "View not found");
        }
    }

}