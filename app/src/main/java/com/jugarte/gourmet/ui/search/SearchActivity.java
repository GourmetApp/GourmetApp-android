package com.jugarte.gourmet.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.javierugarte.searchtoolbar.SearchToolbar;
import com.github.javierugarte.searchtoolbar.SearchToolbarListener;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.Operation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchToolbarListener {

    private static final String EXTRA_GOURMET = "extraGourmet";

    @BindView(R.id.search_toolbar)
    SearchToolbar searchToolbar;

    @BindView(R.id.search_operation_list)
    ListView operationsList;

    @BindView(R.id.no_result)
    TextView noResult;

    private SearchAdapter adapter;
    private Gourmet gourmet;

    public static Intent newStartIntent(Context context, Gourmet gourmet) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_GOURMET, gourmet);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        ButterKnife.bind(this);

        setSupportActionBar(searchToolbar);
        searchToolbar.setSearchToolbarListener(this);
        searchToolbar.setHint(R.string.search_hint);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gourmet = getIntent().getExtras().getParcelable(EXTRA_GOURMET);

        if (gourmet == null) {
            showNoResult(true);
            return;
        }

        adapter = new SearchAdapter(this,
                gourmet.getOperations(), R.layout.operation_item);

        operationsList.setAdapter(adapter);

    }

    @Override
    public void onSearch(String keyword) {
        reloadList(keyword);
    }

    @Override
    public void onClear() {
        reloadList("");
    }

    private void reloadList(String keyword) {
        List<Operation> operations = gourmet.getOperations(keyword);
        showNoResult(operations.isEmpty());
        adapter.setOperations(operations);
    }

    private void showNoResult(boolean display) {
        int visibility = display ? View.VISIBLE : View.GONE;
        noResult.setVisibility(visibility);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
