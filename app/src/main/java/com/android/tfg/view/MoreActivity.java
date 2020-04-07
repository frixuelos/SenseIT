package com.android.tfg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MoreActivity extends AppCompatActivity {

    Toolbar toolbar;
    LineChart chart;
    MoreViewModel moreViewModel;
    String device;

    public void setupChart(){
        chart=findViewById(R.id.moreChart);
        // sin descripcion
        chart.getDescription().setEnabled(false);
        // para las acciones al pulsar
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);
        // escalado y arrastre
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setHorizontalScrollBarEnabled(true);
        chart.setDrawGridBackground(true);
        chart.setHighlightPerDragEnabled(true);
        chart.setPinchZoom(true);
        chart.setNoDataTextColor(Color.BLACK);
        chart.setNoDataText(getString(R.string.moreNoData));
        // fondo
        chart.setGridBackgroundColor(Color.TRANSPARENT);

        // leyenda
        Legend l = chart.getLegend();
        l.setEnabled(false);

        /*********
         * EJE X *
         *********/
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(0.25f); // 15 min
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(new Date((long)value*1000L));
            }
        });
        xAxis.setEnabled(true);
        // Se muestran las ultimas 24 horas incialmente
        xAxis.setAxisMinimum((System.currentTimeMillis()/1000F)-(TimeUnit.HOURS.toMillis(24)/1000F));


        /*********
         * EJE Y *
         *********/
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        //leftAxis.setYOffset(-9f);
        leftAxis.setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    public void setData(LinkedList<MessageModel> messages){

        /**************************************
         * CONVIERTE MENSAJES A VALORES (X,Y) *
         **************************************/
        ArrayList<Entry> temp = new ArrayList<>();
        ArrayList<Entry> hum = new ArrayList<>();
        ArrayList<Entry> press = new ArrayList<>();
        for(MessageModel m : messages){
            temp.add(new Entry(m.getDate(), (float)m.getTemp()));
            hum.add(new Entry(m.getDate(), (float) m.getHum()));
            press.add(new Entry(m.getDate(), (float) m.getPres()));
        }

        /*********************
         * SERIE TEMPERATURA *
         *********************/
        LineDataSet serieTemp = new LineDataSet(temp, getString(R.string.tempTitle));
        serieTemp.setAxisDependency(YAxis.AxisDependency.LEFT);
        serieTemp.setColor(Color.TRANSPARENT);
        serieTemp.setValueTextColor(Color.BLACK);
        serieTemp.setLineWidth(0.2F);
        serieTemp.setDrawCircles(false);
        serieTemp.setDrawValues(true);
        serieTemp.setFillAlpha(65);
        serieTemp.setFillColor(Color.RED);
        serieTemp.setHighLightColor(Color.rgb(244, 117, 117));
        serieTemp.setDrawCircleHole(false);
        serieTemp.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData tempData = new LineData(serieTemp);
        tempData.setValueTextColor(Color.BLACK);
        tempData.setValueTextSize(9f);

        // Agregar datos
        chart.setData(tempData);

        // Actualizar grafico
        chart.invalidate();

    }

    public void configView(){
        // Device
        device=(String)getIntent().getExtras().get("device");

        /***********
         * TOOLBAR *
         ***********/
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(device);
        /***********
         * GRAFICO *
         ***********/
        setupChart();

        /*************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(this).get(MoreViewModel.class);
        moreViewModel.getMessagesFromDevice(device); // primera llamada para todos los dispositivos
        final Observer<LinkedList<MessageModel>> obs = new Observer<LinkedList<MessageModel>>() {
            @Override
            public void onChanged(LinkedList<MessageModel> messages) {
                // Ocultar barra de carga cuando se muestran datos
                //vw.findViewById(R.id.progressBarSearch).setVisibility(View.INVISIBLE);

                // añadir datos al grafico
                setData(messages);
                Log.v("Mensajes...", String.valueOf(messages.getFirst().getDate()));
            }
        };
        moreViewModel.getMessages().observe(this, obs); // mensajes

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        configView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
