package com.android.tfg.view.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.adapter.FavoritesAdapter;
import com.android.tfg.databinding.FragmentFavoritesBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.swipe.SwipeRemoveCallback;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

public class FavoritesFragment extends Fragment {

    private MainViewModel mainViewModel;
    private FavoritesAdapter favoritesAdapter;
    private FragmentFavoritesBinding binding;

    // Swipe para eliminar
    private SwipeRemoveCallback swipeRemoveCallback;

    // Listener para el cambio de preferencias (actualiza los favoritos)
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListenerFav = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mainViewModel.updateFavDevices(); // Se actualizan los favoritos
        }
    };

    // Listener para el cambio de preferencias (global, unidades)
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.contains("units")){ // por si se trata de otra preferencia no actualizar innecesariamente
                favoritesAdapter.notifyDataSetChanged();
            }
        }
    };

    private void configSwipeToRemove(){
        if(getContext()==null){return;}
        swipeRemoveCallback = new SwipeRemoveCallback(getContext()){

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i){
                int pos = viewHolder.getAdapterPosition();
                DeviceModel removed = favoritesAdapter.removeItem(pos);
                mainViewModel.removeFromFavorites(removed.getId());

                Snackbar snackbar = Snackbar.make(binding.coordinator, getString(R.string.remove_from_favorites), Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.setAction(getString(R.string.undo), v -> {
                    favoritesAdapter.insertItem(removed, pos);
                    binding.favoriteRecyclerView.scrollToPosition(pos);
                    mainViewModel.add2Favorites(removed.getId());
                    binding.noFavText.setVisibility(View.GONE); // Ocultar el texto de favoritos vacio
                });
                snackbar.show();

                if(favoritesAdapter.getItemCount()==0){ // Mostrar el texto si no hay favoritos
                    binding.noFavText.setVisibility(View.VISIBLE);
                }
            }

        };
    }

    private void configRecyclerView(LinkedList<DeviceModel> devices){
        if(getActivity()==null || getContext()==null){
            return;
        }

        // Para el texto de lista de favoritos vacia
        if(devices.isEmpty()){
            binding.noFavText.setVisibility(View.VISIBLE);
        }else{
            binding.noFavText.setVisibility(View.GONE);
        }

        // Actualizar recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favoritesAdapter = new FavoritesAdapter(devices, mainViewModel);
        binding.favoriteRecyclerView.setHasFixedSize(true);
        binding.favoriteRecyclerView.setAdapter(favoritesAdapter);
        binding.favoriteRecyclerView.setLayoutManager(layoutManager);
        configSwipeToRemove();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeRemoveCallback);
        itemTouchHelper.attachToRecyclerView(binding.favoriteRecyclerView);
    }

    private void configViewModel(){
        if(getActivity()==null){
            return;
        }
        /*************
         * MODEL VIEW *
         **************/
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);
        final Observer<LinkedList<DeviceModel>> obs = deviceModels -> {
            // configurar recycler view con los datos de favoritos
            // la primera vez se configura
            if(favoritesAdapter==null){
                configRecyclerView(deviceModels);
            // las siguientes simplemente se actualizan los valores del adapter
            }else if(mainViewModel.getFavDevices().getValue()!=null){

                // Para el texto de lista de favoritos vacia
                if(mainViewModel.getFavDevices().getValue().isEmpty()){
                    binding.noFavText.setVisibility(View.VISIBLE);
                }else{
                    binding.noFavText.setVisibility(View.GONE);
                }
               favoritesAdapter.updateItems(mainViewModel.getFavDevices().getValue());
            }


        };
        mainViewModel.getFavDevices().observe(getActivity(), obs); // Observar favoritos
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(getActivity()==null){return;}
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        getActivity().getSharedPreferences("fav", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(preferenceChangeListenerFav); // favoritos
        PreferenceManager.getDefaultSharedPreferences(binding.getRoot().getContext()).registerOnSharedPreferenceChangeListener(preferenceChangeListener); // Preferencias
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) { // Es necesario para recuperar la actividad sin nullpointer
        super.onAttach(context);
    }

}
