package com.android.tfg.view.Main;

import android.Manifest;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tfg.R;
import com.android.tfg.databinding.FragmentHomeBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.LinkedList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FusedLocationProviderClient client;
    private MainViewModel mainViewModel;

    /**************
     * MODEL VIEW *
     **************/
    private void configViewModel(){
        mainViewModel = new ViewModelProvider(getActivity()).get(getString(R.string.mainViewModel), MainViewModel.class);

    }

    /************************
     * OBTIENE LOCALIZACION *
     ************************/
    private void getLocation(){
        client.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            if(location!=null){
                showDevice(mainViewModel.getNear(location.getLatitude(), location.getLongitude())); // Muestra los datos
            }
        });
    }

    /**********************^***************
     * MUESTRA EL DISPOSITIVO MAS CERCANO *
     * @param device                      *
     **************************************/
    private void showDevice(DeviceModel device){
        binding.textView.setText(device.getName());
        binding.loadStub.setVisibility(View.GONE); // load view gone
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(permissions[1].equals(ACCESS_COARSE_LOCATION) && grantResults.length>0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED || grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    // Permitido

                    getLocation();
                }else{
                    // No permitido ERROR
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(getString(R.string.errorNoLocationGrantedTitle))
                            .setMessage(getString(R.string.errorNoLocationGranted))
                            .show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.loadStub.inflate(); // load view inflate

        client = LocationServices.getFusedLocationProviderClient(binding.getRoot().getContext());

        return binding.getRoot();
    }

    /**************************************
     * COMPRUEBA PERMISOS DE LOCALIZACION *
     **************************************/
    private void checkPermissions(){
        if(ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        checkPermissions(); // Cuando se inicia la app comprobamos los permisos
        configViewModel(); // Configurar ViewModel
        super.onActivityCreated(savedInstanceState);
    }


}
