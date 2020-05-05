package com.android.tfg.repository;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.R;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SigfoxRepository {

    private static SigfoxRepository instance;
    private Context context;
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
                /* si se ha establecido un nombre
                String name = null;
                if(doc.contains("name")){
                    name=doc.getString("name");
                }
                DeviceModel device = new DeviceModel(deviceID, doc.getString("type"), name, lastMessage);*/
                DeviceModel device = doc.toObject(DeviceModel.class);
                if(device==null){ // Si es nulo mostramos error
                    Log.e("DATABASE", "allDevicesListener (SigfoxRepository) can't get device");
                    return;
                }
                device.setId(deviceID);

                if(device.getName()==null) {
                    // Se trata de descubrir el nombre de la posicion si no tiene nombre
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(device.getSite().latitude, device.getSite().longitude, 1);
                        Log.w("ADDRESSES", addresses.toString());
                        if (addresses.isEmpty()) {
                            device.setName(deviceID);
                        } else {
                            device.setName(String.format(context.getString(R.string.locationFormat), addresses.get(0).getLocality(), addresses.get(0).getSubAdminArea()));
                            // Tambien lo guardamos en la BBDD
                            doc.getReference().update("name", device.getName());
                        }
                    } catch (IOException exc) {
                        // No se pudo encontrar una dirección se establece nulo
                        Log.w("DATABASE", "allDevicesListener (SigfoxRepository) can't get device location-based name");
                    }
                }

                query.add(device);
            }

            allDevices.setValue(query);
        }
    };
    private ListenerRegistration allDevicesRegistration;

    // Contiene los mensajes
    private MutableLiveData<LinkedList<MessageModel>> messages;
    private ListenerRegistration messagesRegistration;


    private SigfoxRepository(Context context){
        this.allDevices=new MutableLiveData<>();
        this.messages=new MutableLiveData<>();
        this.context=context;
    }

    /*************************************
     * SINGLETON                         *
     * @return SigfoxRepository Instance *
     *************************************/
    public static SigfoxRepository getInstance(Context context){
        if(instance==null){
            instance = new SigfoxRepository(context);
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
        // Si existe otro registrado lo eliminamos
        if(allDevicesRegistration!=null){
            allDevicesRegistration.remove();
        }
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

    public void registerMessagesFromDevice(String device, Date since, Date until){
        // Si existe otro listener lo eliminamos
        if(messagesRegistration!=null){messagesRegistration.remove();}

        // Filtro por defecto (24 ultimas horas)
        if(since==null || until==null){
            Date lastDay = new Date((new Date()).getTime()-(24*60*60*1000));
            Log.v("since", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(lastDay));
            messagesRegistration=databaseReference.document(device)
                    .collection("messages")
                    .orderBy("date", Query.Direction.ASCENDING)
                    .whereGreaterThanOrEqualTo("date", lastDay)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException e) {
                            if(e!=null || querySnapshots==null){
                                Log.w("SigfoxRepository", "Listen failed", e);
                                return;
                            }
                            LinkedList<MessageModel> query = new LinkedList<>();
                            for(DocumentSnapshot doc : querySnapshots.getDocuments()){
                                query.add(doc.toObject(MessageModel.class));
                            }
                            messages.setValue(query);
                            Log.v("Size", String.valueOf(messages.getValue().size()));
                        }
                    });
        }else{ // Rango valido
            messagesRegistration=databaseReference.document(device)
                    .collection("messages")
                    .orderBy("date", Query.Direction.ASCENDING)
                    .whereGreaterThanOrEqualTo("date", since)
                    .whereLessThanOrEqualTo("date", until)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException e) {
                            if(e!=null || querySnapshots==null){
                                Log.w("SigfoxRepository", "Listen failed", e);
                                return;
                            }
                            LinkedList<MessageModel> query = new LinkedList<>();
                            for(DocumentSnapshot doc : querySnapshots.getDocuments()){
                                query.add(doc.toObject(MessageModel.class));
                            }
                            messages.setValue(query);
                        }
                    });
        }
    }

    public void unregisterMessagesFromDevice(){
        if(messagesRegistration!=null){
            messagesRegistration.remove();
        }
    }


}
