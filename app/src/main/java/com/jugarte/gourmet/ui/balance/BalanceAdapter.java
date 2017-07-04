package com.jugarte.gourmet.ui.balance;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.domine.beans.Operation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.OperationViewHolder> {

    private List<Operation> operations;

    BalanceAdapter(List<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public OperationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operation_item, parent, false);
        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperationViewHolder holder, int position) {
        holder.bind(operations.get(position));
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    class OperationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.operation_name)
        TextView nameTextView;

        @BindView(R.id.operation_date)
        TextView dateTextView;

        @BindView(R.id.operation_price)
        TextView priceTextView;

        OperationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Operation operation) {
            nameTextView.setText(operation.getName());

            dateTextView.setText(operation.getDate() + " " + operation.getHour());

            String symbol = isPositiveOperation(operation.getName()) ? "%s" : "-%s";
            String price = String.format("%s€", operation.getPrice());
            priceTextView.setText(String.format(symbol, price));
        }

        private boolean isPositiveOperation(String name) {
            return name.contains(" de saldo");
        }
    }
}
