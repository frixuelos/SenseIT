package com.android.tfg;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public void goHome(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
        fragmentTransaction.commit();
    }

    public void goMap(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new MapFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // NAVIGATION
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // COLOR CATEGORIAS MENU
        Menu menu = navigationView.getMenu();
        MenuItem mGeneral = menu.findItem(R.id.menuGroupGeneral);
        SpannableString s = new SpannableString(mGeneral.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MenuText), 0, s.length(), 0);
        mGeneral.setTitle(s);

        // HOME FRAGMENT
        goHome();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // NAVEGACION
        switch(menuItem.getItemId()){
            case R.id.home: goHome(); break;
            case R.id.map: goMap(); break;
        }
        drawerLayout.closeDrawers(); // Cierra el menu lateral
        return true;
    }
}
