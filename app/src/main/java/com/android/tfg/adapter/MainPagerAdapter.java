package com.android.tfg.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.android.tfg.view.main.FavoritesFragment;
import com.android.tfg.view.main.HomeFragment;
import com.android.tfg.view.main.MapFragment;
import com.android.tfg.view.main.SearchFragment;
import com.android.tfg.view.main.SettingsFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 5;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private MapFragment mapFragment;
    private FavoritesFragment favoritesFragment;
    private SettingsFragment settingsFragment;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: if(homeFragment==null){homeFragment=new HomeFragment();} return homeFragment;

            case 1: if(searchFragment==null){searchFragment=new SearchFragment();} return searchFragment;

            case 2: if(mapFragment==null){mapFragment=new MapFragment();} return mapFragment;

            case 3: if(favoritesFragment==null){favoritesFragment=new FavoritesFragment();} return favoritesFragment;

            case 4: if(settingsFragment==null){settingsFragment=new SettingsFragment();} return settingsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
