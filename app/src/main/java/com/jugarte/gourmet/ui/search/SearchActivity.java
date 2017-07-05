package com.jugarte.gourmet.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.javierugarte.searchtoolbar.SearchToolbar;
import com.github.javierugarte.searchtoolbar.SearchToolbarListener;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.domine.beans.Operation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchToolbarListener {

    private static final String EXTRA_GOURMET = "extraGourmet";

    @BindView(R.id.search_toolbar)
    SearchToolbar searchToolbar;

    @BindView(R.id.rv_search)
    RecyclerView searchRecyclerView;

    @BindView(R.id.tv_no_results)
    TextView noResult;

    private SearchAdapter searchAdapter;
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

        Comparator<SearchViewModel> comparator = new Comparator<SearchViewModel>() {
            @Override
            public int compare(SearchViewModel a, SearchViewModel b) {
                return b.getDateObject().compareTo(a.getDateObject());
            }
        };

        searchAdapter = new SearchAdapter(getApplicationContext(), comparator);
        List<SearchViewModel> operations = getModel(gourmet.getOperations());
        searchAdapter.edit().add(operations).commit();
        searchRecyclerView.setAdapter(searchAdapter);
    }

    private List<SearchViewModel> getModel(List<Operation> operations) {
        ArrayList<SearchViewModel> rOperations = new ArrayList<>();
        for (Operation operation : operations) {
            rOperations.add(new SearchViewModel(operation.getId(),
                    operation.getName(),
                    operation.getDate() + " " + operation.getHour(),
                    operation.getPrice()));
        }
        return rOperations;
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

        List<SearchViewModel> gourmetViewModels = getModel(operations);
        searchAdapter.edit().replaceAll(gourmetViewModels).commit();
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
