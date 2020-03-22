package com.android.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;

    private void configView(){
        constraintLayout = findViewById(R.id.login_layout);
        final Drawable drw = constraintLayout.getBackground();
        final EditText eText = (EditText)findViewById(R.id.editText);
        final EditText pText = (EditText)findViewById(R.id.editText2);
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setAlpha(0.8F);
        btnLogin.setVisibility(View.INVISIBLE);

        eText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    eText.setBackgroundColor(Color.BLACK);
                    eText.setAlpha(0.5F);
                    pText.setBackgroundColor(Color.BLACK);
                    pText.setAlpha(0.5F);
                    drw.setAlpha(50);
                    btnLogin.setVisibility(View.VISIBLE);
                }else{
                    drw.setAlpha(100);
                    btnLogin.setVisibility(View.INVISIBLE);
                }

            }
        });

        pText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    pText.setBackgroundColor(Color.BLACK);
                    pText.setAlpha(0.5F);
                    eText.setBackgroundColor(Color.BLACK);
                    eText.setAlpha(0.5F);
                    drw.setAlpha(50);
                    btnLogin.setVisibility(View.VISIBLE);
                }else{
                    drw.setAlpha(100);
                    btnLogin.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configView(); // Configurar la vista
    }
}
