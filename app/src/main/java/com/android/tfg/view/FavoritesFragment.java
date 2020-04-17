package com.android.tfg.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.adapter.FavoritesAdapter;
import com.android.tfg.adapter.SearchAdapter;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

public class FavoritesFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mainViewModel.registerAllFavorites(); // Se vuelve a consultar el valor
        }
    };

    private void configRecyclerView(LinkedList<DeviceModel> devices){
        // Para el texto de lista de favoritos vacia
        if(getView()!=null){
            if(!devices.isEmpty()){getView().findViewById(R.id.no_fav_text).setVisibility(View.GONE);}
            else{getView().findViewById(R.id.no_fav_text).setVisibility(View.VISIBLE);}
        }

        // Actualizar recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favoritesAdapter = new FavoritesAdapter(devices);
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void configViewModel(){
        /*************
         * MODEL VIEW *
         **************/
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);
        final Observer<LinkedList<DeviceModel>> obs = new Observer<LinkedList<DeviceModel>>() {
            @Override
            public void onChanged(LinkedList<DeviceModel> deviceModels) {
                // configurar recycler view con los datos de favoritos
                configRecyclerView(deviceModels);
            }
        };
        mainViewModel.getFavDevices().observe(getActivity(), obs); // Observar favoritos
    }

    private void configView(View v){
        recyclerView=v.findViewById(R.id.favoriteRecyclerView);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        configView(v); // Configurar vista

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        getActivity().getSharedPreferences("fav", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) { // Es necesario para recuperar la actividad sin nullpointer
        super.onAttach(context);
    }

}
