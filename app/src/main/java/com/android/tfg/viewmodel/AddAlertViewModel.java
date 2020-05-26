package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.model.AlertModel;
import com.android.tfg.repository.SigfoxRepository;
import com.android.tfg.math.Converter;

public class AddAlertViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private Context context;

    public AddAlertViewModel(@NonNull Application application){
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(application.getApplicationContext());
        context=application.getApplicationContext();
    }

    public void addUserAlert(AlertModel alert){
        this.sigfoxRepository.addUserAlert(alert);
    }

    public AlertModel getDeviceAlert(String id){
        return sigfoxRepository.getDeviceAlert(id);
    }

    public void removeDeviceAlert(AlertModel alert){
        this.sigfoxRepository.removeUserAlert(alert);
    }

    /************
     * UNIDADES *
     ************/
    private String getTempUnits(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitTemp), context.getString(R.string.defUnitTemp));
    }

    private String getPresUnits(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitPres), context.getString(R.string.defUnitPres));
    }

    private String getUvUnits(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.keyUnitUV), context.getString(R.string.defUnitUV));
    }

    public double convertTemp2Def(double temp){
        if(getTempUnits().equals(context.getString(R.string.KUnitTemp))){
            return Converter.toCelsius(temp);
        }
        return temp;
    }

    public double convertPres2Def(double pres){
        if(getPresUnits().equals(context.getString(R.string.ATMUnitPres))){
            return Converter.toHPA(pres);
        }
        return pres;
    }

    public double convertUv2Def(double uv){
        if(getUvUnits().equals(context.getString(R.string.WM2UnitUV))){
            return Converter.toMW(uv);
        }
        return uv;
    }

    public double convertTempFromDef(double temp){
        if(getTempUnits().equals(context.getString(R.string.KUnitTemp))){
            return Converter.toFarenheit(temp);
        }
        return temp;
    }

    public double convertPresFromDef(double pres){
        if(getPresUnits().equals(context.getString(R.string.ATMUnitPres))){
            return Converter.toATM(pres);
        }
        return pres;
    }

    public double convertUvFromDef(double uv){
        if(getUvUnits().equals(context.getString(R.string.WM2UnitUV))){
            return Converter.toW(uv);
        }
        return uv;
    }

}
