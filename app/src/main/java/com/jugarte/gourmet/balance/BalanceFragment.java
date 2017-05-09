package com.jugarte.gourmet.balance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.tracker.Tracker;
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

    private boolean displayUpdateIcon;

    BalancePresenter presenter = new BalancePresenter();

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

        setHasOptionsMenu(true);

        // Given data
        if (getArguments() != null && getArguments().getParcelable(ARG_GOURMET) != null) {
            Gourmet gourmet = getArguments().getParcelable(ARG_GOURMET);
            presenter.setGourmet(gourmet);
        } else {
            presenter.login();
        }

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
        presenter.clickCardNumber();
    }

    @Override
    public void showNumberCardSuccess() {
        Toast.makeText(getContext(),
                getResources().getString(R.string.copy_to_clipboard),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void share(String text) {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(
                Intent.createChooser(
                        intent, getResources().getString(R.string.dialog_share_title)
                )
        );
    }

    @Override
    public void showDialogNewVersion(LastVersion lastVersion) {
        LastVersionHelper.showDialog(getActivity(), lastVersion);
    }

    @Override
    public void showUpdateIcon(boolean displayUpdateIcon) {
        this.displayUpdateIcon = displayUpdateIcon;
        setHasOptionsMenu(true);
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
    public void navigateToLogin() {
        MainActivity activity = (MainActivity) getActivity();
        activity.navigateToLogin();
    }

    @Override
    public void navigateToSearch(Gourmet gourmet) {
        startActivity(SearchActivity.newStartIntent(getContext(), gourmet));
    }

    @Override
    public void showError(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGourmetData(Gourmet gourmet) {
        currentText.setVisibility(View.VISIBLE);
        offlineTextView.setVisibility(View.GONE);

        String balance = String.format(getString(R.string.price_euro), gourmet.getCurrentBalance());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_update).setVisible(displayUpdateIcon);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                presenter.clickUpdate();
                break;
            case R.id.action_search:
                presenter.clickSearch();
                break;
            case R.id.action_share_app:
                presenter.clickShare();
                break;
            case R.id.action_open_source:
                presenter.clickOpenSource();
                break;
            case R.id.action_web_site:
                presenter.clickOpenWebSite();
                break;
            case R.id.action_logout:
                presenter.clickLogout();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}