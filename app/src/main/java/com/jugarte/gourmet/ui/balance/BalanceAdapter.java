package com.jugarte.gourmet.ui.balance;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;
import com.jugarte.gourmet.R;
import com.jugarte.gourmet.ui.balance.model.MonthVM;
import com.jugarte.gourmet.ui.balance.model.OperationVM;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class BalanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {

    private static final int TYPE_MONTH = 0;
    private static final int TYPE_OPERATION = 1;

    private Context context;
    private List<Object> operations;

    BalanceAdapter(Context context, List<Object> operations) {
        this.context = context;
        this.operations = operations;
    }

    @Override
    public int getItemViewType(int position) {
        return operations.get(position) instanceof OperationVM ? TYPE_OPERATION : TYPE_MONTH;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_OPERATION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operation_item, parent, false);
            return new OperationViewHolder(view);
        } else if (viewType == TYPE_MONTH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_item, parent, false);
            return new MonthViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OperationViewHolder) {
            ((OperationViewHolder) holder).bind((OperationVM) operations.get(position));
        } else if (holder instanceof MonthViewHolder) {
            ((MonthViewHolder) holder).bind(((MonthVM) operations.get(position)).getMonth());
        }
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    @Override
    public List<?> getAdapterData() {
        return operations;
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_month)
        TextView monthTextView;

        MonthViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String month) {
            monthTextView.setText(month);
        }
    }

    class OperationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_operation_name)
        TextView nameTextView;

        @BindView(R.id.tv_operation_date)
        TextView dateTextView;

        @BindView(R.id.tv_operation_price)
        TextView priceTextView;

        @BindView(R.id.im_operation_image)
        ImageView imageView;

        OperationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(OperationVM operation) {
            nameTextView.setText(operation.getTitle());
            dateTextView.setText(operation.getDate());
            priceTextView.setText(operation.getPrice());
            int resColor = operation.getState() == OperationVM.State.POSITIVE
                    ? R.color.text_operation_price_positive
                    : R.color.text_operation_price_negative;

            int resImage = operation.getState() == OperationVM.State.POSITIVE
                    ? R.drawable.ic_cash_in
                    : R.drawable.ic_cash_out;

            @ColorInt int color = ContextCompat.getColor(context, resColor);
            priceTextView.setTextColor(color);
            imageView.setImageResource(resImage);
        }
    }
}
