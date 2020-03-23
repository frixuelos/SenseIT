package com.android.tfg.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.tfg.R;
import com.android.tfg.firebase.FirebaseSource;
import com.android.tfg.model.LoginUserModel;
import com.android.tfg.view.MainActivity;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.io.Serializable;

public class LoginUserViewModel extends AndroidViewModel {

    public LoginUserViewModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String passwd) {
        /*******************
        * COMPROBAR CAMPOS *
        ********************/
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.length()==0){
            // Email incorrecto
            Toast.makeText(getApplication().getApplicationContext(), getApplication().getResources().getString(R.string.errorLoginEmail), Toast.LENGTH_LONG).show();
            return;

        }else if(!(passwd.length()>5) || passwd.length()==0){
            // Contraseña demasiado corta
            Toast.makeText(getApplication().getApplicationContext(), getApplication().getResources().getString(R.string.errorLoginPasswd), Toast.LENGTH_LONG).show();
            return;
        }

        LoginUserModel currentUser=null;
        try {
            currentUser = new FirebaseSource().login(email, passwd); // loguearse y retornar el usuario
        } catch (FirebaseAuthInvalidUserException e1) {
            Toast.makeText(getApplication().getApplicationContext(), getApplication().getResources().getString(R.string.errorInvalidUser), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(currentUser!=null) {
                // Login con éxito
                Intent i = new Intent(getApplication().getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("currentUser", (Serializable) currentUser); // Se pasa el usuario logueado a la actividad principal
                getApplication().getApplicationContext().startActivity(i);
            }
        }


    }


}
