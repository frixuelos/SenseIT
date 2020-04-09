package com.android.tfg.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class MoreViewModel extends AndroidViewModel {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference("sigfox/messages");

    private MutableLiveData<LinkedList<MessageModel>> messages;

    public MoreViewModel(@NonNull Application application) {
        super(application);
        messages=new MutableLiveData<>();
    }

    public MutableLiveData<LinkedList<MessageModel>> getMessages(){
        return messages;
    }

    public void getMessagesFromDevice(String device){
        if(device.isEmpty()){return;} // por si el string esta vacio

        databaseReference.child(device).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinkedList<MessageModel> query = new LinkedList<>();
                for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los mensajes
                    /***************************
                     * SE OBTIENE CADA MENSAJE *
                     ***************************/
                    query.add(id.getValue(MessageModel.class)); // Añade el resultado
                }
                if(!query.isEmpty()){
                        messages.setValue(query);// Actualiza el MutableLiveData
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
