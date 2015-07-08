package com.jugarte.gourmet.fragments;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.beans.Operation;
import com.jugarte.gourmet.datamanagers.DataManagerFake;
import com.google.gson.Gson;

/**
 * Created by javiergon on 30/05/15.
 */
public class WearListFragment extends BaseFragment {

    private WearableListView mListView;
    private TextView mCurrentBalance;

    private void drawContent(String response) {
        if (response == null) return;
        Gson gson = new Gson();
        Gourmet gourmet = gson.fromJson(response, Gourmet.class);
        drawContent(gourmet);
    }

    private void drawContent(Gourmet gourmet) {
        View view = getView();
        if (gourmet != null) {
            mCurrentBalance = (TextView)view.findViewById(R.id.wear_current_balance);
            mCurrentBalance.setText(gourmet.currentBalance + "€");

            mListView = (WearableListView) view.findViewById(R.id.wear_list_view);
            mListView.setAdapter(new WearListAdapter(getActivity(), gourmet));
            mListView.addOnScrollListener(new WearableListView.OnScrollListener() {
                @Override
                public void onScroll(int i) {}

                @Override
                public void onAbsoluteScrollChange(int i) {
                    if (i < 93) {
                        mCurrentBalance.setTextSize((5*i-1661)/-46);
                        mCurrentBalance.setY((float)((-5565+45*i)/-92));
                    } else {
                        mCurrentBalance.setY(-i+93+15);
                    }
                }

                @Override
                public void onScrollStateChanged(int i) {}

                @Override
                public void onCentralPositionChanged(int i) {}
            });
        }
    }

    private void drawError() {
        Toast.makeText(this.getActivity(), getResources().getString(R.string.error_not_connexion), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void fragmentInit() {
        /* new WearGetText(getActivity()).get("",
                new WearGetText.WearGetCallBack() {
                    @Override
                    public void onGet(String result) {
                        DataManagerFake dmf = new DataManagerFake();
                        WearListLayout.this.drawContent(dmf.login(null));
                    }

                    @Override
                    public void onFail(final Exception e) {
                        WearListLayout.this.drawError();
                    }
                }); */

        DataManagerFake dmf = new DataManagerFake();
        WearListFragment.this.drawContent(dmf.login(null));
    }

    @Override
    protected int getResourceId() {
        return R.layout.wear_list_fragment;
    }


    // Adapter
    private class WearListAdapter extends WearableListView.Adapter {
        private final LayoutInflater mInflater;
        private final Gourmet mGourmet;

        private WearListAdapter(Context context, Gourmet gourmet) {
            mInflater = LayoutInflater.from(context);
            mGourmet = gourmet;
        }

        @Override
        public int getItemCount() {
            return mGourmet.operations.size()+1;
        }

        public Object getItemObject(int position) {
            if (position != 0)
                return mGourmet.operations.get(position-1);

            return null;
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.wear_cell, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView nameOperation = (TextView) holder.itemView.findViewById(R.id.name_operation);
            TextView dateOperation = (TextView) holder.itemView.findViewById(R.id.date_operation);
            TextView priceOperation = (TextView) holder.itemView.findViewById(R.id.price_operation);

            Operation operation = (Operation) getItemObject(position);
            if (operation != null) {
                nameOperation.setText(operation.name);
                dateOperation.setText(operation.date + " " + operation.hour);
                priceOperation.setText(operation.price + "€");
            } else {
                nameOperation.setText("");
                dateOperation.setText("");
                priceOperation.setText("");
            }

            holder.itemView.setTag(position);
        }

    }

}
