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
import androidx.viewpager.widget.ViewPager;

import com.android.tfg.R;
import com.android.tfg.adapter.MorePagerAdapter;
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
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    Toolbar toolbar;
    String device;
    ViewPager viewPager;
    MoreViewModel moreViewModel;

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(this).get("charts",MoreViewModel.class);
        moreViewModel.getMessagesFromDevice(device); // primera llamada para todos los dispositivos
        final Observer<LinkedList<MessageModel>> obs = new Observer<LinkedList<MessageModel>>() {
            @Override
            public void onChanged(LinkedList<MessageModel> messages) {
                Log.v("ACTIVIDAD", "ESCUCHANDO");
            }
        };
        moreViewModel.getMessages().observe(this, obs); // mensajes
    }

    public void configView(){
        // Device
        device=(String) Objects.requireNonNull(getIntent().getExtras()).get("device");

        /***********
         * TOOLBAR *
         ***********/
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(device);

        /*********
         * PAGER *
         *********/
        viewPager = findViewById(R.id.morePager);
        viewPager.setAdapter(new MorePagerAdapter(getSupportFragmentManager(),getApplicationContext(), device));
        // para el tablayout
        TabLayout tabLayout = findViewById(R.id.tabMore);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        configView();
        configViewModel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
