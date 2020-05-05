package com.android.tfg.view.More;

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
import com.android.tfg.databinding.FragmentUvBinding;
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

    private MoreViewModel moreViewModel;
    private FragmentUvBinding binding;

    public UVChartFragment(){

    }

    private void setData(LinkedList<MessageModel> messages) {

        /**************************************
         * CONVIERTE MENSAJES A VALORES (X,Y) *
         **************************************/
        ArrayList<Entry> uv = new ArrayList<>();
        for (MessageModel m : messages) {
            uv.add(new Entry(m.getDate().getSeconds(), (float) moreViewModel.convertUv(m.getUv())));
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
        serieUV.setFillDrawable(binding.getRoot().getResources().getDrawable(R.drawable.gradient_uv));
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
        binding.uvChart.setData(uvData);

        // Actualizar grafico
        binding.uvChart.invalidate();

        // Visible 2 ultimas horas
        binding.uvChart.setVisibleXRangeMaximum(2 * 60 * 60);
        binding.uvChart.moveViewToX(new Date().getTime() - (2 * 60 * 60));

        // Animar
        binding.uvChart.animateX(500);
    }

    private void setupChart(){
        binding.uvChart.getDescription().setTextSize(24f);
        binding.uvChart.getDescription().setText(getString(R.string.uvTitle));

        // descripcion
        binding.uvChart.getDescription().setEnabled(false);
        // para las acciones al pulsar
        binding.uvChart.setTouchEnabled(true);
        binding.uvChart.setDragDecelerationFrictionCoef(0.9f);
        // escalado y arrastre
        binding.uvChart.setDragEnabled(true);
        binding.uvChart.setScaleEnabled(true);
        binding.uvChart.setScaleYEnabled(true);
        binding.uvChart.setHorizontalScrollBarEnabled(true);
        binding.uvChart.setDrawGridBackground(true);
        binding.uvChart.setHighlightPerDragEnabled(true);
        binding.uvChart.setPinchZoom(true);
        binding.uvChart.setNoDataTextColor(Color.BLACK);
        binding.uvChart.setNoDataText(getString(R.string.moreNoData));
        // fondo
        binding.uvChart.setGridBackgroundColor(Color.TRANSPARENT);
        // animacion
        binding.uvChart.animateY(1000);
         // leyenda
        Legend l = binding.uvChart.getLegend();
        l.setEnabled(false);

       /*********
        * EJE X *
        *********/
        XAxis xAxis = binding.uvChart.getXAxis();
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
        YAxis leftAxis = binding.uvChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(false);
        leftAxis.enableGridDashedLine(10,5,0);
        YAxis rightAxis = binding.uvChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.moreViewModel), MoreViewModel.class);
        final Observer<LinkedList<MessageModel>> obs = messages -> {
            if(!messages.isEmpty()){
                setData(messages);
            }else{
                binding.uvChart.setVisibility(View.GONE);
            }
        };
        moreViewModel.getMessages().observe(getViewLifecycleOwner(), obs); // mensajes
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUvBinding.inflate(inflater, container, false);

        /***********
         * GRAFICO *
         ***********/
        setupChart();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        super.onActivityCreated(savedInstanceState);
    }
}
