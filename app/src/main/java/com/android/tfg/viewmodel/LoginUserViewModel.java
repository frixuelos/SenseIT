package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.tfg.R;
import com.android.tfg.firebase.FirebaseSource;
import com.android.tfg.model.LoginUserModel;
import com.android.tfg.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;


public class LoginUserViewModel extends AndroidViewModel {

    public LoginUserViewModel(@NonNull Application application) {
        super(application);
    }
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Exception> exception=new MutableLiveData<>();

    public void login(String email, String passwd){
        /*******************
        * COMPROBAR CAMPOS *
        ********************/


        /*********
         * LOGIN *
         *********/
        firebaseAuth=FirebaseAuth.getInstance();
        Task<AuthResult> task = firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    goMain(new LoginUserModel(user.getUid(), user.getDisplayName(), user.getEmail()));
                }else{
                    setException(task.getException());
                }
            }
        });
    }

    public void goMain(LoginUserModel currentUser){
        Intent i = new Intent(getApplication().getApplicationContext(), MainActivity.class);
        i.putExtra("currentUser", currentUser);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.v("USER", currentUser.getMail());
        getApplication().getApplicationContext().startActivity(i);
    }

    public void setException(Exception e){
        this.exception.postValue(e);
    }

    public MutableLiveData<Exception> getException(){
        return this.exception;
    }

}
