package com.android.tfg.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class RegisterActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    UserViewModel userViewModel;
    Button btnRegister;
    EditText etEmailRegister, etPasswdRegister, etRePasswdRegister;
    ProgressBar pbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configView(); // Configurar la vista
        initUserViewModel(); // Configurar VM
        initRegisterButton(); // Configurar boton login
    }

    private void initRegisterButton(){
        final RegisterActivity owner = this;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*************************
                 * SING UP (MAIL/PASSWD) *
                 *************************/
                register(owner); // De esta manera hacemos reusable el código de login para la pulsacion intro en el teclado
            }
        });
    }

    private void register(RegisterActivity owner){
        /**************
         * VALIDACION *
         **************/
        String email = etEmailRegister.getText().toString();
        String passwd = etPasswdRegister.getText().toString();
        String passwd2 = etRePasswdRegister.getText().toString();

        // EMAIL
        if(email.length()==0)   {
            etEmailRegister.setError(getResources().getString(R.string.errorEmptyLogin));
            etEmailRegister.requestFocus();
            if(passwd.length()==0){
                etPasswdRegister.setError(getResources().getString(R.string.errorEmptyLogin));
                etRePasswdRegister.setError(getResources().getString(R.string.errorEmptyLogin));
            }
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // Email incorrecto
            etEmailRegister.setText("");
            etEmailRegister.setError(getResources().getString(R.string.errorTextEmail));
            etPasswdRegister.setText("");
            etEmailRegister.requestFocus();
            return;
        }

        // PASSWORD
        if(passwd.length()==0) {
            etPasswdRegister.setError(getResources().getString(R.string.errorEmptyLogin));
            etPasswdRegister.requestFocus();
            if(email.length()==0){
                etEmailRegister.setError(getResources().getString(R.string.errorEmptyLogin));
            }
            return;
        }
        if(!(passwd.length()>5)){
            // Contraseña demasiado corta
            etPasswdRegister.setError(getResources().getString(R.string.errorTextPasswd));
            etPasswdRegister.setText("");
            etRePasswdRegister.setText("");
            etPasswdRegister.requestFocus();
            return;
        }

        // RE-PASSWORD
        if(!passwd.equals(passwd2)){
            etPasswdRegister.setError(getResources().getString(R.string.errEqualPasswd));
            etPasswdRegister.requestFocus();
            etPasswdRegister.setText("");
            etRePasswdRegister.setText("");
            return;
        }

        /**************************
         * OBSERVER DE EXCEPCIONES *
         ***************************/
        final Observer<Exception> obs = new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                if (e != null){
                    if (e instanceof FirebaseAuthInvalidCredentialsException) { // Credenciales no validas (email malformed)
                        // Requerimos atencion a los campos
                        etEmailRegister.setText("");
                        etEmailRegister.setError(getResources().getString(R.string.errorLoginInvalidUser));
                        etEmailRegister.requestFocus();
                        etPasswdRegister.setText("");
                        etRePasswdRegister.setText("");
                        pbRegister.setVisibility(View.INVISIBLE);
                    } else if (e instanceof FirebaseAuthWeakPasswordException) { // Password debil
                        // Requerimos atencion a los campos
                        etPasswdRegister.setText("");
                        etRePasswdRegister.setText("");
                        etPasswdRegister.setError(getResources().getString(R.string.errorWeakPasswd));
                        etPasswdRegister.requestFocus();
                        pbRegister.setVisibility(View.INVISIBLE);
                    } else if(e instanceof FirebaseAuthUserCollisionException){ // Ya existe el usuario
                        etEmailRegister.setError(getResources().getString(R.string.errorRegisterCollision));
                        etEmailRegister.requestFocus();
                        etPasswdRegister.setText("");
                        etRePasswdRegister.setText("");
                        pbRegister.setVisibility(View.INVISIBLE);
                    }else{
                        // Mostramos error de inicio de sesion
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorRegister), Toast.LENGTH_LONG);
                        pbRegister.setVisibility(View.INVISIBLE);
                    }
                    e=null;
                }
            }
        };
        userViewModel.getException().observe(owner, obs);

        /************
         * REGISTER *
         ************/
        pbRegister.setVisibility(View.VISIBLE); // ProgressBar
        userViewModel.register(etEmailRegister.getText().toString(), etPasswdRegister.getText().toString());
    }

    private void initUserViewModel(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void configView(){
        /********************
         * INICIALIZACIONES *
         ********************/
        constraintLayout = findViewById(R.id.login_layout);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswdRegister = findViewById(R.id.etPasswdRegister);
        etRePasswdRegister = findViewById(R.id.etRePasswdRegister);
        btnRegister = findViewById(R.id.btnRegister);
        pbRegister = findViewById(R.id.pbRegister);
        pbRegister.setIndeterminate(true);

        /**********
         * ESTILO *
         **********/
        etEmailRegister.setBackgroundColor(Color.BLACK);
        etEmailRegister.setAlpha(0.5F);
        etPasswdRegister.setBackgroundColor(Color.BLACK);
        etPasswdRegister.setAlpha(0.5F);
        etRePasswdRegister.setBackgroundColor(Color.BLACK);
        etRePasswdRegister.setAlpha(0.5F);

        /**************************************************
         * OBTENER EMAIL/PASSORD DE LA PANTALLA DE INICIO *
         **************************************************/
        String email = getIntent().getStringExtra("email");
        String passwd = getIntent().getStringExtra("passwd");
        /*************************************************/
        if(email.length()!=0 && passwd.length()==0){ // Introdujo el email
            etEmailRegister.setText(email);
            etPasswdRegister.requestFocus();
        }else if(email.length()!=0 && passwd.length()!=0){ // Se introdujo la contraseña y el email
            etEmailRegister.setText(email);
            etPasswdRegister.setText(passwd);
            etRePasswdRegister.requestFocus();
        }else{ // No se introdujo nada
            etEmailRegister.requestFocus();
        }

    }

}
