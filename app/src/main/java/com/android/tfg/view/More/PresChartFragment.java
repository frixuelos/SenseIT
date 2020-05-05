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
import com.android.tfg.databinding.FragmentPresBinding;
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

public class PresChartFragment extends Fragment {

    private MoreViewModel moreViewModel;
    private FragmentPresBinding binding;

    public PresChartFragment(){

    }

    private void setData(LinkedList<MessageModel> messages) {

        /**************************************
         * CONVIERTE MENSAJES A VALORES (X,Y) *
         **************************************/
        ArrayList<Entry> pres = new ArrayList<>();
        for (MessageModel m : messages) {
            pres.add(new Entry(m.getDate().getSeconds(), (float) moreViewModel.convertPres(m.getPres())));
        }

        /*****************
         * SERIE PRESION *
         *****************/
        LineDataSet seriePres = new LineDataSet(pres, getString(R.string.presTitle));
        seriePres.setAxisDependency(YAxis.AxisDependency.LEFT);
        seriePres.enableDashedLine(50, 10, 0);
        seriePres.setColor(Color.BLACK);
        seriePres.setValueTextColor(Color.BLACK);
        seriePres.setLineWidth(2F);
        seriePres.setDrawCircles(true);
        seriePres.setDrawValues(true);
        seriePres.setCircleColor(Color.BLACK);
        seriePres.setDrawCircleHole(false);
        seriePres.setFillDrawable(binding.getRoot().getResources().getDrawable(R.drawable.gradient_pres));
        seriePres.setHighlightEnabled(false);
        seriePres.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData presData = new LineData(seriePres);
        presData.setValueTextColor(Color.BLACK);
        presData.setValueTextSize(12f);
        // Formatter para mostrar solo 2 decimales
        presData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(Math.round(value*100.0)/100.0);
            }
        });

        // Agregar datos
        binding.presChart.setData(presData);

        // Actualizar grafico
        binding.presChart.invalidate();

        // Visible 2 ultimas horas
        binding.presChart.setVisibleXRangeMaximum(2 * 60 * 60);
        binding.presChart.moveViewToX(new Date().getTime() - (2 * 60 * 60));

        // Animar
        binding.presChart.animateX(500);
    }

    private void setupChart(){
        binding.presChart.getDescription().setTextSize(24f);
        binding.presChart.getDescription().setText(getString(R.string.presTitle));

        // descripcion
        binding.presChart.getDescription().setEnabled(false);
        // para las acciones al pulsar
        binding.presChart.setTouchEnabled(true);
        binding.presChart.setDragDecelerationFrictionCoef(0.9f);
        // escalado y arrastre
        binding.presChart.setDragEnabled(true);
        binding.presChart.setScaleEnabled(true);
        binding.presChart.setScaleYEnabled(true);
        binding.presChart.setHorizontalScrollBarEnabled(true);
        binding.presChart.setDrawGridBackground(true);
        binding.presChart.setHighlightPerDragEnabled(true);
        binding.presChart.setPinchZoom(true);
        binding.presChart.setNoDataTextColor(Color.BLACK);
        binding.presChart.setNoDataText(getString(R.string.moreNoData));
        // fondo
        binding.presChart.setGridBackgroundColor(Color.TRANSPARENT);
        // animacion
        binding.presChart.animateY(1000);
         // leyenda
        Legend l = binding.presChart.getLegend();
        l.setEnabled(false);

       /*********
        * EJE X *
        *********/
        XAxis xAxis = binding.presChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.enableGridDashedLine(10,5,0);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-45f);
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
        YAxis leftAxis = binding.presChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(false);
        leftAxis.enableGridDashedLine(10,5,0);
        YAxis rightAxis = binding.presChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.moreViewModel), MoreViewModel.class);
        final Observer<LinkedList<MessageModel>> obs = messages -> {
            // añadir datos al grafico
            if(!messages.isEmpty()){
                setData(messages);
            }else{
                binding.presChart.setVisibility(View.GONE);
            }
        };
        moreViewModel.getMessages().observe(getViewLifecycleOwner(), obs); // mensajes
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPresBinding.inflate(inflater, container, false);

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
