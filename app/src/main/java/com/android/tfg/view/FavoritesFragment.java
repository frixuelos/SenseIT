package com.android.tfg.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.MainViewModel;

import java.util.LinkedList;

public class FavoritesFragment extends Fragment {

    private MainViewModel mainViewModel;

    public void configRecyclerView(LinkedList<DeviceModel> deviceModels){

    }

    private void configViewModel(){
        /*************
         * MODEL VIEW *
         **************/
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);
        final Observer<LinkedList<DeviceModel>> obs = new Observer<LinkedList<DeviceModel>>() {
            @Override
            public void onChanged(LinkedList<DeviceModel> deviceModels) {
                // configurar recycler view con los datos
                configRecyclerView(deviceModels);
            }
        };
        mainViewModel.getFavDevices().observe(getActivity(), obs); // Observar favoritos
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        inflater.inflate(R.layout.fragment_favorites, container);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        super.onActivityCreated(savedInstanceState);
    }

}
