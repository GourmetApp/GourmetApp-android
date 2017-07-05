package com.jugarte.gourmet.fragments;

import android.content.Context;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.domine.beans.Gourmet;
import com.jugarte.gourmet.views.WearListView;
import com.google.gson.Gson;
import com.kogitune.wearhttp.WearGetText;


/**
 * Created by javiergon on 30/05/15.
 */
public class WearGridFragment extends BaseFragment {

    private void drawCotent(String response) {
        View view = getView();
        if (response == null) return;
        Gson gson = new Gson();
        Gourmet gourmet = gson.fromJson(response, Gourmet.class);
        if (gourmet != null) {
            GridViewPager pager = (GridViewPager) view.findViewById(R.id.wear_pager);
            pager.setAdapter(new MyGridPagerAdapter(gourmet));
            DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) view.findViewById(R.id.wear_page_indicator);
            dotsPageIndicator.setPager(pager);
        }
    }

    private void drawError() {
        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void fragmentInit(View view) {
        new WearGetText(this.getActivity()).get("http://jugarte.es/api/gourmet/gourmet.php",
                new WearGetText.WearGetCallBack() {
                    @Override
                    public void onGet(String result) {
                        WearGridFragment.this.drawCotent(result);
                    }

                    @Override
                    public void onFail(final Exception e) {
                        WearGridFragment.this.drawError();
                    }
                });
    }

    @Override
    protected int getResourceId() {
        return R.layout.wear_grid_fragment;
    }


    private class MyGridPagerAdapter extends GridPagerAdapter {

        private Gourmet mGourmet = null;

        public MyGridPagerAdapter (Gourmet gourmet) {
            mGourmet = gourmet;
        }

        private View getCurrentBalanceView(ViewGroup viewGroup, String currentBalance) {
            Context context = WearGridFragment.this.getActivity();
            View view = LayoutInflater.from(context).inflate(R.layout.wear_cbalance_view, viewGroup, false);
            TextView currentBalanceTV = (TextView) view.findViewById(R.id.wear_current_balance);
            if (currentBalance != null) {
                currentBalanceTV.setText(currentBalance + "€");
                viewGroup.addView(view);
            }
            return view;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return 2;
        }

        @Override
        public int getCurrentColumnForRow(int row, int currentColumn) {
            return currentColumn;
        }

        @Override
        protected Object instantiateItem(ViewGroup viewGroup, int row, int col) {
            if (col == 0)
                return this.getCurrentBalanceView(viewGroup, mGourmet.currentBalance);
            else {
                WearListView wearList = new WearListView(WearGridFragment.this.getActivity());
                wearList.setDatas(mGourmet.operations);
                viewGroup.addView(wearList);
                return wearList;
            }
        }

        @Override
        protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object o) {
            viewGroup.removeView((View) o);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }
    }
}
