package com.android.tfg.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.tfg.R;

public class MoreActivity extends AppCompatActivity {

    TextView texto;
    Toolbar toolbar;

    public void configView(){
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        texto=findViewById(R.id.textoMore);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        configView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
