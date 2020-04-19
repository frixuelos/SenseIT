package com.android.tfg.viewmodel;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.android.tfg.repository.SigfoxRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParser;
import com.google.rpc.Help;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MainViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<LinkedList<DeviceModel>> devices, favDevices;
    private SharedPreferences sharedPreferences;

    /*************
     * OBSERVERS *
     *************/
    private final Observer<LinkedList<DeviceModel>> allDevicesObserver = new Observer<LinkedList<DeviceModel>>() {
        @Override
        public void onChanged(LinkedList<DeviceModel> deviceModels) {
            // actualizar datos
            devices.setValue(deviceModels);
        }
    };
    private final Observer<LinkedList<DeviceModel>> favDevicesObserver = new Observer<LinkedList<DeviceModel>>() {
        @Override
        public void onChanged(LinkedList<DeviceModel> deviceModels) {
            // actualizar datos
            LinkedList<DeviceModel> query = new LinkedList<>();
            for(DeviceModel deviceModel : deviceModels){
                if(sharedPreferences.getAll().containsKey(deviceModel.getDeviceID())){
                    query.add(deviceModel);
                }
            }
            favDevices.setValue(query);
        }
    };


    public MainViewModel(@NonNull Application application) {
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance();
        devices=new MutableLiveData<>();
        favDevices=new MutableLiveData<>();
        sharedPreferences=application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
    }

    /***************
     * ALL DEVICES *
     ****************/
    public MutableLiveData<LinkedList<DeviceModel>> getDevices() {
        return devices;
    }

    public void registerAllDevices() {
        sigfoxRepository.registerAllDevicesListener();
        sigfoxRepository.getAllDevices().observeForever(allDevicesObserver);
        sigfoxRepository.getAllDevices().observeForever(favDevicesObserver);
    }

    public void unregisterAllDevices(){
        sigfoxRepository.unregisterAllDevicesListener();
        sigfoxRepository.getAllDevices().removeObserver(allDevicesObserver);;
        sigfoxRepository.getAllDevices().removeObserver(favDevicesObserver);
    }

    /*************
     * FAVORITES *
     *************/
    public MutableLiveData<LinkedList<DeviceModel>> getFavDevices(){ return favDevices; }

    public void updateFavDevices(){
        if(devices.getValue()==null){return;} // Si es nulo se omite

        // actualizar datos
        LinkedList<DeviceModel> query = new LinkedList<>();
        for(DeviceModel deviceModel : devices.getValue()){
            if(sharedPreferences.getAll().containsKey(deviceModel.getDeviceID())){
                query.add(deviceModel);
            }
        }
        favDevices.setValue(query);
    }

    public boolean isFavorite(String device){
        return sharedPreferences.getBoolean(device, false);
    }

    public void add2Favorites(String device){
        // Añadir localmente a favoritos
        sharedPreferences
                .edit()
                .putBoolean(device, true)
                .apply();
        // Actualizar favoritos
        updateFavDevices();
    }

    public void removeFromFavorites(String device){
        // Eliminar localmente a favoritos
        sharedPreferences
                .edit()
                .remove(device)
                .apply();
        // Actualizar favoritos
        updateFavDevices();
    }

}
