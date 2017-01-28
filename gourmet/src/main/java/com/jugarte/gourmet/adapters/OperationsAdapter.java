package com.jugarte.gourmet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.lib.R;

import java.util.ArrayList;

/**
 * Created by javiergon on 30/05/15.
 */
public class OperationsAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<Operation> mOperations = null;
    private int mResourceId = 0;

    public OperationsAdapter(Context context, ArrayList<Operation> transactions, int resourceId) {
        this.mContext = context;
        this.mOperations = transactions;
        this.mResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return this.mOperations.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mOperations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void applyStyles (ContentHolder holder) {
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ContentHolder h = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResourceId, parent, false);

            h = new ContentHolder();
            h.date = (TextView) view.findViewById(R.id.date_operation);
            h.name = (TextView) view.findViewById(R.id.name_operation);
            h.price = (TextView) view.findViewById(R.id.price_operation);
            this.applyStyles(h);
            view.setTag(h);
        } else {
            h = (ContentHolder) view.getTag();
        }

        Operation operation = (Operation)this.getItem(position);
        if (operation == null)
            return view;

        h.date.setText(operation.getDate() + " " + operation.getHour());
        h.name.setText(operation.getName());
        String price = String.format(mContext.getString(R.string.price_euro), operation.getPrice());
        h.price.setText(price);

        return view;
    }

    /// Content Holder
    private class ContentHolder {
        private TextView date = null;
        private TextView name = null;
        private TextView price = null;
    }
}