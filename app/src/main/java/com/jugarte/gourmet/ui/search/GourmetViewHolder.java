package com.jugarte.gourmet.ui.search;

import android.view.View;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.jugarte.gourmet.R;

class GourmetViewHolder extends SortedListAdapter.ViewHolder<GourmetViewModel> {

    private final TextView nameTextView;
    private final TextView dateTextView;
    private final TextView priceTextView;

    public GourmetViewHolder(View itemView) {
        super(itemView);

        nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
        dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
        priceTextView = (TextView) itemView.findViewById(R.id.tv_price);
    }

    @Override
    protected void performBind(GourmetViewModel item) {
        nameTextView.setText(item.getName());
        dateTextView.setText(item.getDate());
        priceTextView.setText(item.getPrice());
    }
}
