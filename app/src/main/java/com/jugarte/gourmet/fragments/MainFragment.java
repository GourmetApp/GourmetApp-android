package com.jugarte.gourmet.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.requests.LoginRequest;
import com.jugarte.gourmet.requests.ServiceRequest;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.helpers.GourmetSqliteHelper;
import com.jugarte.gourmet.internal.Constants;
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
    private Button mLogoutButton = null;
    private ListView mOperationsList = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mCardNumberTextView = null;
    private TextView mOfflineTextView = null;
    private RelativeLayout mContainer = null;


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
            mLogoutButton = (Button) view.findViewById(R.id.main_logout);
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
                Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                logout();
            }
        }
    }

    private void logout() {
        CredentialsLogin.removeCredentials();
        GourmetSqliteHelper sqliteHelper = new GourmetSqliteHelper(getActivity().getApplicationContext());
        sqliteHelper.resetTables();
        MainActivity activity = (MainActivity) this.getActivity();
        activity.navigateToLogin();
    }

    private void drawLayout(Object result) {
        if (result != null) {
            Gourmet gourmet = (Gourmet) result;
            if (gourmet.errorCode != null && gourmet.errorCode.equals("0")) {
                mCurrentText.setVisibility(View.VISIBLE);
                mCurrentBalance.setText(gourmet.currentBalance + "€");
                String cardNumber = TextFormatUtils.formatCreditCardNumber(gourmet.cardNumber);
                mCardNumberTextView.setText(cardNumber);

                if (gourmet.offlineMode && gourmet.modificationDate != null) {
                    mOfflineTextView.setVisibility(View.VISIBLE);
                    String offlineText = String.format(getString(R.string.offline_modification), gourmet.modificationDate);
                    mOfflineTextView.setText(offlineText);
                } else {
                    mOfflineTextView.setVisibility(View.GONE);
                }

                OperationsAdapter adapter = new OperationsAdapter(MainFragment.this.getActivity(), gourmet.operations, R.layout.operation_cell);
                mOperationsList.setAdapter(adapter);
            } else {
                this.showError(gourmet.errorCode);
            }
        } else {
            this.showError("3");
        }
    }

    private void loginRequest() {
        final String user = CredentialsLogin.getUserCredential();
        final String pass = CredentialsLogin.getPasswordCredential();
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
                MainFragment.this.drawLayout(gourmet);
                MainFragment.this.mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        loginRequest.setOnErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        loginRequest.launchConnection();
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

        ViewGroup.LayoutParams lp = mContainer.getLayoutParams();
        Point displayPoint = DisplayUtils.getScreenSize(getActivity());
        lp.height = (int) ((float) displayPoint.x) * 9 / 16;
        mContainer.setLayoutParams(lp);

        if (this.getParams() != null && this.getParams().length() > 0) {
            Gson gson = new Gson();
            this.drawLayout(gson.fromJson(this.getParams(), Gourmet.class));
        } else {
            showLoading(true);
            loginRequest();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loginRequest();
            }
        });

        mCardNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(MainFragment.this.getActivity().getApplicationContext(),
                        CredentialsLogin.getUserCredential());
                Toast.makeText(MainFragment.this.getActivity(),
                        getResources().getString(R.string.copy_to_clipboard),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment.this.logout();
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

}