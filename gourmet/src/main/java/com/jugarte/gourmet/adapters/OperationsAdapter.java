package com.jugarte.gourmet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jugarte.gourmet.domine.beans.Operation;
import com.jugarte.gourmet.lib.R;

import java.util.List;

public class OperationsAdapter extends BaseAdapter {

    private Context context = null;
    private List<Operation> operations = null;
    private int resourceId = 0;

    public OperationsAdapter(Context context, List<Operation> operations, int resourceId) {
        this.context = context;
        this.operations = operations;
        this.resourceId = resourceId;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.operations.size();
    }

    @Override
    public Object getItem(int position) {
        return this.operations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void applyStyles(ContentHolder holder) {
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ContentHolder h = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, parent, false);

            h = new ContentHolder();
            h.date = (TextView) view.findViewById(R.id.date_operation);
            h.name = (TextView) view.findViewById(R.id.name_operation);
            h.price = (TextView) view.findViewById(R.id.price_operation);
            this.applyStyles(h);
            view.setTag(h);

        } else {
            h = (ContentHolder) view.getTag();
        }

        Operation operation = (Operation) this.getItem(position);
        if (operation == null)
            return view;

        h.date.setText(operation.getDate() + " " + operation.getHour());
        h.name.setText(operation.getName());
        String symbol = isPositiveOperation(operation.getName()) ? "%s" : "-%s";

        String price = String.format(context.getString(R.string.price_euro), operation.getPrice());
        h.price.setText(String.format(symbol, price));

        return view;
    }

    private boolean isPositiveOperation(String name) {
        return name.contains(" de saldo");
    }

    /// Content Holder
    private class ContentHolder {
        private TextView date = null;
        private TextView name = null;
        private TextView price = null;
    }
}