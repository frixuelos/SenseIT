package com.android.tfg.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.SearchView;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.tfg.R;
import com.android.tfg.adapter.SearchAdapter;
import com.android.tfg.databinding.FragmentSearchBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.MainViewModel;
import java.util.LinkedList;
import java.util.Objects;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private MainViewModel mainViewModel;
    private SearchAdapter searchAdapter;
    private FragmentSearchBinding binding;

    // Necesario para actualizar la vista conforme a los datos de la BBDD
    private void configRecyclerView(LinkedList<DeviceModel> devices){
        if(searchAdapter!=null){
            searchAdapter.updateItems(mainViewModel.getDevices().getValue());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        searchAdapter = new SearchAdapter(devices);
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
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // Inflar menu busqueda
        /*******************
         * SEARCH LISTENER *
         *******************/
        ((SearchView)menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) { // Filtra con cada cambio de texto
        searchAdapter.getFilter().filter(newText);
        return false;
    }
}
