package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.repository.SigfoxRepository;

import java.util.LinkedList;

public class MainViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<LinkedList<DeviceModel>> devices, favDevices;
    private SharedPreferences sharedPreferencesFav;
    private DeviceModel nearDevice;

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
                if(sharedPreferencesFav.getAll().containsKey(deviceModel.getId())){
                    query.add(deviceModel);
                }
            }
            favDevices.setValue(query);
        }
    };


    public MainViewModel(@NonNull Application application) {
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(getApplication().getApplicationContext());
        devices=new MutableLiveData<>();
        favDevices=new MutableLiveData<>();
        sharedPreferencesFav =application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
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
            if(sharedPreferencesFav.getAll().containsKey(deviceModel.getId())){
                query.add(deviceModel);
            }
        }
        favDevices.setValue(query);
    }

    public boolean isFavorite(String device){
        return sharedPreferencesFav.getBoolean(device, false);
    }

    public void add2Favorites(String device){
        // Añadir localmente a favoritos
        sharedPreferencesFav
                .edit()
                .putBoolean(device, true)
                .apply();
        // Actualizar favoritos
        updateFavDevices();
    }

    public void removeFromFavorites(String device){
        // Eliminar localmente a favoritos
        sharedPreferencesFav
                .edit()
                .remove(device)
                .apply();
        // Actualizar favoritos
        updateFavDevices();
    }

    /************
     * UNIDADES *
     ************/
    public double convertTemp(double temp){
        Context context = getApplication().getApplicationContext();
        if(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitTemp), context.getString(R.string.defUnitTemp)).equals("ºF")){
            return Math.round((9*temp/5+32.0)*100)/100.0;
        }
        return temp;
    }

    public double convertPres(double pres){
        Context context = getApplication().getApplicationContext();
        if(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitPres), context.getString(R.string.defUnitPres)).equals("atm")){
            return Math.round(0.000987*pres*100)/100.0;
        }
        return pres;
    }

    public double convertUv(double uv){
        Context context = getApplication().getApplicationContext();
        if(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitUV), context.getString(R.string.defUnitUV)).equals("W/m2")){
            return Math.round(uv*10.0*100)/100.0;
        }
        return uv;
    }

    /*****************
     * HOME FRAGMENT *
     *****************/
    public DeviceModel getNear(double lat, double lng){
        if(nearDevice!=null){return nearDevice;}
        if(devices.getValue()==null){return new DeviceModel();}
        double diff = Double.MAX_VALUE;
        for(DeviceModel device : devices.getValue()){
            if(nearDevice==null){nearDevice=device;}
            double diff_tmp = Math.abs(device.getComputedLocation().getLat()-lat)+Math.abs(device.getComputedLocation().getLng()-lng);
            if(diff_tmp<=diff){
                diff=diff_tmp;
                nearDevice=device;
            }
        }
        return nearDevice;
    }

}
