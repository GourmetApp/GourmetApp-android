package com.jugarte.gourmet.fragments;

import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.activities.MainActivity;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.datamanagers.DataManager;
import com.jugarte.gourmet.helpers.CredentialsLogin;
import com.jugarte.gourmet.utils.ClipboardUtils;
import com.jugarte.gourmet.utils.DisplayUtils;
import com.jugarte.gourmet.utils.ErrorMessageUtils;
import com.jugarte.gourmet.utils.TextFormatUtils;
import com.google.gson.Gson;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;


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

    /**********************
     * 					  *
     *	    INTERNAL	  *
     *					  *
     **********************/
    private void showError(String errorCode) {
        String errorMessage = ErrorMessageUtils.getErrorMessageWithCode(getActivity(), errorCode);
        if (errorCode != null && errorMessage != null) {
            if (errorCode.equalsIgnoreCase("1") || errorCode.equalsIgnoreCase("3")){
                Snackbar.with(getActivity().getApplicationContext())
                        .type(SnackbarType.MULTI_LINE)
                        .text(errorMessage)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .show(getActivity());
            } else {
                Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                logout();
            }
        }
    }

    private void logout() {
        CredentialsLogin.removeCredentials();
        MainActivity activity = (MainActivity) this.getActivity();
        activity.navigateToLogin();
    }

    private void drawLayout(Object result) {
        if (result != null) {
            Gourmet gourmet = (Gourmet) result;
            if (gourmet.errorCode != null && gourmet.errorCode.equals("0")) {
                mCurrentText.setVisibility(View.VISIBLE);
                mCurrentBalance.setText(gourmet.currentBalance + "€");
                String cardNumber = TextFormatUtils.formatCreditCardNumber(CredentialsLogin.getUserCredential());
                if (cardNumber != null) {
                    mCardNumberTextView.setText(cardNumber);
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
        View view = getView();
        mCurrentText = (TextView) view.findViewById(R.id.main_current_text);
        mCurrentBalance = (TextView) view.findViewById(R.id.main_current_balance);
        mLogoutButton = (Button) view.findViewById(R.id.main_logout);
        mOperationsList = (ListView) view.findViewById(R.id.main_operations_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mCardNumberTextView = (TextView) view.findViewById(R.id.main_card_number);

        // AspectRatio 16:9
        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.all_container);
        ViewGroup.LayoutParams lp = container.getLayoutParams();
        Point displayPoint = DisplayUtils.getScreenSize(getActivity());
        lp.height = (int) ((float) displayPoint.x) * 9 / 16;
        container.setLayoutParams(lp);

        if (this.getParams() != null && this.getParams().length() > 0) {
            Gson gson = new Gson();
            this.drawLayout(gson.fromJson(this.getParams(), Gourmet.class));
        } else {
            showLoading(true);
            new DataAsyncTask().execute();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DataAsyncTask().execute();
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
     *	  LIFE CICLE 	  *
     *					  *
     **********************/

    /**********************
     * 				      *
     *		ADAPTER		  *
     *					  *
     **********************/
    private class DataAsyncTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... _void) {
            String user = CredentialsLogin.getUserCredential();
            String pass = CredentialsLogin.getPasswordCredential();
            DataManager dm = new DataManager();
            return dm.login(user, pass);
        }

        @Override
        protected void onPostExecute(Object result) {
            MainFragment.this.drawLayout(result);
            showLoading(false);
            MainFragment.this.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}