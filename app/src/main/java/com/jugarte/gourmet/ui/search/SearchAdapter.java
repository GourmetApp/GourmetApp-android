package com.jugarte.gourmet.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jugarte.gourmet.R;

import java.util.Comparator;

public class SearchAdapter extends SortedListAdapter<GourmetViewModel> {

    public SearchAdapter(Context context, Comparator<GourmetViewModel> comparator) {
        super(context, GourmetViewModel.class, comparator);
    }

    @NonNull
    @Override
    protected ViewHolder<GourmetViewModel> onCreateViewHolder(@NonNull LayoutInflater inflater,
                                                              @NonNull ViewGroup viewGroup, int i) {
        final View itemView = inflater.inflate(R.layout.row_search, viewGroup, false);
        return new GourmetViewHolder(itemView);
    }
}
