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

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
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

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReferenceMessages = database.getReference("sigfox/messages");
    private MutableLiveData<LinkedList<MessageModel>> messages;
    private SharedPreferences sharedPreferences;
    private int TYPE;

    /*************
     * LISTENERS *
     *************/
    private ValueEventListener lastDayListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinkedList<MessageModel> query = new LinkedList<>();
            for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los mensajes
                /***************************
                 * SE OBTIENE CADA MENSAJE *
                 ***************************/
                MessageModel currentMessage = id.getValue(MessageModel.class);
                if(currentMessage==null){return;} // Si es nulo retorna
                if(24 * 60 * 60 > (System.currentTimeMillis()/1000L - currentMessage.getDate())){ // Si pertenece a las ultimas 24h
                    query.add(currentMessage);
                }
            }
            if(!query.isEmpty()){ // si no esta vacia la query retorna resultado
                messages.setValue(query); // Actualiza el MutableLiveData
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private ValueEventListener lastSixHoursListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinkedList<MessageModel> query = new LinkedList<>();
            for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los mensajes
                /***************************
                 * SE OBTIENE CADA MENSAJE *
                 ***************************/
                MessageModel currentMessage = id.getValue(MessageModel.class);
                if (currentMessage == null) {
                    return;
                } // Si es nulo retorna
                if (6 * 60 * 60 > (System.currentTimeMillis() / 1000L - currentMessage.getDate())) { // Si pertenece a las ultimas 6h
                    query.add(currentMessage);
                }
            }
            if (!query.isEmpty()) { // si no esta vacia la query retorna resultado
                messages.setValue(query); // Actualiza el MutableLiveData
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public MoreViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences=application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
        messages=new MutableLiveData<>();
        TYPE=24;
    }

    public MutableLiveData<LinkedList<MessageModel>> getMessages(){
        return messages;
    }

    public void registerMessagesFromDevice(String device){
        if(device==null){return;} // por si el string esta vacio
        switch(TYPE){
            case 6: databaseReferenceMessages.child(device).orderByKey().limitToLast(140).addValueEventListener(lastSixHoursListener); break;
            default: databaseReferenceMessages.child(device).orderByKey().limitToLast(140).addValueEventListener(lastDayListener); break; // Por defecto 24h
        }

    }

    public void unregisterMessagesFromDevice(String device){
        if(device==null){return;} // por si el string esta vacio
        switch(TYPE){
            case 6: databaseReferenceMessages.removeEventListener(lastSixHoursListener); break;
            default: databaseReferenceMessages.removeEventListener(lastDayListener); break;
        }
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
