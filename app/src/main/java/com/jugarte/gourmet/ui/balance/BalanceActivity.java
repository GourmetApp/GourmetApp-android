package com.jugarte.gourmet.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.ui.balance.model.BalanceVM;
import com.jugarte.gourmet.ui.base.BaseActivity;
import com.jugarte.gourmet.ui.login.LoginActivity;
import com.jugarte.gourmet.ui.search.SearchActivity;
import com.jugarte.gourmet.utils.TextFormatUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BalanceActivity extends BaseActivity implements BalanceScreen {

    public static final String EXTRA_GOURMET = "EXTRA_GOURMET";

    @BindView(R.id.balance_current_balance)
    TextView currentBalance;

    @BindView(R.id.balance_current_text)
    TextView currentText;

    @BindView(R.id.balance_operations_list)
    RecyclerView operationsList;

    @BindView(R.id.balance_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.balance_card_number)
    TextView cardNumberTextView;

    @BindView(R.id.balance_offline_text_view)
    TextView offlineTextView;

    private boolean displayUpdateIcon;

    @Inject
    BalancePresenter<BalanceScreen> presenter;

    public static Intent newStartIntent(Context context, Gourmet gourmet) {
        Intent intent = new Intent(context, BalanceActivity.class);
        intent.putExtra(EXTRA_GOURMET, gourmet);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_activity);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        init(getIntent());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.login();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        init(intent);
    }

    private void init(Intent intent) {
        if (intent != null &&
                intent.getExtras() != null &&
                intent.getExtras().getParcelable(EXTRA_GOURMET) != null) {
            Gourmet gourmet = intent.getExtras().getParcelable(EXTRA_GOURMET);
            presenter.setGourmet(gourmet);
        } else {
            presenter.login();
        }
    }

    @OnClick(R.id.balance_card_number)
    public void cardNumberClick() {
        presenter.clickCardNumber();
    }

    @Override
    public void showNumberCardSuccess() {
        Toast.makeText(getApplicationContext(),
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
        LastVersionHelper.showDialog(this, lastVersion);
    }

    @Override
    public void showUpdateIcon(boolean displayUpdateIcon) {
        this.displayUpdateIcon = displayUpdateIcon;
        invalidateOptionsMenu();
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
        startActivity(LoginActivity.newStartIntent(this));
        finish();
    }

    @Override
    public void navigateToSearch(Gourmet gourmet) {
        startActivity(SearchActivity.newStartIntent(this, gourmet));
    }

    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGourmetData(BalanceVM balance) {
        currentText.setVisibility(View.VISIBLE);
        offlineTextView.setVisibility(View.GONE);

        currentBalance.setText(balance.getCurrent());

        String cardNumber = TextFormatUtils.formatCreditCardNumber(balance.getCardNumber());
        cardNumberTextView.setText(cardNumber);
        
        BalanceAdapter adapter = new BalanceAdapter(getApplicationContext(), balance.getOperations());
        operationsList.setAdapter(adapter);
        operationsList.setLayoutManager(new StickyLayoutManager(getApplicationContext(), adapter));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (menu.findItem(R.id.action_update) != null) {
            menu.findItem(R.id.action_update).setVisible(displayUpdateIcon);
        }
        return true;
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

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
