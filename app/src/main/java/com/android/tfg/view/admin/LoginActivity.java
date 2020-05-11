package com.android.tfg.view.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.databinding.ActivityLoginBinding;
import com.android.tfg.viewmodel.LoginViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    public void configViewModel(){
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        final Observer<Task<AuthResult>> obs = authResultTask -> {
            if(authResultTask.isSuccessful()){
                // Login ha tenido exito
                go2Admin();
            }else{
                // Login ha fallado
                binding.loadStub.setVisibility(View.GONE);
                Log.w("SUCCESS", String.valueOf(authResultTask.isSuccessful()));
                Snackbar.make(binding.getRoot(), authResultTask.getException().getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).setTextColor(Color.WHITE).show(); // mostrar error
            }
        };
        loginViewModel.getLogin().observe(this, obs);
    }

    public void go2Admin(){
        // Login ha tenido exito
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkUser(){
        if(loginViewModel.isLoggedIn()){
            // Iniciar actividad admin con los datos del usuario
            binding.loadStub.setVisibility(View.GONE);
            go2Admin();
        }else{
            // Iniciar actividad login
            binding.loadStub.setVisibility(View.GONE);


            // Onclick login
            binding.loginButton.setOnClickListener(v -> {
                binding.loadStub.setVisibility(View.VISIBLE);
                Log.w("PASSWD", binding.passwd.getText().toString());
                loginViewModel.login(binding.email.getText().toString(), binding.passwd.getText().toString());
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar ViewModel
        configViewModel();

        // Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Vista loading
        binding.loadStub.inflate();

        checkUser();


    }



}
