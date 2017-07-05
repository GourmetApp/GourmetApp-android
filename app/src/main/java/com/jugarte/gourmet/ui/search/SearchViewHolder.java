package com.jugarte.gourmet.ui.search;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jugarte.gourmet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class SearchViewHolder extends SortedListAdapter.ViewHolder<SearchViewModel> {

    @BindView(R.id.tv_operation_name)
    TextView nameTextView;

    @BindView(R.id.tv_operation_date)
    TextView dateTextView;

    @BindView(R.id.tv_operation_price)
    TextView priceTextView;

    SearchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void performBind(@NonNull SearchViewModel item) {
        nameTextView.setText(item.getName());

        dateTextView.setText(item.getDate());

        String symbol = isPositiveOperation(item.getName()) ? "%s" : "-%s";
        String price = String.format("%s€", item.getPrice());
        priceTextView.setText(String.format(symbol, price));
    }

    private boolean isPositiveOperation(String name) {
        return name.contains(" de saldo");
    }

}
