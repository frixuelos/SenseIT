package com.android.tfg.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.adapter.FavoritesAdapter;
import com.android.tfg.adapter.SearchAdapter;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.swipe.SwipeRemoveCallback;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.LinkedList;

public class FavoritesFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;

    // Swipe para eliminar
    private SwipeRemoveCallback swipeRemoveCallback;

    // Listener para el cambio de preferencias (actualiza los favoritos)
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mainViewModel.updateFavDevices(); // Se actualizan los favoritos
        }
    };

    private void configSwipeToRemove(){
        if(getContext()==null){return;}
        swipeRemoveCallback = new SwipeRemoveCallback(getContext()){

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i){
                int pos = viewHolder.getAdapterPosition();
                DeviceModel removed = favoritesAdapter.removeItem(pos);
                mainViewModel.removeFromFavorites(removed.getDeviceID());

                Snackbar snackbar = Snackbar
                        .make(getView(), getString(R.string.remove_from_favorites), Snackbar.LENGTH_LONG);
                snackbar.getView().setBottom(-56);
                snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favoritesAdapter.insertItem(removed, pos);
                        recyclerView.scrollToPosition(pos);
                        mainViewModel.add2Favorites(removed.getDeviceID());
                        getView().findViewById(R.id.no_fav_text).setVisibility(View.GONE); // Ocultar el texto de favoritos vacio
                    }
                });
                snackbar.show();

                if(favoritesAdapter.getItemCount()==0){ // Mostrar el texto si no hay favoritos
                    getView().findViewById(R.id.no_fav_text).setVisibility(View.VISIBLE);
                }
                            }

        };
    }

    private void configRecyclerView(LinkedList<DeviceModel> devices){
        if(getActivity()==null || getContext()==null){
            return;
        }

        // Para el texto de lista de favoritos vacia
        if(getView()!=null){
            if(!devices.isEmpty()){getView().findViewById(R.id.no_fav_text).setVisibility(View.GONE);}
            else{getView().findViewById(R.id.no_fav_text).setVisibility(View.VISIBLE);}
        }

        // Actualizar recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favoritesAdapter = new FavoritesAdapter(devices);
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        configSwipeToRemove();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeRemoveCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void configViewModel(){
        if(getActivity()==null){
            return;
        }
        /*************
         * MODEL VIEW *
         **************/
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);
        final Observer<LinkedList<DeviceModel>> obs = new Observer<LinkedList<DeviceModel>>() {
            @Override
            public void onChanged(LinkedList<DeviceModel> deviceModels) {
                // configurar recycler view con los datos de favoritos
                // la primera vez se configura
                if(favoritesAdapter==null){
                    configRecyclerView(deviceModels);

                // las siguientes simplemente se actualizan los valores del adapter
                }else if(mainViewModel.getFavDevices().getValue()!=null){
                   favoritesAdapter.updateItems(mainViewModel.getFavDevices().getValue());
                }


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
        if(getActivity()==null){return;}
        configViewModel(); // Configuramos el viewmodel aqui para que cargue los datos antes
        getActivity().getSharedPreferences("fav", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) { // Es necesario para recuperar la actividad sin nullpointer
        super.onAttach(context);
    }

}
