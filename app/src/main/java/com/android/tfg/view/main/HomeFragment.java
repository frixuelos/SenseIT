package com.android.tfg.view.main;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FusedLocationProviderClient client;
    private MainViewModel mainViewModel;
    private DeviceModel device;
    private final int REQUEST_CODE = 9632;

    // Listener para el cambio de preferencias (global, unidades)
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.contains("units")){ // por si se trata de otra preferencia no actualizar innecesariamente
                showDevice();
            }
        }
    };

    // Necesario para mostrar nuevos datos en el dispositivo
    private final Observer<DeviceModel> obs = deviceModel -> {
        if(deviceModel!=null){
            device=deviceModel;
            showDevice();
        }
    };

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
                mainViewModel.getNear(location.getLatitude(), location.getLongitude()).observe(getViewLifecycleOwner(), obs);
                mainViewModel.setLocation(location.getLatitude(), location.getLongitude()); // set user location
            }
        });
    }

    /**********************^***************
     * MUESTRA EL DISPOSITIVO MAS CERCANO *
     **************************************/
    private void showDevice(){
        binding.itemTitle.setText(device.getName());
        binding.itemId.setText(device.getId());
        binding.itemTemp.setText(String.valueOf(mainViewModel.convertTemp(device.getLastMessage().getTemp())));
        binding.itemHum.setText(String.valueOf(device.getLastMessage().getHum()));
        binding.itemPres.setText(String.valueOf(mainViewModel.convertPres(device.getLastMessage().getPres())));
        binding.itemUv.setText(String.valueOf(mainViewModel.convertUv(device.getLastMessage().getUv())));
        String date = new SimpleDateFormat("dd/MM/yyy @ HH:mm:ss", Locale.getDefault())
                        .format(new Date(device.getLastMessage().getDate().getSeconds()*1000L));
        binding.itemLastUpdate.setText(date);
        // load view gone
        binding.loadStub.setVisibility(View.GONE);
        binding.itemTitle.setVisibility(View.VISIBLE);
        binding.gridLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permitido
                getLocation();
            } else {
                // No permitido ERROR
                new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                        .setTitle(getString(R.string.errorNoLocationGrantedTitle))
                        .setMessage(getString(R.string.errorNoLocationGranted))
                        .show();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }else{
            getLocation();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        configViewModel(); // Configurar ViewModel
        checkPermissions(); // comprobar permisos
        PreferenceManager.getDefaultSharedPreferences(binding.getRoot().getContext()).registerOnSharedPreferenceChangeListener(preferenceChangeListener); // Preferencias
        super.onActivityCreated(savedInstanceState);
    }


}
