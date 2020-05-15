package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.tfg.model.DeviceModel;
import com.android.tfg.repository.SigfoxRepository;
import com.google.android.gms.tasks.Task;

public class EditNameViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<Task<Void>> editNameResult;
    private DeviceModel device;

    public EditNameViewModel(Application application){
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(application.getApplicationContext());
        editNameResult=new MutableLiveData<>();
        device=new DeviceModel();
    }

    public void setDevice(DeviceModel device){
        this.device=device;
    }

    public DeviceModel getDevice(){
        return this.device;
    }

    public void editName(DeviceModel device){
        sigfoxRepository.editName(device);
        sigfoxRepository.getEditNameResult().observeForever(voidTask -> editNameResult.setValue(voidTask));
    }

    public MutableLiveData<Task<Void>> getEditNameResult(){
        return editNameResult;
    }



}
