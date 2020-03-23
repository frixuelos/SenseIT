package com.android.tfg.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tfg.R;
import com.android.tfg.model.LoginUserModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LoginUserModel currentUser;

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
        /*************
        * LOGIN DATA *
        **************/
        currentUser = (LoginUserModel) getIntent().getSerializableExtra("currentUser");
        Log.v("LOL", "LLEGAAAA");
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.welcomeMessage).format(currentUser.getName()), Toast.LENGTH_LONG).show();
        TextView tHeaderMail = (TextView) findViewById(R.id.tHeaderMail);
        tHeaderMail.setText(currentUser.getMail());

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
