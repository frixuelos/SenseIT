package com.android.tfg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.model.MessageModel;
import com.android.tfg.viewmodel.MoreViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class UVChartFragment extends Fragment {

    MoreViewModel moreViewModel;
    LineChart chart;

    public UVChartFragment(){

    }

    private void setData(LinkedList<MessageModel> messages) {

        /**************************************
         * CONVIERTE MENSAJES A VALORES (X,Y) *
         **************************************/
        ArrayList<Entry> uv = new ArrayList<>();
        for (MessageModel m : messages) {
            uv.add(new Entry(m.getDate(), (float) m.getUv()));
        }

        /*****************
         * SERIE PRESION *
         *****************/
        LineDataSet serieUV = new LineDataSet(uv, getString(R.string.uvTitle));
        serieUV.setAxisDependency(YAxis.AxisDependency.LEFT);
        serieUV.enableDashedLine(50, 10, 0);
        serieUV.setColor(Color.BLACK);
        serieUV.setValueTextColor(Color.BLACK);
        serieUV.setLineWidth(2F);
        serieUV.setDrawCircles(true);
        serieUV.setDrawValues(true);
        serieUV.setCircleColor(Color.BLACK);
        serieUV.setDrawCircleHole(false);
        serieUV.setFillDrawable(Objects.requireNonNull(getView()).getResources().getDrawable(R.drawable.gradient_uv));
        serieUV.setHighlightEnabled(false);
        serieUV.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData uvData = new LineData(serieUV);
        uvData.setValueTextColor(Color.BLACK);
        uvData.setValueTextSize(12f);
        // Formatter para mostrar solo 2 decimales
        uvData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(Math.round(value*100.0)/100.0);
            }
        });

        // Agregar datos
        chart.setData(uvData);

        // Actualizar grafico
        chart.invalidate();

        // Visible 2 ultimas horas
        chart.setVisibleXRangeMaximum(2 * 60 * 60);
        chart.moveViewToX(new Date().getTime() - (2 * 60 * 60));

        // Animar
        chart.animateX(500);
    }

    private void setupChart(View v){
        chart= v.findViewById(R.id.uvChart);
        chart.getDescription().setTextSize(24f);
        chart.getDescription().setText(getString(R.string.uvTitle));

        // descripcion
        chart.getDescription().setEnabled(false);
        // para las acciones al pulsar
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);
        // escalado y arrastre
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setScaleYEnabled(true);
        chart.setHorizontalScrollBarEnabled(true);
        chart.setDrawGridBackground(true);
        chart.setHighlightPerDragEnabled(true);
        chart.setPinchZoom(true);
        chart.setNoDataTextColor(Color.BLACK);
        chart.setNoDataText(getString(R.string.moreNoData));
        // fondo
        chart.setGridBackgroundColor(Color.TRANSPARENT);
        // animacion
        chart.animateY(1000);
         // leyenda
        Legend l = chart.getLegend();
        l.setEnabled(false);

       /*********
        * EJE X *
        *********/
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.enableGridDashedLine(10,5,0);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setValueFormatter(new ValueFormatter() {
        private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(new Date((long) value * 1000L));
            }
        });
        xAxis.setEnabled(true);

        /*********
         * EJE Y *
         *********/
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(false);
        leftAxis.enableGridDashedLine(10,5,0);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(getActivity()).get("charts",MoreViewModel.class);
        final Observer<LinkedList<MessageModel>> obs = new Observer<LinkedList<MessageModel>>() {
            @Override
            public void onChanged(LinkedList<MessageModel> messages) {
                // añadir datos al grafico
                setData(messages);
            }
        };
        moreViewModel.getMessages().observe(getViewLifecycleOwner(), obs); // mensajes
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uv, container, false);

        /***********
         * GRAFICO *
         ***********/
        setupChart(v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        super.onActivityCreated(savedInstanceState);
    }
}
