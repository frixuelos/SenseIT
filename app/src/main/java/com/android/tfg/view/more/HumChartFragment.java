package com.android.tfg.view.more;

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
import com.android.tfg.databinding.FragmentHumBinding;
import com.android.tfg.model.MessageModel;
import com.android.tfg.viewmodel.MoreViewModel;
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

public class HumChartFragment extends Fragment {

    private MoreViewModel moreViewModel;
    private FragmentHumBinding binding;

    public HumChartFragment() {
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
                binding.humChart.clear();
            }
        };
        moreViewModel.getMessages().observe(getViewLifecycleOwner(), obs); // mensajes
    }

    private void setData(LinkedList<MessageModel> messages) {

        /**************************************
         * CONVIERTE MENSAJES A VALORES (X,Y) *
         **************************************/
        ArrayList<Entry> hum = new ArrayList<>();
        for (MessageModel m : messages) {
            hum.add(new Entry(m.getDate().getSeconds(), (float) m.getHum()));
        }

        /*****************
         * SERIE HUMEDAD *
         *****************/
        LineDataSet serieHum = new LineDataSet(hum, getString(R.string.humTitle));
        serieHum.setAxisDependency(YAxis.AxisDependency.LEFT);
        serieHum.enableDashedLine(50, 10, 0);
        serieHum.setColor(Color.BLACK);
        serieHum.setValueTextColor(Color.BLACK);
        serieHum.setLineWidth(2F);
        serieHum.setDrawCircles(true);
        serieHum.setDrawValues(true);
        serieHum.setCircleColor(Color.BLACK);
        serieHum.setDrawCircleHole(false);
        serieHum.setFillDrawable(binding.getRoot().getResources().getDrawable(R.drawable.gradient_hum));
        serieHum.setHighlightEnabled(false);
        serieHum.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData humData = new LineData(serieHum);
        humData.setValueTextColor(Color.BLACK);
        humData.setValueTextSize(12f);
        // Formatter para mostrar solo 2 decimales
        humData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(Math.round(value*100.0)/100.0);
            }
        });

        // Agregar datos
        binding.humChart.setData(humData);

        // Actualizar grafico
        binding.humChart.invalidate();

        // Visible 2 ultimas horas
        binding.humChart.setVisibleXRangeMaximum(2 * 60 * 60);
        binding.humChart.moveViewToX(new Date().getTime() - (2 * 60 * 60));

        // Animar
        binding.humChart.animateX(500);
    }

    private void setupChart(){
        binding.humChart.getDescription().setTextSize(24f);
        binding.humChart.getDescription().setText(getString(R.string.humTitle));

        // descripcion
        binding.humChart.getDescription().setEnabled(false);
        // para las acciones al pulsar
        binding.humChart.setTouchEnabled(true);
        binding.humChart.setDragDecelerationFrictionCoef(0.9f);
        // escalado y arrastre
        binding.humChart.setDragEnabled(true);
        binding.humChart.setScaleEnabled(true);
        binding.humChart.setScaleYEnabled(true);
        binding.humChart.setHorizontalScrollBarEnabled(true);
        binding.humChart.setDrawGridBackground(true);
        binding.humChart.setHighlightPerDragEnabled(true);
        binding.humChart.setPinchZoom(true);
        binding.humChart.setNoDataTextColor(Color.BLACK);
        binding.humChart.setNoDataText(getString(R.string.moreNoData));
        // fondo
        binding.humChart.setGridBackgroundColor(Color.TRANSPARENT);
        // animacion
        binding.humChart.animateY(1000);
        // leyenda
        Legend l = binding.humChart.getLegend();
        l.setEnabled(false);
        /*********
         * EJE X *
         *********/
        XAxis xAxis = binding.humChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
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
        YAxis leftAxis = binding.humChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setEnabled(false);
        leftAxis.setLabelCount(4);
        leftAxis.enableGridDashedLine(10,5,0);
        YAxis rightAxis = binding.humChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHumBinding.inflate(inflater, container, false);

        /***********
         * GRAFICO *
         ***********/
        setupChart();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        super.onActivityCreated(savedInstanceState);
    }
}
