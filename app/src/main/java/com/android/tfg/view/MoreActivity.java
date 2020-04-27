package com.android.tfg.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.android.tfg.R;
import com.android.tfg.adapter.MoreAdapter;
import com.android.tfg.adapter.MorePagerAdapter;
import com.android.tfg.adapter.SearchAdapter;
import com.android.tfg.databinding.ActivityMoreBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.android.tfg.viewholder.SearchViewHolder;
import com.android.tfg.viewmodel.MoreViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.LinkedList;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    private String device;
    private MoreViewModel moreViewModel;
    private ActivityMoreBinding binding;
    private MoreAdapter moreAdapter;
    private int CHART_SELECTED;

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(this).get(getString(R.string.moreViewModel), MoreViewModel.class);
        moreViewModel.registerMessagesFromDevice(device); // primera llamada para todos los mensajes
        final Observer<LinkedList<MessageModel>> obs = messages -> {
            // añadir datos al recyclerview
            configRecyclerView(messages);
        };
        moreViewModel.getMessages().observe(this, obs); // mensajes
    }

    // Necesario para actualizar la vista conforme a los datos de la BBDD
    private void configRecyclerView(LinkedList<MessageModel> messages){
        if(moreAdapter!=null){
            moreAdapter.updateItems(moreViewModel.getMessages().getValue());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        moreAdapter = new MoreAdapter(messages);
        binding.moreRecyclerView.setHasFixedSize(true);
        binding.moreRecyclerView.addItemDecoration(new DividerItemDecoration(binding.moreRecyclerView.getContext(), layoutManager.getOrientation()));
        binding.moreRecyclerView.setAdapter(moreAdapter);
        binding.moreRecyclerView.setLayoutManager(layoutManager);
    }

    public void configView(){
        // Device
        device=(String) Objects.requireNonNull(getIntent().getExtras()).get("device");
        setTitle(device);

        /*********
         * PAGER *
         *********/
        binding.morePager.setOffscreenPageLimit(4);
        binding.morePager.setAdapter(new MorePagerAdapter(getSupportFragmentManager(),getApplicationContext(), device));

        /*****************
         * TOGGLE BUTTON *
         *****************/
        CHART_SELECTED=0;
        binding.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(!isChecked){return;}
            switch(checkedId){
                case R.id.toggleTemp:   binding.morePager.setCurrentItem(0);
                                        CHART_SELECTED=0;
                                        break;
                case R.id.toggleHum:    binding.morePager.setCurrentItem(1);
                                        CHART_SELECTED=1;
                                        break;
                case R.id.togglePres:   binding.morePager.setCurrentItem(2);
                                        CHART_SELECTED=2;
                                        break;
                case R.id.toggleUV:     binding.morePager.setCurrentItem(3);
                                        CHART_SELECTED=3;
                                        break;
            }
        });

    }

    public void configToolbar(){
        /***********
         * TOOLBAR *
         ***********/
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configToolbar();
        binding = ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configView();
        configViewModel();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        binding.morePager.setCurrentItem(CHART_SELECTED); // Cuando se cambia la orientacion de la pantalla establecemos el grafico seleccionado
        binding.toggleGroup.clearChecked();
        binding.toggleTemp.setChecked(false);
        switch (CHART_SELECTED){
            case 0: binding.toggleTemp.setChecked(true);
                    break;
            case 1: binding.toggleHum.setChecked(true);
                    break;
            case 2: binding.togglePres.setChecked(true);
                    break;
            case 3: binding.toggleUV.setChecked(true);
                    break;
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        moreViewModel.unregisterMessagesFromDevice(device); // Eliminar listener de mensajes asociado al dispositivo
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflar boton favoritos
        getMenuInflater().inflate(R.menu.more_favorites_menu, menu);

        // cargar preferencias favoritos
        if(moreViewModel.isFavorite(device)){ // Añadido a favoritos
            menu.findItem(R.id.favoriteEvent).setIcon(R.drawable.ic_favorite_checked_24dp);
            menu.findItem(R.id.favoriteEvent).setChecked(true);

        }else{ // eliminado de favoritos
            menu.findItem(R.id.favoriteEvent).setIcon(R.drawable.ic_favorite_24dp);
            menu.findItem(R.id.favoriteEvent).setChecked(false);
        }

        // capturar el evento fav
        menu.findItem(R.id.favoriteEvent).setOnMenuItemClickListener(item -> {
            if(!item.isChecked()){ // Cambiar a añadido a favoritos
                addFav(item);
            }else{ // Cambiar a eliminado de favoritos
                removeFav(item);
            }
            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addFav(MenuItem item){
        // configurar icono
        item.setIcon(R.drawable.ic_favorite_checked_24dp);
        item.setChecked(true);

        // añadir a favoritos
        moreViewModel.add2Favorites(device);
        Toast.makeText(getApplicationContext(), getString(R.string.add_to_favorites), Toast.LENGTH_SHORT).show();
    }

    private void removeFav(MenuItem item){
        // configurar icono
        item.setIcon(R.drawable.ic_favorite_24dp);
        item.setChecked(false);

        // eliminar de favoritos
        moreViewModel.removeFromFavorites(device);
        Toast.makeText(getApplicationContext(), getString(R.string.remove_from_favorites), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
