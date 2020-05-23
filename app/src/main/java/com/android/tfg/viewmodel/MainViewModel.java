package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.repository.SigfoxRepository;
import com.android.tfg.repository.UserRepository;
import com.android.tfg.view.math.Converter;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlUtil;

import java.util.LinkedList;

public class MainViewModel extends AndroidViewModel {

    private Context context;
    private SigfoxRepository sigfoxRepository;
    private UserRepository userRepository;
    private MutableLiveData<LinkedList<DeviceModel>> devices, favDevices;
    private SharedPreferences sharedPreferencesFav;
    private MutableLiveData<DeviceModel> nearDevice;

    /*************
     * OBSERVERS *
     *************/
    private final Observer<LinkedList<DeviceModel>> allDevicesObserver = new Observer<LinkedList<DeviceModel>>() {
        @Override
        public void onChanged(LinkedList<DeviceModel> deviceModels) {
            // actualizar datos
            devices.setValue(deviceModels);

            // actualizar dispositivo mas cercano
            if (nearDevice.getValue() != null) {
                for(DeviceModel deviceModel : deviceModels){
                    if(deviceModel.getId().equals(nearDevice.getValue().getId())){
                        nearDevice.setValue(deviceModel);
                        break;
                    }
                }
            }
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
        context=getApplication().getApplicationContext();
        sigfoxRepository=SigfoxRepository.getInstance(getApplication().getApplicationContext());
        userRepository=UserRepository.getInstance();
        devices=new MutableLiveData<>();
        favDevices=new MutableLiveData<>();
        sharedPreferencesFav=application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
        nearDevice=new MutableLiveData<>();
        userLocation=new MutableLiveData<>();
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
    public String getTempUnits(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitTemp), context.getString(R.string.defUnitTemp));
    }

    public String getPresUnits(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitPres), context.getString(R.string.defUnitPres));
    }

    public String getUvUnits(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitUV), context.getString(R.string.defUnitUV));
    }

    public double convertTemp(double temp){
        if(getTempUnits().equals(context.getString(R.string.KUnitTemp))){
            return Converter.toFarenheit(temp);
        }
        return temp;
    }

    public double convertPres(double pres){
        if(getPresUnits().equals(context.getString(R.string.ATMUnitPres))){
            return Converter.toATM(pres);
        }
        return pres;
    }

    public double convertUv(double uv){
        if(getUvUnits().equals(context.getString(R.string.WM2UnitUV))){
            return Converter.toW(uv);
        }
        return uv;
    }

    /*****************
     * HOME FRAGMENT *
     *****************/
    private MutableLiveData<LatLng> userLocation;
    public MutableLiveData<DeviceModel> getNear(double lat, double lng){
        if(nearDevice.getValue()!=null){return nearDevice;}
        if(devices.getValue()==null){nearDevice.setValue(new DeviceModel()); return nearDevice;}
        double diff = Double.MAX_VALUE;
        for(DeviceModel device : devices.getValue()){
            if(nearDevice.getValue()==null){nearDevice.setValue(device);}
            double diff_tmp = Math.abs(device.getComputedLocation().getLat()-lat)+Math.abs(device.getComputedLocation().getLng()-lng);
            if(diff_tmp<=diff){
                diff=diff_tmp;
                nearDevice.setValue(device);
            }
        }

        return nearDevice;
    }

    public void setLocation(double lat, double lng){
        this.userLocation.setValue(new LatLng(lat,lng));
    }

    public MutableLiveData<LatLng> getUserLocation(){
        return this.userLocation;
    }

    /*********
     * ADMIN *
     *********/
    public boolean isLoggedIn(){
        return userRepository.getCurrentUser()!=null;
    }

}
