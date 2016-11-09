package com.jugarte.gourmet.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Gourmet;

/**
 * Created by javiergon on 15/05/15.
 */

public class SearchActivity extends AppCompatActivity {

    private static final String EXTRA_GOURMET = "extraGourmet";

    private Gourmet gourmet;

    public static Intent newStartIntent(Context context, Gourmet gourmet) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_GOURMET, gourmet);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gourmet = getIntent().getExtras().getParcelable(EXTRA_GOURMET);

        OperationsAdapter adapter = new OperationsAdapter(this,
                gourmet.getOperations(), R.layout.operation_cell);
        ListView operationsList = (ListView) findViewById(R.id.search_operation_list);
        operationsList.setAdapter(adapter);

    }

}
