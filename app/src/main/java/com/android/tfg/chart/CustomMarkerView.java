package com.android.tfg.chart;

import android.content.Context;
import android.widget.TextView;

import com.android.tfg.R;
import com.android.tfg.databinding.MarkerViewBinding;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private TextView label;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        label = (TextView) findViewById(R.id.label);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        label.setText(String.valueOf(e.getY()));

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }
        return mOffset;
    }

}
