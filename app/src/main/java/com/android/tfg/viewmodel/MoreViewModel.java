package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.android.tfg.R;
import com.android.tfg.model.MessageModel;
import com.android.tfg.repository.SigfoxRepository;

import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;

public class MoreViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<LinkedList<MessageModel>> messages;
    private SharedPreferences sharedPreferencesFav;
    private SharedPreferences sharedPreferences;

    /*********
     * DATES *
     *********/

    private long since, until;

    public long getSince() {
        return since;
    }

    public void setSince(long since) {
        this.since = since;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }



    /*************
     * OBSERVERS *
     *************/
    private final Observer<LinkedList<MessageModel>> messagesObserver = new Observer<LinkedList<MessageModel>>() {
        @Override
        public void onChanged(LinkedList<MessageModel> messageModels) {
            messages.setValue(messageModels);
        }
    };
    private boolean MESSAGES_INIT;


    public MoreViewModel(@NonNull Application application) {
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(getApplication().getApplicationContext());
        sharedPreferencesFav=application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        messages=new MutableLiveData<>();
        MESSAGES_INIT=false;
        until=new Date().getTime();
        since=new Date(until-24*60*60*1000L).getTime();
    }

    public MutableLiveData<LinkedList<MessageModel>> getMessages(){
        return messages;
    }

    public void registerMessagesFromDevice(String device){
        if(device==null){return;} // por si el string esta vacio

        // actualizar seleccion
        setSince(since);

        if(since==until){ // Se ha seleccionado un dia unicamente
            setUntil(since+24*60*60*1000L); // actualizar seleccion
            sigfoxRepository.registerMessagesFromDevice(device, new Date(getSince()), new Date(getUntil()));
        }else{
            setUntil(until); // actualizar seleccion
            sigfoxRepository.registerMessagesFromDevice(device, new Date(getSince()), new Date(getUntil()));
        }

        if(!MESSAGES_INIT){
            sigfoxRepository.getMessages().observeForever(messagesObserver);
        }
    }

    public void unregisterMessagesFromDevice(String device){
        if(device==null){return;} // por si el string esta vacio
        sigfoxRepository.unregisterMessagesFromDevice();
        sigfoxRepository.getMessages().removeObserver(messagesObserver);
    }

    /*************
     * FAVORITOS *
     *************/
    public boolean isFavorite(String device){
        return sharedPreferencesFav.getBoolean(device, false);
    }

    public void add2Favorites(String device){
        // Añadir localmente a favoritos
        sharedPreferencesFav
                .edit()
                .putBoolean(device, true)
                .apply();
    }

    public void removeFromFavorites(String device){
        // Eliminar localmente a favoritos
        sharedPreferencesFav
                .edit()
                .remove(device)
                .apply();
    }

    /************
     * UNIDADES *
     ************/
    public double convertTemp(double temp){
        Context context = getApplication().getApplicationContext();
        if(this.sharedPreferences.getString(context.getString(R.string.keyUnitTemp), context.getString(R.string.defUnitTemp)).equals("ºF")){
            return Math.round((9*temp/5+32.0)*100)/100.0;
        }
        return temp;
    }

    public double convertPres(double pres){
        Context context = getApplication().getApplicationContext();
        if(this.sharedPreferences.getString(context.getString(R.string.keyUnitPres), context.getString(R.string.defUnitPres)).equals("atm")){
            return Math.round(0.000987*pres*100)/100.0;
        }
        return pres;
    }

    public double convertUv(double uv){
        Context context = getApplication().getApplicationContext();
        if(this.sharedPreferences.getString(context.getString(R.string.keyUnitUV), context.getString(R.string.defUnitUV)).equals("W/m2")){
            return Math.round(uv*10.0*100)/100.0;
        }
        return uv;
    }

}
