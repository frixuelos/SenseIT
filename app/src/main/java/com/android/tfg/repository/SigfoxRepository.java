package com.android.tfg.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.LinkedList;

public class SigfoxRepository {

    private static SigfoxRepository instance;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference databaseReference = database.collection("devices");


    // Contiene todos los dispositivos
    private MutableLiveData<LinkedList<DeviceModel>> allDevices;
    private EventListener<QuerySnapshot> allDevicesListener = new EventListener<QuerySnapshot>(){
        @Override
         public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e){
            if(e!=null || value==null){ // Error
                Log.e("DATABASE", "allDevicesListener (SigfoxRepository)");
                return;
            }
            LinkedList<DeviceModel> query = new LinkedList<>();

            for(DocumentSnapshot doc : value.getDocuments()){
                String deviceID = doc.getId();
                MessageModel lastMessage = doc.get("lastMessage", MessageModel.class);
                query.add(new DeviceModel(deviceID, null, null, lastMessage));
            }

            allDevices.setValue(query);
        }
    };
    private ListenerRegistration allDevicesRegistration;

    // Contiene los mensajes
    private MutableLiveData<LinkedList<MessageModel>> messages;
    private ListenerRegistration messagesRegistration;


    private SigfoxRepository(){
        this.allDevices=new MutableLiveData<>();
        this.messages=new MutableLiveData<>();
    }

    /*************************************
     * SINGLETON                         *
     * @return SigfoxRepository Instance *
     *************************************/
    public static SigfoxRepository getInstance(){
        if(instance==null){
            instance = new SigfoxRepository();
        }

        return instance;
    }

    /***************
     * ALL DEVICES *
     ***************/
    public MutableLiveData<LinkedList<DeviceModel>> getAllDevices(){
        return this.allDevices;
    }

    public void registerAllDevicesListener(){
        allDevicesRegistration= databaseReference.addSnapshotListener(allDevicesListener);

    }

    public void unregisterAllDevicesListener(){
        if(allDevicesRegistration!=null){
            allDevicesRegistration.remove();
        }
    }

    /************
     * MESSAGES *
     ************/
    public MutableLiveData<LinkedList<MessageModel>> getMessages(){
        return this.messages;
    }

    public void registerMessagesFromDevice(String device, Timestamp since, Timestamp until){
        if(since==null | until==null){
            LinkedList<MessageModel> query = new LinkedList<>();
            messagesRegistration= databaseReference.document(device)
                    .collection("messages")
                    .whereGreaterThan("date", new Timestamp(new Date(new Date().getTime()-(24*60*60))))
                    .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException e) {
                            if(e!=null || querySnapshots==null){
                                Log.w("SigfoxRepository", "Listen failed", e);
                                return;
                            }

                            for(DocumentSnapshot doc : querySnapshots.getDocuments()){
                                query.add(doc.toObject(MessageModel.class));
                            }
                        }
                    });
            messages.setValue(query);
        }else{
            // Si existe otro listener lo eliminamos
            if(messagesRegistration!=null){messagesRegistration.remove();}

        }
    }

    public void unregisterMessagesFromDevice(){
        messagesRegistration.remove();
    }


}
