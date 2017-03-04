package com.jugarte.gourmet.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.javierugarte.searchtoolbar.SearchToolbar;
import com.github.javierugarte.searchtoolbar.SearchToolbarListener;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchToolbarListener {

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
        SearchToolbar searchToolbar = (SearchToolbar) findViewById(R.id.search_toolbar);

        setSupportActionBar(searchToolbar);
        searchToolbar.setSearchToolbarListener(this);
        searchToolbar.setHint(R.string.search_hint);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gourmet = getIntent().getExtras().getParcelable(EXTRA_GOURMET);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }

        OperationsAdapter adapter = new OperationsAdapter(this,
                gourmet.getOperations(), R.layout.operation_cell);
        ListView operationsList = (ListView) findViewById(R.id.search_operation_list);
        operationsList.setAdapter(adapter);

    }

    @Override
    public void onSearch(String keyword) {
        ArrayList<Operation> operations = gourmet.getOperations(keyword);
        OperationsAdapter adapter = new OperationsAdapter(this,
                operations, R.layout.operation_cell);
        ListView operationsList = (ListView) findViewById(R.id.search_operation_list);
        operationsList.setAdapter(adapter);
    }

    @Override
    public void onClear() {
        OperationsAdapter adapter = new OperationsAdapter(this,
                gourmet.getOperations(), R.layout.operation_cell);
        ListView operationsList = (ListView) findViewById(R.id.search_operation_list);
        operationsList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
