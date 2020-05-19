package com.android.tfg.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.marker.MyMarker;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.more.MoreActivity;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.LinkedList;
import java.util.Locale;

public class MapFragment extends SupportMapFragment {

    private MainViewModel mainViewModel;
    private ClusterManager<MyMarker> clusterManager;

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
                        // clusterer
                        clusterManager = new ClusterManager<MyMarker>(MapFragment.this.getActivity().getApplicationContext(), googleMap);
                        googleMap.setOnCameraIdleListener(clusterManager);
                        googleMap.setOnMarkerClickListener(clusterManager);

                        // añadir markers
                        for(DeviceModel device : deviceModels){
                            MyMarker marker = new MyMarker(device.getSite().latitude,
                                    device.getSite().longitude,
                                    String.format(Locale.getDefault(),
                                    getString(R.string.markerTitleFormat),
                                    device.getName(),
                                    device.getId()),
                                    String.format(Locale.getDefault(),
                                            getString(R.string.markerSnippetFormat),
                                            getString(R.string.tempTitle),
                                            device.getLastMessage().getTemp(),
                                            getString(R.string.humTitle),
                                            device.getLastMessage().getHum(),
                                            getString(R.string.presTitle),
                                            device.getLastMessage().getPres(),
                                            getString(R.string.uvTitle),
                                            device.getLastMessage().getUv()));

                            clusterManager.addItem(marker); // añadir marca
                        }
                    }
                });
            }
        };
        mainViewModel.getDevices().observe(getViewLifecycleOwner(), obs); // Observar posicion dispositivos
    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        configViewModel();
        super.onActivityCreated(bundle);
    }
}
