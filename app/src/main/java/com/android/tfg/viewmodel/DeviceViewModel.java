package com.android.tfg.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.model.DeviceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.LinkedList;

public class DeviceViewModel extends AndroidViewModel {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String DEFAULT_DEVICE_TYPE = "5e4847b93e09fa0d7b3c923e";
    private final String DEFAULT_REFERENCE = "sigfox";

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference(DEFAULT_REFERENCE).child(DEFAULT_DEVICE_TYPE);
    }

    public MutableLiveData<LinkedList<DeviceModel>> getDevices(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    String id = child.getKey();
                    Log.v("DEVICE_ID", id);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }

}
