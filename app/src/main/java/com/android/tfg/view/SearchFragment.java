package com.android.tfg.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchViewModel searchViewModel;
    private LinearLayoutManager layoutManager;
    private ActionBar toolbar;

    // Necesario para actualizar la vista conforme a los datos de la BBDD
    public void configRecyclerView(LinkedList<DeviceModel> devices){
        //recyclerView.setHasFixedSize(true); // no varia tamaño
        layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(new SearchAdapter(devices));
        recyclerView.setLayoutManager(layoutManager);
    }

    public void configView(View view){
        /************
         * TOOLBAR  *
         ************/


        /*****************
         * RECYCLER VIEW *
         *****************/
        recyclerView=view.findViewById(R.id.searchsRecyclerView);

        /*************
        * MODEL VIEW *
        **************/
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.showDevices(""); // primera llamada para todos los dispositivos
        final Observer<LinkedList<DeviceModel>> obs = new Observer<LinkedList<DeviceModel>>() {
            @Override
            public void onChanged(LinkedList<DeviceModel> deviceModels) {
                // actualizar recycler view
                configRecyclerView(deviceModels);
            }
        };
        searchViewModel.getDevices().observe(getViewLifecycleOwner(), obs); // TEST dispositivos

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
    public void onDestroy() {
        super.onDestroy();

    }


}
