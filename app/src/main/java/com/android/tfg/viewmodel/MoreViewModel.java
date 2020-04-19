package com.android.tfg.viewmodel;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.android.tfg.repository.SigfoxRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.rpc.Help;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class MoreViewModel extends AndroidViewModel {

    private SigfoxRepository sigfoxRepository;
    private MutableLiveData<LinkedList<MessageModel>> messages;
    private SharedPreferences sharedPreferences;

    /*************
     * OBSERVERS *
     *************/
    private final Observer<LinkedList<MessageModel>> messagesObserver = new Observer<LinkedList<MessageModel>>() {
        @Override
        public void onChanged(LinkedList<MessageModel> messageModels) {
            messages.setValue(messageModels);
        }
    };


    public MoreViewModel(@NonNull Application application) {
        super(application);
        sigfoxRepository=SigfoxRepository.getInstance(getApplication().getApplicationContext());
        sharedPreferences=application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
        messages=new MutableLiveData<>();
    }

    public MutableLiveData<LinkedList<MessageModel>> getMessages(){
        return messages;
    }

    public void registerMessagesFromDevice(String device){
        if(device==null){return;} // por si el string esta vacio
        sigfoxRepository.registerMessagesFromDevice(device, null, null);
        sigfoxRepository.getMessages().observeForever(messagesObserver);
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
        return sharedPreferences.getBoolean(device, false);
    }

    public void add2Favorites(String device){
        // Añadir localmente a favoritos
        sharedPreferences
                .edit()
                .putBoolean(device, true)
                .apply();
    }

    public void removeFromFavorites(String device){
        // Eliminar localmente a favoritos
        sharedPreferences
                .edit()
                .remove(device)
                .apply();
    }

}
