package com.android.tfg.view;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tfg.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback{

    MapView mapView;
    GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView=rootView.findViewById(R.id.map_view);
        /**********************
         * THREAD TO SHOW MAP *
         **********************/
        if(googleMap==null){ // de esta manera no se congela la aplicacion
            Log.v("MAP", "SE CREA POR SER NULL");
            final OnMapReadyCallback listener = this;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isAdded()){
                        mapView.onCreate(null);
                        mapView.onResume();
                        mapView.getMapAsync(listener);
                    }
                }
            }, 1000);
        }else{
            mapView.onResume();
        }

        ((MainActivity)getActivity()).getSupportActionBar().hide();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView(){
        mapView.onPause();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
        ((MainActivity)getActivity()).getSupportActionBar().show();
        Log.v("MAP","PAUSE");
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap=mMap;
    }

}
