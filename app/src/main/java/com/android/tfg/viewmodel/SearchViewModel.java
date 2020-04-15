package com.android.tfg.viewmodel;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
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

public class SearchViewModel extends AndroidViewModel {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference("sigfox");
    private MutableLiveData<LinkedList<DeviceModel>> devices;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        devices=new MutableLiveData<>();
    }

    public MutableLiveData<LinkedList<DeviceModel>> getDevices() {
        return devices;
    }

    public void setDevicesListener() {
        databaseReference.child("last").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinkedList<DeviceModel> query = new LinkedList<>();
                for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los IDs
                    final String deviceID = id.getKey(); // Obtiene el ID
                    /********************************
                     * SE OBTIENE EL ULTIMO MENSAJE *
                     ********************************/
                    query.add(new DeviceModel(deviceID, null, null, id.getValue(MessageModel.class))); // Añade el resultado al query
                }
                devices.setValue(query); // Actualiza el MutableLiveData
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
