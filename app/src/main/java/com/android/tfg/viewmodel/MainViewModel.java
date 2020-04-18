package com.android.tfg.viewmodel;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParser;
import com.google.rpc.Help;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MainViewModel extends AndroidViewModel {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference("sigfox");
    private MutableLiveData<LinkedList<DeviceModel>> devices, favDevices;
    private SharedPreferences sharedPreferences;

    /*************
     * LISTENERS *
     *************/
    private ValueEventListener allDevicesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinkedList<DeviceModel> query = new LinkedList<>();
            for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los IDs
                final String deviceID = id.getKey(); // Obtiene el ID
                /********************************
                 * SE OBTIENE EL ULTIMO MENSAJE *
                 ********************************/
                query.add(new DeviceModel(deviceID, null, null, id.getValue(MessageModel.class))); // Añade el resultado
            }
            devices.setValue(query); // Actualiza el MutableLiveData
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private ValueEventListener favoritesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinkedList<DeviceModel> query = new LinkedList<>();
            for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los IDs
                final String deviceID = id.getKey(); // Obtiene el ID
                /********************************
                 * SE OBTIENE EL ULTIMO MENSAJE *
                 ********************************/
                if (sharedPreferences.getAll().containsKey(deviceID)) { // solo si esta en favoritos se agrega
                    query.add(new DeviceModel(deviceID, null, null, id.getValue(MessageModel.class))); // Añade el resultado
                }
                favDevices.setValue(query); // Actualiza el MutableLiveData
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public MainViewModel(@NonNull Application application) {
        super(application);
        devices=new MutableLiveData<>();
        favDevices=new MutableLiveData<>();
        sharedPreferences=application.getApplicationContext().getSharedPreferences(application.getApplicationContext().getString(R.string.favoritesPreferences), Context.MODE_PRIVATE);
    }

    public MutableLiveData<LinkedList<DeviceModel>> getDevices() {
        return devices;
    }

    public void registerAllDevices() {
        databaseReference.child("last").addValueEventListener(allDevicesListener);
    }

    public void unregisterAllDevices(){
        databaseReference.child("last").removeEventListener(allDevicesListener);
    }

    public MutableLiveData<LinkedList<DeviceModel>> getFavDevices(){ return favDevices; }

    public void registerAllFavorites(){
        // Puesto que es una consulta "puntual" se elimina tambien el listener
        databaseReference.child("last").addListenerForSingleValueEvent(favoritesListener);
        databaseReference.child("last").removeEventListener(favoritesListener);
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
