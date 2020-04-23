package com.android.tfg.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.view.Window;

import com.android.tfg.databinding.ActivityMainBinding;
import com.android.tfg.swipe.NoSwipeViewPager;
import com.android.tfg.R;
import com.android.tfg.adapter.MainPagerAdapter;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        mainViewModel = new ViewModelProvider(this).get(getString(R.string.mainViewModel), MainViewModel.class);
        mainViewModel.registerAllDevices(); // primera llamada para todos los dispositivos (inclusive favoritos)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // modo normal (muestra pantalla inicio)
        setTheme(R.style.AppTheme);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY); // para ocultar con scroll
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.nav_home));

        /**************
         * VIEW MODEL *
         **************/
        configViewModel();

        /*************************
         * BOTTOM NAVIGATION BAR *
         *************************/
        binding.bottomBar.setOnNavigationItemSelectedListener(menuItem -> {
           switch(menuItem.getItemId()) {
               case R.id.nav_home: binding.mainViewPager.setCurrentItem(0, true);
                                   return true;

               case R.id.nav_search:   binding.mainViewPager.setCurrentItem(1,true);
                                       return true;

               case R.id.nav_map:  binding.mainViewPager.setCurrentItem(2, true);
                                   return true;

               case R.id.nav_fav:  binding.mainViewPager.setCurrentItem(3,true);
                                   return true;

               case R.id.nav_config:   binding.mainViewPager.setCurrentItem(4, true);
                                       return true;

           }
           return false;
        });

        /**************
         * VIEW PAGER *
         **************/
        binding.mainViewPager.setOffscreenPageLimit(5);
        binding.mainViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        binding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateOptionsMenu(); // invalida el menu del toolbar
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: binding.bottomBar.getMenu().findItem(R.id.nav_home).setChecked(true);
                        setTitle(getString(R.string.nav_home));
                        break;
                    case 1: binding.bottomBar.getMenu().findItem(R.id.nav_search).setChecked(true);
                        setTitle(getString(R.string.nav_search));
                        break;
                    case 2: binding.bottomBar.getMenu().findItem(R.id.nav_map).setChecked(true);
                        setTitle(getString(R.string.nav_map));
                        break;
                    case 3: binding.bottomBar.getMenu().findItem(R.id.nav_fav).setChecked(true);
                        setTitle(getString(R.string.nav_fav));
                        break;
                    case 4: binding.bottomBar.getMenu().findItem(R.id.nav_config).setChecked(true);
                        setTitle(getString(R.string.nav_config));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mainViewModel.unregisterAllDevices(); // Eliminar listener de dispositivos
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){ // Para la pulsacion del boton atras (viewpager)
        if(binding.mainViewPager.getCurrentItem()==0) {
            super.onBackPressed();
        }else{
            binding.mainViewPager.setCurrentItem(binding.mainViewPager.getCurrentItem()-1,true);
        }

    }

    /****************
     * MENU TOOLBAR *
     ****************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        switch(binding.mainViewPager.getCurrentItem()){
            case 1: getMenuInflater().inflate(R.menu.option_menu, menu); // Se infla aqui y se muestra para minimizar al lag del menu
                    menu.findItem(R.id.action_search).setVisible(true);
                    break;

        }
        return super.onCreateOptionsMenu(menu);
    }
}
