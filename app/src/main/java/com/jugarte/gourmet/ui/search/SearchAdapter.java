package com.jugarte.gourmet.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jugarte.gourmet.R;

import java.util.Comparator;

class SearchAdapter extends SortedListAdapter<SearchViewModel> {

    SearchAdapter(Context context, Comparator<SearchViewModel> comparator) {
        super(context, SearchViewModel.class, comparator);
    }

    @NonNull
    @Override
    protected ViewHolder<SearchViewModel> onCreateViewHolder(@NonNull LayoutInflater inflater,
                                                             @NonNull ViewGroup viewGroup, int i) {
        final View itemView = inflater.inflate(R.layout.search_item, viewGroup, false);
        return new SearchViewHolder(itemView);
    }
}
