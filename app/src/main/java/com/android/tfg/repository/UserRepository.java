package com.android.tfg.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

public class UserRepository {

    private FirebaseAuth firebaseAuth;
    private static UserRepository instance;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference databaseReference = database.collection("users");
    private MutableLiveData<Task<AuthResult>> loginTask;
    private MutableLiveData<Task<Void>> resetPasswordTask;

    public UserRepository(){
        firebaseAuth=FirebaseAuth.getInstance();
        loginTask=new MutableLiveData<>();
        resetPasswordTask=new MutableLiveData<>();
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
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(task -> {
            loginTask.setValue(task);
            if(firebaseAuth.getCurrentUser()!=null){ // Login exito
                // Insertar si no existe el usuario
                databaseReference.document(firebaseAuth.getCurrentUser().getUid()).get().continueWith(new Continuation<DocumentSnapshot, Object>() {
                    @Override
                    public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        if(task.isSuccessful()){ // Tarea completada con exito

                            DocumentSnapshot doc = task.getResult();
                            if(doc==null || !doc.exists()){ // No existe el usuario en la BBDD
                                HashMap<String,String> user = new HashMap<>();
                                user.put("email", firebaseAuth.getCurrentUser().getEmail());
                                databaseReference.document(firebaseAuth.getCurrentUser().getUid()).set(user);
                            }

                        }
                        return null;
                    }
                });
            }
        });
    }

    public void logout(){
        firebaseAuth.signOut();
    }

    public void resetPassword(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                resetPasswordTask.setValue(task);
            }
        });
    }

    public MutableLiveData<Task<Void>> getResetPassword(){
        return resetPasswordTask;
    }

}
