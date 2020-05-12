package com.android.tfg.view.main;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.more.MoreActivity;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapFragment extends SupportMapFragment {

    private MainViewModel mainViewModel;

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
                getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // añadir markers
                        for(DeviceModel device : deviceModels){
                            MarkerOptions marker = new MarkerOptions().position(device.getSite()); // Crear marca

                            //Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault()); // Obtener nombre de posicion
                            marker.title(device.getId());
                            googleMap.addMarker(marker);
                        }

                        // Click en el marker
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Intent i = new Intent(getContext(), MoreActivity.class);
                                i.putExtra("device", marker.getTitle());
                                startActivity(i);
                                return false;
                            }
                        });

                    }
                });
            }
        };
        mainViewModel.getDevices().observe(getActivity(), obs); // Observar posicion dispositivos
    }


    @Override
    public void onAttach(Activity activity) {
        configViewModel();
        super.onAttach(activity);
    }
}