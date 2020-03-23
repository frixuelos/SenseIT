package com.android.tfg.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.tfg.R;
import com.android.tfg.viewmodel.LoginUserViewModel;


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
        initLoginUserViewModel();
        initLoginButton();

    }

    private void initLoginButton(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("LOGIN", "CLICK !!");
                userViewModel.login(etEmail.getText().toString(), etPasswd.getText().toString());
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
