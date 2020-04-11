package com.android.tfg.viewmodel;

import android.app.Application;
import android.os.Message;
import android.util.Log;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class MoreViewModel extends AndroidViewModel implements ValueEventListener{

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference("sigfox/messages");
    private MutableLiveData<LinkedList<MessageModel>> messages;

    /**********************
     * TIPO DE LISTENER   *
     * 1 => 6h            *
     * 0 => 24h           *
     * -1 => all          *
     **********************/
    private int TYPE;

    public MoreViewModel(@NonNull Application application) {
        super(application);
        messages=new MutableLiveData<>();
        this.TYPE=0;
    }

    public MutableLiveData<LinkedList<MessageModel>> getMessages(){
        return messages;
    }

    public void getMessagesFromDevice(String device){
        if(device==null){return;} // por si el string esta vacio
        databaseReference.child(device).orderByKey().limitToLast(140).addValueEventListener(this);
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    /***********************
     * LISTENERS           *
     * @param dataSnapshot *
     ***********************/
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        LinkedList<MessageModel> query = new LinkedList<>();
        for (DataSnapshot id : dataSnapshot.getChildren()) { // Itera los mensajes
            /***************************
             * SE OBTIENE CADA MENSAJE *
             ***************************/
            MessageModel currentMessage = id.getValue(MessageModel.class);
            if(currentMessage==null){return;} // Si es nulo retorna
            /************************
             * FILTROS DE SELECCION *
             ************************/
            if(this.TYPE==0){ // 24h
                if(24 * 60 * 60 > (System.currentTimeMillis()/1000L - currentMessage.getDate())){
                    query.add(currentMessage);
                }
            }else if(this.TYPE==1){ // 6h
                if(6 * 60 * 60>(System.currentTimeMillis()/1000L - currentMessage.getDate())) {
                    query.add(currentMessage); // Añade el resultado
                }
            }else{ // todos
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
}
