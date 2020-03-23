package com.android.tfg.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.model.LoginUserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseSource {

    private FirebaseAuth firebaseAuth;
    private LoginUserModel currentUser;
    private Exception exception;

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public FirebaseSource(){
        this.firebaseAuth=FirebaseAuth.getInstance();
        this.currentUser=null;
        this.exception=null;
    }

    public LoginUserModel login(String email, String passwd) throws Exception {
        final Exception[] e = {null};
        if(currentUser!=null)   return currentUser; // Si ya ha iniciado sesion
        // Iniciamos sesion mediante email/passwd
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ // la tarea finaliza con exito
                    // Login realizado con exito
                    // Se establece el usuario actual
                    setCurrentUser(
                            new LoginUserModel(
                                    firebaseAuth.getCurrentUser().getUid(),
                                    firebaseAuth.getCurrentUser().getDisplayName(),
                                    firebaseAuth.getCurrentUser().getEmail()
                            )
                    );
                }else{
                    e[0]=task.getException(); // En caso de no haber finalizado con exito, lanza una excepcion
                    Log.v("LOL", task.getException().getMessage());
                }
            }
        });

        if(e[0]!=null){
            throw e[0];
        }
        return currentUser;
    }

    public void logout(){
        firebaseAuth.signOut();
        this.currentUser=null;
    }

    public LoginUserModel getCurrentUser(){
        return this.currentUser;
    }


    public void setCurrentUser(LoginUserModel currentUser) {
        this.currentUser = currentUser;
    }

}
