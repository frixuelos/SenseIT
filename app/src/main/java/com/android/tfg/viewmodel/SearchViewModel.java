package com.android.tfg.viewmodel;

import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.android.tfg.model.DeviceModel;
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

public class SearchViewModel extends AndroidViewModel {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference("sigfox");
    private MutableLiveData<LinkedList<DeviceModel>> devices;

    private ValueEventListener allDevicesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinkedList<DeviceModel> query = new LinkedList<>();
            double lat=0.0, lng=0.0;
            for (DataSnapshot type : dataSnapshot.getChildren()) { // Itera los tipos
                for (DataSnapshot id : type.getChildren()) { // Itera los IDs
                    String deviceID = id.getKey(); // Obtiene el ID
                    for(DataSnapshot message : id.getChildren()){ // Itera mensajes
                        for(DataSnapshot info : message.getChildren()){ // Itera info mensaje
                            if(info.getKey().equals("computedLocation")){
                                for(DataSnapshot computedLocation : info.getChildren()){ // Itera computedLocation
                                    if(computedLocation.getKey().equals("lat")){
                                        lat=computedLocation.getValue(Float.class);
                                    }
                                    if(computedLocation.getKey().equals("lng")){
                                        lng=computedLocation.getValue(Float.class);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    query.add(new DeviceModel(deviceID, null, null, new LatLng(lat, lng))); // Añade el resultado
                }
            }
            devices.setValue(query); // Actualiza el MutableLiveData
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener searchDevicesListener = null;


    public SearchViewModel(@NonNull Application application) {
        super(application);
        devices=new MutableLiveData<>();
    }

    public MutableLiveData<LinkedList<DeviceModel>> getDevices() {
        return devices;
    }

    public void setDevices(MutableLiveData<LinkedList<DeviceModel>> devices) {
        this.devices = devices;
    }

    public void showDevices(final String search) {
        if (search.isEmpty()) {
            if(this.searchDevicesListener!=null){
                databaseReference.removeEventListener(this.searchDevicesListener);
                this.searchDevicesListener=null;
            }
            databaseReference.addValueEventListener(this.allDevicesListener); // Establecemos
        }else{
            databaseReference.removeEventListener(this.allDevicesListener);
            if(this.searchDevicesListener!=null){
                databaseReference.removeEventListener(this.searchDevicesListener);
            }
            this.searchDevicesListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    LinkedList<DeviceModel> query = new LinkedList<>();
                    for (DataSnapshot type : dataSnapshot.getChildren()) { // Itera los tipos
                        for (DataSnapshot id : type.getChildren()) { // Itera los IDs
                            String deviceID = id.getKey(); // Obtiene el ID
                            if(deviceID.contains(search)){
                                query.add(new DeviceModel(deviceID, null, null, null)); // Añade el resultado
                            }
                        }
                    }
                    devices.setValue(query); // Actualiza el MutableLiveData
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }
    }
}
