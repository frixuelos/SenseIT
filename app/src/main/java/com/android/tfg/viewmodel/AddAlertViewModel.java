package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.model.AlertModel;
import com.android.tfg.repository.SigfoxRepository;

public class AddAlertViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private Context context;
    private MutableLiveData<AlertModel> deviceAlert;
    private Observer<AlertModel> deviceAlertListener = new Observer<AlertModel>() {
        @Override
        public void onChanged(AlertModel alertModel) {
            deviceAlert.setValue(alertModel);
        }
    };

    public AddAlertViewModel(@NonNull Application application){
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(application.getApplicationContext());
        deviceAlert=new MutableLiveData<>();
        context=application.getApplicationContext();
    }

    public void addUserAlert(AlertModel alert){
        this.sigfoxRepository.addUserAlert(alert);
    }

    public void registerDeviceAlert(String deviceID){
        this.sigfoxRepository.registerDeviceAlert(deviceID);
        this.sigfoxRepository.getDeviceAlert().observeForever(deviceAlertListener);
    }

    public void unregisterDeviceAlert(String deviceID){
        this.sigfoxRepository.unregisterDeviceAlert(deviceID);
    }

    public MutableLiveData<AlertModel> getDeviceAlert(){
        return this.deviceAlert;
    }

    public void removeDeviceAlert(AlertModel alert){
        this.sigfoxRepository.removeUserAlert(alert);
    }

}
