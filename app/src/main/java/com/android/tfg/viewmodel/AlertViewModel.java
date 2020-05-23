package com.android.tfg.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.tfg.model.AlertModel;
import com.android.tfg.repository.SigfoxRepository;

import java.util.ArrayList;
import java.util.List;

public class AlertViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<ArrayList<AlertModel>> userAlerts;
    private final Observer<List<AlertModel>> obsUserAlerts = new Observer<List<AlertModel>>() {
        @Override
        public void onChanged(List<AlertModel> alertModels) {
            userAlerts.setValue((ArrayList<AlertModel>) alertModels);
        }
    };

    public AlertViewModel(@NonNull Application application) {
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(getApplication().getApplicationContext());
        userAlerts=new MutableLiveData<>();
    }

    public void registerUserAlerts(){
        this.sigfoxRepository.registerUserAlerts();
        this.sigfoxRepository.getUserAlerts().observeForever(obsUserAlerts);
    }

    public void unregisterUserAlerts(){
        this.sigfoxRepository.unregisterUserAlerts();
        this.sigfoxRepository.getUserAlerts().removeObserver(obsUserAlerts);
    }

    public MutableLiveData<ArrayList<AlertModel>> getUserAlerts(){
        return this.userAlerts;
    }

    public void removeUserAlert(AlertModel alert){
        this.sigfoxRepository.removeUserAlert(alert);
    }

    public void clearUserAlerts(){
        this.sigfoxRepository.clearUserAlerts();
    }

    public void addUserAlert(AlertModel alert){
        this.sigfoxRepository.addUserAlert(alert);
    }
}
