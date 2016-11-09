package com.jugarte.gourmet.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.activities.SearchActivity;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.helpers.GourmetSqliteHelper;
import com.jugarte.gourmet.helpers.LastVersionHelper;
import com.jugarte.gourmet.requests.GitHubRequest;
import com.jugarte.gourmet.requests.LoginRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.internal.Constants;
import com.jugarte.gourmet.tracker.Crash;
import com.jugarte.gourmet.tracker.Tracker;
import com.jugarte.gourmet.utils.ClipboardUtils;
import com.jugarte.gourmet.utils.DisplayUtils;
import com.jugarte.gourmet.utils.ErrorMessageUtils;
import com.jugarte.gourmet.utils.TextFormatUtils;
import com.google.gson.Gson;

import java.util.HashMap;


/**
 * Created by javiergon on 15/05/15.
 */
public class MainFragment extends BaseFragment {

    /**********************
     * 					  *
     *	  PROPERTIES	  *
     *					  *
     **********************/
    private TextView mCurrentBalance = null;
    private TextView mCurrentText = null;
    private ListView mOperationsList = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mCardNumberTextView = null;
    private TextView mOfflineTextView = null;
    private RelativeLayout mContainer = null;

    private boolean isEqualsVersion = false;

    Gourmet gourmet = null;

    /**********************
     * 					  *
     *	    INTERNAL	  *
     *					  *
     **********************/

    private void bindingView() {
        View view = getView();

        if (view != null) {
            mCurrentText = (TextView) view.findViewById(R.id.main_current_text);
            mCurrentBalance = (TextView) view.findViewById(R.id.main_current_balance);
            mOperationsList = (ListView) view.findViewById(R.id.main_operations_list);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh_layout);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
            mCardNumberTextView = (TextView) view.findViewById(R.id.main_card_number);
            mOfflineTextView = (TextView) view.findViewById(R.id.main_offline_text_view);

            // AspectRatio 16:9
            mContainer = (RelativeLayout) view.findViewById(R.id.all_container);
        }
    }
    private void showError(String errorCode) {
        String errorMessage = ErrorMessageUtils.getErrorMessageWithCode(getContext(), errorCode);
        if (errorCode != null && getView() != null) {
            if (errorCode.equalsIgnoreCase("1") || errorCode.equalsIgnoreCase("3")){
                // Snackbar
                Snackbar snackbar = Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_INDEFINITE);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
                snackbar.setAction(R.string.button_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading(true);
                        loginRequest();
                    }
                });
            } else {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                logout();
            }
        }

        Tracker.getInstance().sendLoginResult(Tracker.Param.ERROR, errorMessage);
    }

    private void drawLayout(Gourmet gourmet) {
        this.gourmet = gourmet;
        if (gourmet != null) {

            if (gourmet.getErrorCode() != null && gourmet.getErrorCode().equals("0")) {

                Tracker.getInstance().sendLoginResult(Tracker.Param.OK);

                mCurrentText.setVisibility(View.VISIBLE);
                String currentBalance = gourmet.getCurrentBalance() + "€";
                mCurrentBalance.setText(currentBalance);
                String cardNumber = TextFormatUtils.formatCreditCardNumber(gourmet.getCardNumber());
                mCardNumberTextView.setText(cardNumber);

                if (gourmet.isOfflineMode() && gourmet.getModificationDate() != null) {
                    mOfflineTextView.setVisibility(View.VISIBLE);
                    String offlineText = String.format(getString(R.string.offline_modification), gourmet.getModificationDate());
                    mOfflineTextView.setText(offlineText);
                } else {
                    mOfflineTextView.setVisibility(View.GONE);
                }

                OperationsAdapter adapter = new OperationsAdapter(getActivity(), gourmet.getOperations(), R.layout.operation_cell);
                mOperationsList.setAdapter(adapter);
            } else {
                showError(gourmet.getErrorCode());
            }
        } else {
            showError("3");
        }
    }

    private void loginRequest() {
        final String user = CredentialsLogin.getUserCredential(getContext());
        final String pass = CredentialsLogin.getPasswordCredential(getContext());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setContext(getContext());
        loginRequest.setQueryParams(new HashMap<String, String>(3) {{
            put(Constants.SERVICE_PARAM_USER_KEY, user);
            put(Constants.SERVICE_PARAM_PASS_KEY, pass);
            put(Constants.SERVICE_PARAM_TOKEN_KEY, Constants.SERVICE_PARAM_TOKEN_RESPONSE);
        }});

        loginRequest.setResponseListener(new ServiceRequest.Listener<Gourmet>() {
            @Override
            public void onResponse(Gourmet gourmet) {
                showLoading(false);
                drawLayout(gourmet);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        loginRequest.setOnErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Tracker.getInstance().sendLoginResult(Tracker.Param.ERROR, "Volley error");
                Crash.report(error);
            }
        });

        loginRequest.launchConnection();
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

    private void logout() {
        CredentialsLogin.removeCredentials(getContext());
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(getContext());
        sqliteHelper.resetTables();

        MainActivity activity = (MainActivity) getActivity();
        activity.navigateToLogin();
    }

    /**********************
     * 					  *
     *		PUBLIC 		  *
     *					  *
     **********************/

    /**********************
     *					  *
     *		OVERRIDE	  *
     *					  *
     **********************/

    @Override
    protected void fragmentInit() {
        bindingView();

        // Set 16:9 the view
        ViewGroup.LayoutParams lp = mContainer.getLayoutParams();
        Point displayPoint = DisplayUtils.getScreenSize(getActivity());
        lp.height = (int) ((float) displayPoint.x) * 9 / 16;
        mContainer.setLayoutParams(lp);

        // Given data
        if (getParams() != null && getParams().length() > 0) {
            Gson gson = new Gson();
            Gourmet gourmet = gson.fromJson(getParams(), Gourmet.class);
            drawLayout(gourmet);
        } else {
            showLoading(true);
            loginRequest();
        }

        checkNewVersion();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loginRequest();
            }
        });

        mCardNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(getContext(),
                        CredentialsLogin.getUserCredential(getContext()));

                Toast.makeText(getContext(),
                        getResources().getString(R.string.copy_to_clipboard),
                        Toast.LENGTH_SHORT).show();

                Tracker.getInstance().sendMenuEvent("copy_clipboard");
            }
        });

    }

    @Override
    protected int getResourceId() {
        return R.layout.main_fragment;
    }

    /**********************
     * 					  *
     *	  LIFE CYCLE 	  *
     *					  *
     **********************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        return view;
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