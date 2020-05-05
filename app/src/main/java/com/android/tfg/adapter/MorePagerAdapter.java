package com.android.tfg.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.view.More.HumChartFragment;
import com.android.tfg.view.More.PresChartFragment;
import com.android.tfg.view.More.TempChartFragment;
import com.android.tfg.view.More.UVChartFragment;

public class MorePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 4;
    private Context context;
    private String device;
    private TempChartFragment tempChartFragment;
    private HumChartFragment humChartFragment;
    private PresChartFragment presChartFragment;
    private UVChartFragment uvChartFragment;

    public MorePagerAdapter(@NonNull FragmentManager fm, Context c, String d){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context=c;
        device=d;
    }

    public MorePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            default: if(tempChartFragment==null){tempChartFragment=new TempChartFragment();}
                    return tempChartFragment;
            case 1: if(humChartFragment==null){humChartFragment=new HumChartFragment();}
                    return humChartFragment;
            case 2: if(presChartFragment==null){presChartFragment=new PresChartFragment();}
                    return presChartFragment;
            case 3: if(uvChartFragment==null){uvChartFragment=new UVChartFragment();}
                    return uvChartFragment;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            default: return context.getString(R.string.tempTitle);
            case 1: return context.getString(R.string.humTitle);
            case 2: return context.getString(R.string.presTitle);
            case 3: return context.getString(R.string.uvTitle);
        }
    }
}
