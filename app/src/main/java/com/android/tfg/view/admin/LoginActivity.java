package com.android.tfg.view.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    public void configViewModel(){
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // LOGIN
        final Observer<Task<AuthResult>> loginObserver = authResultTask -> {
            if(authResultTask.isSuccessful()){
                // Login ha tenido exito
                go2Admin();
            }else{
                // Login ha fallado
                binding.loadStub.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(), authResultTask.getException().getMessage(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.colorError))
                        .setTextColor(Color.WHITE)
                        .show(); // mostrar error
            }
        };
        loginViewModel.getLogin().observe(this, loginObserver);

        // RESET PASSWORD
        final Observer<Task<Void>> resetPasswordObserver = resultTask -> {
            if(resultTask.isSuccessful()){
                // Envio del reset correcto
                binding.loadStub.setVisibility(View.GONE);
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.resetPasswordSentTitle))
                        .setMessage(getString(R.string.resetPasswordSent))
                        .setNeutralButton(getString(R.string.resetPasswordSentOK), null)
                        .show();
            }else{
                // Envio fallido
                binding.loadStub.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(), resultTask.getException().getMessage(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.colorError))
                        .setTextColor(Color.WHITE)
                        .show(); // mostrar error
            }
        };
        loginViewModel.getResetPassword().observe(this, resetPasswordObserver);
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

                if(binding.email.getText().toString().isEmpty()
                        || !Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()
                        || binding.passwd.getText().toString().isEmpty()){
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext()).setTitle(getString(R.string.errorLoginValuesInvalidTitle))
                                                                .setMessage(getString(R.string.errorLoginValuesInvalid))
                                                                .setNeutralButton(getString(R.string.errorLoginValuesInvalidOK),null)
                                                                .show();
                    return;
                }
                binding.loadStub.setVisibility(View.VISIBLE);
                loginViewModel.login(binding.email.getText().toString(), binding.passwd.getText().toString());
            });


            // Onclick reset password
            binding.txtLostPasswd.setOnClickListener(v -> {
                if(binding.email.getText().toString().isEmpty()
                        || !Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()){
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.errorResetPasswordTitle))
                            .setMessage(getString(R.string.errorResetPassword))
                            .setNeutralButton(getString(R.string.errorResetPasswordOK),null)
                            .show();
                    return;
                }
                binding.loadStub.setVisibility(View.VISIBLE);
                loginViewModel.resetPassword(binding.email.getText().toString());
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

        // Title
        setTitle(getString(R.string.adminLoginTitle));

        // Vista loading
        binding.loadStub.inflate();

        checkUser();


    }



}
