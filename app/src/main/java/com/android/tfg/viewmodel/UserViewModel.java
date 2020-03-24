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
import com.android.tfg.view.LoginActivity;
import com.android.tfg.view.MainActivity;
import com.android.tfg.view.RegisterActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.io.Serializable;


public class UserViewModel extends AndroidViewModel {

    public UserViewModel(@NonNull Application application) {
        super(application);
    }
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private MutableLiveData<Exception> exception=new MutableLiveData<>();

    public void login(String email, String passwd){
        /*********
         * LOGIN *
         *********/
        if(firebaseAuth==null){
            firebaseAuth=FirebaseAuth.getInstance();
        }
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

    public void logout(){
        if(firebaseAuth==null){
            firebaseAuth=FirebaseAuth.getInstance();
        }
        firebaseAuth.signOut();
        goLogin();
    }

    public void register(String email, String passwd){
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        if(firebaseAuth==null){
            firebaseAuth=firebaseAuth.getInstance();
        }
        if(mDatabase==null){
            mDatabase= FirebaseDatabase.getInstance().getReference();
        }

        firebaseAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser(); // Usuario registrado.
                    LoginUserModel userModel = new LoginUserModel(user.getUid(), user.getDisplayName(), user.getEmail());
                    // Registrar en la BBDD
                    mDatabase.child("users").child(userModel.getUID()).setValue(userModel);

                    goMain(userModel);
                }else{
                    setException(task.getException());
                }
            }
        });
    }

    public void goRegister(String email, String passwd){
        Intent i = new Intent(getApplication().getApplicationContext(), RegisterActivity.class);
        i.putExtra("email", email);
        i.putExtra("passwd", passwd);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().getApplicationContext().startActivity(i);

    }

    public void goLogin(){
        Intent i = new Intent(getApplication().getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().getApplicationContext().startActivity(i);
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
