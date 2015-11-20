package com.jugarte.gourmet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jugarte.gourmet.lib.R;
import com.jugarte.gourmet.utils.LogUtils;

/**
 * Created by javiergon on 30/06/15.
 */
public abstract class BaseFragment extends Fragment {

    abstract protected void fragmentInit();
    abstract protected int getResourceId();

    /**********************
     * 					  *
     *	    INTERNAL	  *
     *					  *
     **********************/

    /**********************
     * 					  *
     *		PUBLIC 		  *
     *					  *
     **********************/

    public String getParams() {
        if (getArguments() != null) {
            Bundle args = getArguments();
            return args.getString("params");
        } return null;
    }

    public void showLoading(boolean display) {
        View view = getView();
        if (view != null) {
            View loadingView = view.findViewById(R.id.loading_view);
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

    /**********************
     * 					  *
     *	  LIFE CICLE 	  *
     *					  *
     **********************/

    @Override
    public void onStart() {
        super.onStart();
        this.fragmentInit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return this.getLayoutInflater(savedInstanceState).inflate(this.getResourceId(), null);
    }

}
