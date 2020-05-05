package com.android.tfg.view.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.widget.Filter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.tfg.R;
import com.android.tfg.adapter.SearchAdapter;
import com.android.tfg.databinding.FragmentSearchBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.MainViewModel;
import java.util.LinkedList;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private Filter searchFilter;
    private boolean SEARCH_ENABLED = false;
    private MainViewModel mainViewModel;
    private SearchAdapter searchAdapter;
    private FragmentSearchBinding binding;
    // Listener para el cambio de preferencias (actualiza los favoritos)
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListenerFav = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(binding.searchsRecyclerView.getAdapter()!=null){
                // Se actualiza el elemento que ha cambiado
                searchAdapter.updateItem(key);
            }
        }
    };

    // Necesario para actualizar la vista conforme a los datos de la BBDD
    private void configRecyclerView(LinkedList<DeviceModel> devices){
        if(searchAdapter!=null){
            searchAdapter.updateItems(mainViewModel.getDevices().getValue());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        searchAdapter = new SearchAdapter(devices, mainViewModel);
        binding.searchsRecyclerView.setHasFixedSize(true);
        binding.searchsRecyclerView.setAdapter(searchAdapter);
        binding.searchsRecyclerView.setLayoutManager(layoutManager);
    }

    private void configView(){
        /******************
         * SEARCH TOOLBAR *
         ******************/
        setHasOptionsMenu(true);
    }

    private void configViewModel(){
        if(getActivity()==null){return;}
        /*************
         * MODEL VIEW *
         **************/
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);
        final Observer<LinkedList<DeviceModel>> obs = deviceModels -> {
            // configurar recycler view con los datos
            if(deviceModels.isEmpty()){return;}
            configRecyclerView(deviceModels);
        };
        mainViewModel.getDevices().observe(getActivity(), obs); // TEST dispositivos
    }

    @Override
    public void onAttach(@NonNull Context context) { // Es necesario para recuperar la actividad sin nullpointer
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        configView(); // Configurar vista (inicializaciones)

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        getActivity().getSharedPreferences("fav", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(preferenceChangeListenerFav);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // Inflar menu busqueda
        /*******************
         * SEARCH LISTENER *
         *******************/
        ((SearchView)menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(this); // Busqueda
        menu.findItem(R.id.action_search).setOnActionExpandListener(this); // Collapse

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(!SEARCH_ENABLED){return false;}
        if(searchFilter==null){
            searchFilter=searchAdapter.getFilter();
        }
        searchFilter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) { // Filtra con cada cambio de texto
        if(!SEARCH_ENABLED){return false;}
        if(searchFilter==null){
            searchFilter=searchAdapter.getFilter();
        }
        searchFilter.filter(newText);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) { // Cuando se expande el menu de busqueda
        SEARCH_ENABLED=true;
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) { // Cuando se oculta
        searchAdapter.clearFilter(searchFilter);
        SEARCH_ENABLED=false;
        return true;
    }
}
