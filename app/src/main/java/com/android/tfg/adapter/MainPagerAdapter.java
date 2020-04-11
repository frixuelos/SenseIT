package com.android.tfg.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.android.tfg.view.HomeFragment;
import com.android.tfg.view.SearchFragment;
import com.google.android.gms.maps.SupportMapFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();

            case 1: return new SearchFragment();

            case 2: return new HomeFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
