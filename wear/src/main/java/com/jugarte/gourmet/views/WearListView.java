package com.jugarte.gourmet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jugarte.gourmet.R;
import com.jugarte.gourmet.adapters.OperationsAdapter;
import com.jugarte.gourmet.beans.Operation;

import java.util.ArrayList;

public class WearListView extends RelativeLayout {

    private Context mContext;

    public WearListView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public WearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WearListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        View v = LayoutInflater.from(mContext).inflate(R.layout.wear_list_view, null);
        this.addView(v);
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    private void drawList(ArrayList<Operation> datas) {
        ListView listOperations = (ListView) this.findViewById(R.id.wear_list_operations);
        OperationsAdapter adapter = new OperationsAdapter(mContext, datas, R.layout.wear_cell);
        listOperations.setAdapter(adapter);
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param datas The example drawable attribute value to use.
     */
    public void setDatas(ArrayList<Operation> datas) {
        this.drawList(datas);
    }

}
