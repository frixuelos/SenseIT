package com.android.tfg.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.LinkedList;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String device;
    private ViewPager viewPager;
    private MoreViewModel moreViewModel;

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(this).get(getString(R.string.moreViewModel), MoreViewModel.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflar boton favoritos
        getMenuInflater().inflate(R.menu.favorites_menu, menu);

        // cargar preferencias favoritos
        if(moreViewModel.isFavorite(device)){
            toolbar.getMenu().findItem(R.id.favoriteEvent).setIcon(getDrawable(R.drawable.ic_favorite_checked_24dp));
            menu.findItem(R.id.favoriteEvent).setChecked(true);
        }else{
            toolbar.getMenu().findItem(R.id.favoriteEvent).setIcon(getDrawable(R.drawable.ic_favorite_24dp));
            menu.findItem(R.id.favoriteEvent).setChecked(false);
        }

        // capturar el evento fav
        menu.findItem(R.id.favoriteEvent).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(!item.isChecked()){ // Cambiar a añadido a favoritos
                    addFav(item);
                }else{ // Cambiar a eliminado de favoritos
                    removeFav(item);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addFav(MenuItem item){
        // configurar icono
        item.setIcon(getDrawable(R.drawable.ic_favorite_checked_24dp));
        item.setChecked(true);

        // añadir a favoritos
        moreViewModel.add2Favorites(device);
    }

    private void removeFav(MenuItem item){
        // configurar icono
        item.setIcon(getDrawable(R.drawable.ic_favorite_24dp));
        item.setChecked(false);

        // eliminar de favoritos
        moreViewModel.removeFromFavorites(device);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
