package com.android.tfg.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.android.tfg.R;
import com.android.tfg.adapter.MorePagerAdapter;
import com.android.tfg.databinding.ActivityMoreBinding;
import com.android.tfg.viewmodel.MoreViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    private String device;
    private MoreViewModel moreViewModel;
    private ActivityMoreBinding binding;

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(this).get(getString(R.string.moreViewModel), MoreViewModel.class);
        moreViewModel.registerMessagesFromDevice(device); // primera llamada para todos los dispositivos
    }

    public void configView(){
        // Device
        device=(String) Objects.requireNonNull(getIntent().getExtras()).get("device");

        /*********
         * PAGER *
         *********/
        binding.morePager.setOffscreenPageLimit(4);
        binding.morePager.setAdapter(new MorePagerAdapter(getSupportFragmentManager(),getApplicationContext(), device));

        /*****************
         * TOGGLE BUTTON *
         *****************/
        binding.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(!isChecked){return;}
            switch(checkedId){
                case R.id.toggleTemp:   binding.morePager.setCurrentItem(0);
                                        break;
                case R.id.toggleHum:    binding.morePager.setCurrentItem(1);
                                        break;
                case R.id.togglePres:   binding.morePager.setCurrentItem(2);
                                        break;
                case R.id.toggleUV:     binding.morePager.setCurrentItem(3);
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
