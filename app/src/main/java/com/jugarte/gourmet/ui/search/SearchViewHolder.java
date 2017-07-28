package com.jugarte.gourmet.ui.search;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.ui.balance.model.OperationVM;

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
        int resColor = item.isPositive()
                ? R.color.text_operation_price_positive
                : R.color.text_operation_price_negative;

        @ColorInt int color = ContextCompat.getColor(itemView.getContext(), resColor);

        nameTextView.setText(item.getName());
        dateTextView.setText(item.getDate());
        priceTextView.setText(item.getPrice());
        priceTextView.setTextColor(color);
    }

}
