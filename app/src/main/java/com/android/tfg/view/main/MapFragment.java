package com.android.tfg.view.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.databinding.MarkerInfoViewBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.Locale;

public class MapFragment extends SupportMapFragment {

    private MainViewModel mainViewModel;
    private GoogleMap googleMap;

    // Listener para el cambio de preferencias (global, unidades)
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.contains("units")){ // por si se trata de otra preferencia no actualizar innecesariamente
                if(mainViewModel.getDevices().getValue()==null){return;}
                updateMap(mainViewModel.getDevices().getValue());
            }
        }
    };

    /*************************
     * ADD DEVICES TO MAP    *
     * @param deviceModels   *
     *************************/
    private void getMapAsync(LinkedList<DeviceModel> deviceModels){

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapFragment.this.googleMap=googleMap;

                // custom bubble
                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    private MarkerInfoViewBinding binding= MarkerInfoViewBinding.inflate(MapFragment.this.getLayoutInflater());

                    @Override
                    public View getInfoWindow(Marker marker) {
                        binding.title.setText(marker.getTitle());
                        binding.snippet.setText(marker.getSnippet());

                        return binding.getRoot();
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });


                // añadir markers
                for(DeviceModel device : deviceModels){
                    MarkerOptions marker = new MarkerOptions()
                            .position(device.getSite())
                            .title(String.format(Locale.getDefault(),
                                    getString(R.string.markerTitleFormat),
                                    device.getName(),
                                    device.getId()))
                            .snippet(String.format(Locale.getDefault(),
                                    getString(R.string.markerSnippetFormat),
                                    device.getLastMessage().getTemp(),
                                    mainViewModel.getTempUnits(),
                                    device.getLastMessage().getHum(),
                                    getString(R.string.defUnitHum),
                                    device.getLastMessage().getPres(),
                                    mainViewModel.getPresUnits(),
                                    device.getLastMessage().getUv(),
                                    mainViewModel.getUvUnits()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    googleMap.addMarker(marker);
                }
            }
        });
    }

    /**********************
     * UPDATE DEVICES MAP *
     * @param devices     *
     **********************/
    private void updateMap(LinkedList<DeviceModel> devices){
        googleMap.clear();
        for(DeviceModel device : devices){
            MarkerOptions marker = new MarkerOptions()
                    .position(device.getSite())
                    .title(String.format(Locale.getDefault(),
                            getString(R.string.markerTitleFormat),
                            device.getName(),
                            device.getId()))
                    .snippet(String.format(Locale.getDefault(),
                            getString(R.string.markerSnippetFormat),
                            device.getLastMessage().getTemp(),
                            mainViewModel.getTempUnits(),
                            device.getLastMessage().getHum(),
                            getString(R.string.defUnitHum),
                            device.getLastMessage().getPres(),
                            mainViewModel.getPresUnits(),
                            device.getLastMessage().getUv(),
                            mainViewModel.getUvUnits()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            googleMap.addMarker(marker);
        }
    }

    /**************
     * MODEL VIEW *
     **************/
    private void configViewModel(){
        if(getActivity()==null){
            return;
        }
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);

        // Devices observer
        final Observer<LinkedList<DeviceModel>> obs = new Observer<LinkedList<DeviceModel>>() {
            @Override
            public void onChanged(LinkedList<DeviceModel> deviceModels) {
                getMapAsync(deviceModels);
            }
        };

        mainViewModel.getDevices().observe(getViewLifecycleOwner(), obs); // Observar posicion dispositivos

        // User loc observer
        final Observer<LatLng> obsLoc = new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if(latLng.latitude!=0 && latLng.longitude!=0){
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
                        }
                    }
                });
            }
        };

        mainViewModel.getUserLocation().observe(getViewLifecycleOwner(), obsLoc); // Observar posicion usuario
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setRetainInstance(true);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        configViewModel();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(preferenceChangeListener); // Preferencias
        super.onActivityCreated(bundle);
    }
}