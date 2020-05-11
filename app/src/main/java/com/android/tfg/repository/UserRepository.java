package com.android.tfg.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class UserRepository {

    private FirebaseAuth firebaseAuth;
    private static UserRepository instance;
    private MutableLiveData<Task<AuthResult>> loginTask;

    public UserRepository(){
        firebaseAuth=FirebaseAuth.getInstance();
        loginTask=new MutableLiveData<>();
    }

    /*************************************
     * SINGLETON                         *
     * @return UserRepository Instance   *
     *************************************/
    public static UserRepository getInstance(){
        if(instance==null){
            instance = new UserRepository();
        }

        return instance;
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public MutableLiveData<Task<AuthResult>> getLogin(){
        return loginTask;
    }

    public void login(String email, String passwd) {
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(task -> loginTask.setValue(task));
    }

    public void logout(){
        firebaseAuth.signOut();
    }

}
