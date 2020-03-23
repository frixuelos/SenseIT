package com.android.tfg.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import com.android.tfg.model.LoginUserModel;
import com.android.tfg.view.LoginActivity;
import com.android.tfg.viewmodel.LoginUserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Time;

public class FirebaseSource {

    private FirebaseAuth firebaseAuth;

    public FirebaseSource(){
        this.firebaseAuth=FirebaseAuth.getInstance();
    }

    public void login(LoginUserViewModel vm, String email, String passwd) throws Exception {
        // Iniciamos sesion mediante email/passwd
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

            }
        });

    }

    public void logout(){
        firebaseAuth.signOut();
    }

}
