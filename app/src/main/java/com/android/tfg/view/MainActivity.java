package com.android.tfg.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.view.Window;

import com.android.tfg.R;
import com.android.tfg.viewmodel.UserViewModel;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity{

    UserViewModel userViewModel;
    BottomNavigationView bottomNavigationView;

    public void goHome(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, new HomeFragment())
                .commit();
    }

    public void goConfig(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, new SupportMapFragment())
                .commitNow();
    }

    public void goSearch(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, new SearchFragment())
                .commitNow();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY); // para ocultar con scroll
        setContentView(R.layout.activity_main);

        /**************
         * VIEW MODEL *
         **************/
        initLoginUserViewModel();

        /***********
         * TOOLBAR *
         ***********/
        Toolbar actionBar = findViewById(R.id.toolbar);
        actionBar.setNestedScrollingEnabled(true);
        setSupportActionBar(actionBar);

        /*************************
         * BOTTOM NAVIGATION BAR *
         *************************/
        bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_home: goHome();
                                        return true;

                    case R.id.nav_search: goSearch();
                                        return true;

                    case R.id.nav_fav: goConfig();
                        return true;
                }
                return false;
            }
        });

        /***********************
         * HOME FRAGMENT START *
         ***********************/
        goHome();

    }

    private void initLoginUserViewModel(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /*getMenuInflater().inflate(R.menu.option_menu, menu);
        ******************************
         * FIX COLOR (WHITE -> BLACK) *
         ******************************
        for(int i=0; i<menu.size(); i++){
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.optLogout:    //userViewModel.logout();
                                    break;
        }
        return true;
    }

}
