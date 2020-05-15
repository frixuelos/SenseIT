package com.android.tfg.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.model.ConfigModel;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.repository.SigfoxRepository;
import com.google.android.gms.tasks.Task;

public class UpdateSettingsViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<Task<Void>> updateSettingsResult;

    public UpdateSettingsViewModel(@NonNull Application application) {
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(application.getApplicationContext());
        updateSettingsResult=new MutableLiveData<>();
    }

    public void updateSettings(DeviceModel device, int sleepTime){
        device.getConfig().setSleepTime(sleepTime);
        sigfoxRepository.updateSettings(device);
        sigfoxRepository.getUpdateSettingsResult().observeForever(voidTask -> updateSettingsResult.setValue(voidTask));
    }

    public MutableLiveData<Task<Void>> getUpdateSettingsResult(){
        return this.updateSettingsResult;
    }

}
