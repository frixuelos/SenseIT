package com.android.tfg.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.SearchViewModel;
import java.util.LinkedList;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    private SearchViewModel searchViewModel;
    private ActionBar toolbar;
    private SearchAdapter searchAdapter;

    // Necesario para actualizar la vista conforme a los datos de la BBDD
    public void configRecyclerView(LinkedList<DeviceModel> devices){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        searchAdapter = new SearchAdapter(devices);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void configView(View view){
        final View vw = view;
        /****************
         * PROGRESS BAR *
         ****************/
        view.findViewById(R.id.progressBarSearch).setVisibility(View.VISIBLE);

        /******************
         * SEARCH TOOLBAR *
         ******************/
        setHasOptionsMenu(true);

        /*****************
         * RECYCLER VIEW *
         *****************/
        recyclerView=view.findViewById(R.id.searchsRecyclerView);

        /*************
        * MODEL VIEW *
        **************/
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setDevicesListener(); // primera llamada para todos los dispositivos
        final Observer<LinkedList<DeviceModel>> obs = new Observer<LinkedList<DeviceModel>>() {
            @Override
            public void onChanged(LinkedList<DeviceModel> deviceModels) {
                // Ocultar barra de carga cuando se muestran datos
                vw.findViewById(R.id.progressBarSearch).setVisibility(View.INVISIBLE);

                // configurar recycler view con los datos
                configRecyclerView(deviceModels);
            }
        };
        searchViewModel.getDevices().observe(getViewLifecycleOwner(), obs); // TEST dispositivos

        /*****************
         * HIDE KEYBOARD *
         *****************/
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
                return true;
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) { // Es necesario para recuperar la actividad sin nullpointer
        super.onAttach(context);
        /***********
         * TOOLBAR *
         ***********/
        toolbar=((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setTitle(getResources().getString(R.string.nav_search));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        configView(view); // Configurar vista (inicializaciones)

        return view;
    }

   @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // Inflar menu busqueda
        inflater.inflate(R.menu.option_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.searchHint));
        EditText editText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setHighlightColor(getResources().getColor(R.color.colorAccent));
        editText.setTextColor(getResources().getColor(R.color.titleColor));
        searchView.setIconifiedByDefault(false);

        /*******************
         * SEARCH LISTENER *
         *******************/
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) { // Filtra con cada cambio de texto
        searchAdapter.filter(newText);
        return false;
    }
}
