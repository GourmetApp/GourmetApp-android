package com.jugarte.gourmet.balance;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.activities.SearchActivity;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.requests.GitHubRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.utils.ClipboardUtils;
import com.jugarte.gourmet.utils.DisplayUtils;
import com.jugarte.gourmet.utils.ErrorMessageUtils;
import com.jugarte.gourmet.utils.TextFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BalanceFragment extends Fragment implements BalanceScreen {

    public static final String ARG_GOURMET = "ARG_GOURMET";

    @BindView(R.id.balance_current_balance) TextView currentBalance;
    @BindView(R.id.balance_current_text) TextView currentText;
    @BindView(R.id.balance_operations_list) ListView operationsList;
    @BindView(R.id.balance_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.balance_card_number) TextView cardNumberTextView;
    @BindView(R.id.balance_offline_text_view) TextView offlineTextView;
    @BindView(R.id.all_container) RelativeLayout balanceRL;

    private boolean isEqualsVersion = false;

    BalancePresenter presenter = new BalancePresenter();
    Gourmet gourmet = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreateView(inflater, viewGroup, savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.balance_fragment, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ButterKnife.bind(this, view);
        presenter.bind(getContext(), this);

        // Set 16:9 the view
        ViewGroup.LayoutParams lp = balanceRL.getLayoutParams();
        Point displayPoint = DisplayUtils.getScreenSize(getActivity());
        lp.height = (int) ((float) displayPoint.x) * 9 / 16;
        balanceRL.setLayoutParams(lp);

        // Given data
        if (getArguments() != null && getArguments().getParcelable(ARG_GOURMET) != null) {
            Gourmet gourmet = getArguments().getParcelable(ARG_GOURMET);
            showGourmetData(gourmet);
        } else {
            presenter.login();
        }

        checkNewVersion();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.login();
            }
        });

        return view;
    }

    @OnClick(R.id.balance_card_number)
    public void cardNumberClick() {
        ClipboardUtils.copyToClipboard(getContext(),
                CredentialsLogin.getUserCredential(getContext()));

        Toast.makeText(getContext(),
                getResources().getString(R.string.copy_to_clipboard),
                Toast.LENGTH_SHORT).show();

        Tracker.getInstance().sendMenuEvent("copy_clipboard");
    }

    private void showError(String errorCode) {
        String errorMessage = ErrorMessageUtils.getErrorMessageWithCode(getContext(), errorCode);
        if (errorCode != null && getView() != null) {
            if (errorCode.equalsIgnoreCase("1") || errorCode.equalsIgnoreCase("3")) {
                Snackbar snackbar = Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_INDEFINITE);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
                snackbar.setAction(R.string.button_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.login();
                    }
                });
            } else {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                logout();
            }
        }

        Tracker.getInstance().sendLoginResult(Tracker.Param.ERROR, errorMessage);
    }

    private void checkNewVersion() {
        GitHubRequest gitHubRequest = new GitHubRequest();
        gitHubRequest.setContext(getContext());
        gitHubRequest.setResponseListener(new ServiceRequest.Listener<LastVersion>() {
            @Override
            public void onResponse(LastVersion lastVersion) {

                if (lastVersion != null && lastVersion.getNameTagVersion() != null) {

                    isEqualsVersion = LastVersionHelper.isEqualsVersion(
                            lastVersion.getNameTagVersion(),
                            LastVersionHelper.getCurrentVersion(getContext()));

                    boolean shouldShowDialog = LastVersionHelper.shouldShowDialog(
                            lastVersion.getNameTagVersion(), getContext());

                    if (!isEqualsVersion && shouldShowDialog) {
                        LastVersionHelper.showDialog(getActivity(), lastVersion);
                    }

                    setHasOptionsMenu(true);

                }
            }
        });

        gitHubRequest.launchConnection();
    }

    @Override
    public void showOfflineMode(String modificationDate) {
        if (modificationDate != null) {
            offlineTextView.setVisibility(View.VISIBLE);
            String offlineText = String.format(getString(R.string.offline_modification), modificationDate);
            offlineTextView.setText(offlineText);
        } else {
            offlineTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGourmetData(Gourmet gourmet) {
        currentText.setVisibility(View.VISIBLE);
        offlineTextView.setVisibility(View.GONE);
        String balance = gourmet.getCurrentBalance() + "€";
        currentBalance.setText(balance);
        String cardNumber = TextFormatUtils.formatCreditCardNumber(gourmet.getCardNumber());
        cardNumberTextView.setText(cardNumber);

        OperationsAdapter adapter = new OperationsAdapter(getActivity(), gourmet.getOperations(), R.layout.operation_cell);
        operationsList.setAdapter(adapter);

        Tracker.getInstance().sendLoginResult(Tracker.Param.OK);
    }

    @Override
    public void showLoading(boolean display) {
        swipeRefreshLayout.setRefreshing(display);
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
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.dialog_share_title)));
    }

    private void logout() {
        CredentialsLogin.removeCredentials(getContext());

        MainActivity activity = (MainActivity) getActivity();
        activity.navigateToLogin();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isEqualsVersion) {
            menu.findItem(R.id.action_update).setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                Tracker.getInstance().sendMenuEvent("download");
                openUrl(Constants.getUrlHomePage());
                break;
            case R.id.action_search:
                Tracker.getInstance().sendMenuEvent("search");
                startActivity(SearchActivity.newStartIntent(getContext(), gourmet));
                break;
            case R.id.action_share_app:
                Tracker.getInstance().sendMenuEvent("share");
                shareText(Constants.getShareText(getActivity()));
                break;
            case R.id.action_open_source:
                Tracker.getInstance().sendMenuEvent("open_source");
                openUrl(Constants.getUrlGitHubProject());
                break;
            case R.id.action_web_site:
                Tracker.getInstance().sendMenuEvent("web_site");
                openUrl(Constants.getUrlHomePage());
                break;
            case R.id.action_logout:
                Tracker.getInstance().sendMenuEvent("logout");
                logout();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}