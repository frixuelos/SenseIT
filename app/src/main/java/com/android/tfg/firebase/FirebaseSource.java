package com.android.tfg.firebase;

import androidx.annotation.NonNull;

import com.android.tfg.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseSource {

    private FirebaseAuth firebaseAuth;

    public FirebaseSource(){
        this.firebaseAuth=FirebaseAuth.getInstance();
    }

    public void login(UserViewModel vm, String email, String passwd) throws Exception {
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
