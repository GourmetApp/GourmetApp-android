package com.jugarte.gourmet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jugarte.gourmet.fragments.WearListFragment;

public class MainWearActivity extends FragmentActivity {

    /**********************
     * 					  *
     *		INTERNAL	  *
     *					  *
     **********************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_wear_activity);

        WearListFragment wearLayout = new WearListFragment();
        //WearLayout wearLayout = new WearLayout();
        wearLayout.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(
                android.R.id.content, wearLayout).commit();
    }

}
