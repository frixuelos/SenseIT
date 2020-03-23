package com.android.tfg.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tfg.R;
import com.android.tfg.viewmodel.LoginUserViewModel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    LoginUserViewModel userViewModel;
    Button btnLogin;
    EditText etEmail, etPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configView(); // Configurar la vista
        initLoginUserViewModel(); // Configurar VM
        initLoginButton(); // Configurar boton login

    }

    private void initLoginButton(){
        final LoginActivity owner = this;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**************
                 * VALIDACION *
                 **************/
                String email = etEmail.getText().toString();
                String passwd = etPasswd.getText().toString();
                // EMAIL
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.length()==0){
                    // Email incorrecto
                    etEmail.setText("");
                    etEmail.setError(getResources().getString(R.string.errorTextEmail));
                    etPasswd.setText("");
                    etEmail.requestFocus();
                    return;
                }
                // PASSWORD
                if(!(passwd.length()>5) || passwd.length()==0){
                    // Contraseña demasiado corta
                    etEmail.setText("");
                    etPasswd.setError(getResources().getString(R.string.errorTextPasswd));
                    etPasswd.setText("");
                    etPasswd.requestFocus();
                    return;
                }

                /*********
                 * LOGIN *
                 *********/
                userViewModel.login(etEmail.getText().toString(), etPasswd.getText().toString());

                /**************************
                * OBSERVER DE EXCEPCIONES *
                ***************************/
                final Observer<Exception> obs = new Observer<Exception>() {
                    @Override
                    public void onChanged(Exception e) {
                        if (e != null){
                            if (e instanceof FirebaseAuthInvalidUserException) { // Usuario no valido
                                // Requerimos atencion a los campos
                                etEmail.setText("");
                                etEmail.setError(getResources().getString(R.string.errorLoginInvalidUser));
                                etPasswd.setText("");
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) { // Password no es correcto
                                // Requerimos atencion a los campos
                                etPasswd.setText("");
                                etPasswd.setError(getResources().getString(R.string.errorLoginInvalidPassword));
                            } else {
                                // Mostramos error de inicio de sesion
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorLogin), Toast.LENGTH_LONG);
                            }
                        }
                    }
                };
                userViewModel.getException().observe(owner, obs);
            }
        });
    }

    private void initLoginUserViewModel(){
        userViewModel = new ViewModelProvider(this).get(LoginUserViewModel.class);
    }

    private void configView(){
        constraintLayout = findViewById(R.id.login_layout);
        final Drawable drw = constraintLayout.getBackground();
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPasswd = (EditText)findViewById(R.id.etPasswd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setAlpha(0.8F);
        btnLogin.setVisibility(View.INVISIBLE);


        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    etEmail.setBackgroundColor(Color.BLACK);
                    etEmail.setAlpha(0.5F);
                    etPasswd.setBackgroundColor(Color.BLACK);
                    etPasswd.setAlpha(0.5F);
                    drw.setAlpha(50);
                    btnLogin.setVisibility(View.VISIBLE);
                }else{
                    drw.setAlpha(100);
                    btnLogin.setVisibility(View.INVISIBLE);
                }

            }
        });

        etPasswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    etPasswd.setBackgroundColor(Color.BLACK);
                    etPasswd.setAlpha(0.5F);
                    etEmail.setBackgroundColor(Color.BLACK);
                    etEmail.setAlpha(0.5F);
                    drw.setAlpha(50);
                    btnLogin.setVisibility(View.VISIBLE);
                }else{
                    drw.setAlpha(100);
                    btnLogin.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

}
