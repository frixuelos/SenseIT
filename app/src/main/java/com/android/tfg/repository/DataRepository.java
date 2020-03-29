package com.android.tfg.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.LoginUserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class DataRepository {

    private DatabaseReference firebaseDatabase;
    private LinkedList<DeviceModel> queryResult;

    public DataRepository(){
        this.firebaseDatabase= FirebaseDatabase.getInstance().getReference();
        this.queryResult=new LinkedList<>();
    }

    public MutableLiveData<LinkedList<DeviceModel>> getAllDevices(){
        /*this.firebaseDatabase.child("sigfox").child().child.equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                queryResult.add(dataSnapshot.getValue(LoginUserModel.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DeviceModel user=null;
        if(!queryResult.isEmpty()){
            user=queryResult.getFirst();
            queryResult.clear();
        }*/
        return null;
    }

}
