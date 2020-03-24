package com.android.tfg.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.UserAlreadyExistsException;
import com.android.tfg.model.LoginUserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UserRepository {

    private DatabaseReference firebaseDatabase;
    private LinkedList<LoginUserModel> queryResult;

    public UserRepository(){
        this.firebaseDatabase=FirebaseDatabase.getInstance().getReference();
        this.queryResult=new LinkedList<>();
    }

    public void registerUser(LoginUserModel user) {
        if(user!=null && this.getUserByMail(user.getMail())==null) {
            // para evitar guardar el UID 2 veces se crea otro usuario sin UID
            this.firebaseDatabase.child("users").child(user.getUID()).setValue(new LoginUserModel(null, user.getName(), user.getMail()));
        }
    }

    public LoginUserModel getUserByMail(String mail){
        this.firebaseDatabase.child("users").orderByChild("mail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                queryResult.add(dataSnapshot.getValue(LoginUserModel.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LoginUserModel user=null;
        if(!queryResult.isEmpty()){
            user=queryResult.getFirst();
            queryResult.clear();
        }
        return user;
    }

}
