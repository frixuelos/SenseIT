package com.android.tfg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    Toolbar toolbar;
    HashMap<String,LineChart> charts;
    MoreViewModel moreViewModel;
    String device;

    public void setupChart(){
        /**********************
         * TODOS LOS GRAFICOS *
         **********************/
        charts=new HashMap<>();
        charts.put("temp", (LineChart) findViewById(R.id.tempChart));
        Objects.requireNonNull(charts.get("temp")).getDescription().setTextSize(24f);
        Objects.requireNonNull(charts.get("temp")).getDescription().setText(getString(R.string.tempTitle));

        charts.put("hum", (LineChart) findViewById(R.id.humChart));
        Objects.requireNonNull(charts.get("hum")).getDescription().setTextSize(24f);
        Objects.requireNonNull(charts.get("hum")).getDescription().setText(getString(R.string.humTitle));

        charts.put("pres", (LineChart) findViewById(R.id.presChart));
        Objects.requireNonNull(charts.get("pres")).getDescription().setTextSize(24f);
        Objects.requireNonNull(charts.get("pres")).getDescription().setText(getString(R.string.presTitle));


        for(LineChart chart : charts.values()) {

            // descripcion
            chart.getDescription().setEnabled(true);
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
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(900f);
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
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            leftAxis.setDrawGridLines(true);
            leftAxis.setGranularityEnabled(true);
            leftAxis.setEnabled(true);
            leftAxis.setXOffset(-15);
            leftAxis.setLabelCount(4);
            leftAxis.enableGridDashedLine(10,5,0);
            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);
        }

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
        serieTemp.enableDashedLine(50, 10, 0);
        serieTemp.setColor(Color.BLACK);
        serieTemp.setValueTextColor(Color.BLACK);
        serieTemp.setLineWidth(2F);
        serieTemp.setDrawCircles(true);
        serieTemp.setDrawValues(false);
        serieTemp.setCircleColor(Color.BLACK);
        serieTemp.setDrawCircleHole(false);
        serieTemp.setFillDrawable(getDrawable(R.drawable.gradient_temp));
        serieTemp.setHighlightEnabled(false);
        serieTemp.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData tempData = new LineData(serieTemp);
        tempData.setValueTextColor(Color.BLACK);
        tempData.setValueTextSize(12f);

        // Agregar datos
        Objects.requireNonNull(charts.get("temp")).setData(tempData);

        // Actualizar grafico
        Objects.requireNonNull(charts.get("temp")).invalidate();

        // Visible 2 ultimas horas
        Objects.requireNonNull(charts.get("temp")).setVisibleXRangeMaximum(2*60*60);
        Objects.requireNonNull(charts.get("temp")).moveViewToX(messages.getLast().getDate()-(2*60*60));

        // Animar
        Objects.requireNonNull(charts.get("temp")).animateX(500);


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
        serieHum.setDrawValues(false);
        serieHum.setCircleColor(Color.BLACK);
        serieHum.setDrawCircleHole(false);
        serieHum.setFillDrawable(getDrawable(R.drawable.gradient_hum));
        serieHum.setHighlightEnabled(false);
        serieHum.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData humData = new LineData(serieHum);
        humData.setValueTextColor(Color.BLACK);
        humData.setValueTextSize(12f);

        // Agregar datos
        Objects.requireNonNull(charts.get("hum")).setData(humData);

        // Actualizar grafico
        Objects.requireNonNull(charts.get("hum")).invalidate();

        // Visible 2 ultimas horas
        Objects.requireNonNull(charts.get("hum")).setVisibleXRangeMaximum(2*60*60);
        Objects.requireNonNull(charts.get("hum")).moveViewToX(messages.getLast().getDate()-(2*60*60));

        // Animar
        Objects.requireNonNull(charts.get("hum")).animateX(500);

        /*****************
         * SERIE PRESION *
         *****************/
        LineDataSet seriePres = new LineDataSet(press, getString(R.string.presTitle));
        seriePres.setAxisDependency(YAxis.AxisDependency.LEFT);
        seriePres.enableDashedLine(50, 10, 0);
        seriePres.setColor(Color.BLACK);
        seriePres.setValueTextColor(Color.BLACK);
        seriePres.setLineWidth(2F);
        seriePres.setDrawCircles(true);
        seriePres.setDrawValues(false);
        seriePres.setCircleColor(Color.BLACK);
        seriePres.setDrawCircleHole(false);
        seriePres.setFillDrawable(getDrawable(R.drawable.gradient_pres));
        seriePres.setHighlightEnabled(false);
        seriePres.setDrawFilled(true);

        // Se convierte a objeto con todos los datos
        LineData presData = new LineData(seriePres);
        presData.setValueTextColor(Color.BLACK);
        presData.setValueTextSize(12f);

        // Agregar datos
        Objects.requireNonNull(charts.get("pres")).setData(presData);

        // Actualizar grafico
        Objects.requireNonNull(charts.get("pres")).invalidate();

        // Visible 2 ultimas horas
        Objects.requireNonNull(charts.get("pres")).setVisibleXRangeMaximum(2*60*60);
        Objects.requireNonNull(charts.get("pres")).moveViewToX(messages.getLast().getDate()-(2*60*60));

        // Animar
        Objects.requireNonNull(charts.get("pres")).animateX(500);

    }

    public void configView(){
        /****************
         * PROGRESS BAR *
         ****************/
        findViewById(R.id.progressBarMore).setVisibility(View.VISIBLE);

        // Device
        device=(String) Objects.requireNonNull(getIntent().getExtras()).get("device");

        /***********
         * TOOLBAR *
         ***********/
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
        final Observer<LinkedList<MessageModel>> obs = messages -> {
            // Ocultar barra de carga cuando se muestran datos
            findViewById(R.id.progressBarMore).setVisibility(View.GONE);

            // Cards visibles
            findViewById(R.id.cardTemp).setVisibility(View.VISIBLE);
            findViewById(R.id.cardHum).setVisibility(View.VISIBLE);
            findViewById(R.id.cardPres).setVisibility(View.VISIBLE);

            // añadir datos al grafico
            setData(messages);
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
