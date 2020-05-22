package com.android.tfg.view.alert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.tfg.R;
import com.android.tfg.adapter.AlertAdapter;
import com.android.tfg.databinding.ActivityAlertBinding;
import com.android.tfg.model.AlertModel;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.room.AlertDatabase;
import com.android.tfg.swipe.SwipeRemoveCallback;
import com.android.tfg.viewholder.AlertViewHolder;
import com.android.tfg.viewmodel.AlertViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AlertActivity extends AppCompatActivity {

    private ActivityAlertBinding binding;
    private AlertAdapter alertAdapter;
    private AlertViewModel alertViewModel;
    private SwipeRemoveCallback swipeRemoveCallback;

    private void configRecyclerView(ArrayList<AlertModel> userAlerts){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // si no tiene mostrar texto vacio
        if(userAlerts==null || userAlerts.isEmpty()){binding.alertNoData.setVisibility(View.VISIBLE); return;}
        else{binding.alertNoData.setVisibility(View.GONE);}

        // configurar recycler con datos
        alertAdapter = new AlertAdapter(userAlerts);
        binding.alertRecyclerView.setHasFixedSize(true);
        binding.alertRecyclerView.setAdapter(alertAdapter);
        binding.alertRecyclerView.setLayoutManager(layoutManager);
        // Swipe to remove
        configSwipeToRemove();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeRemoveCallback);
        itemTouchHelper.attachToRecyclerView(binding.alertRecyclerView);
    }

    private void configViewModel(){
        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        alertViewModel.registerUserAlerts();
        final Observer<ArrayList<AlertModel>> obs = new Observer<ArrayList<AlertModel>>() {
            @Override
            public void onChanged(ArrayList<AlertModel> alertModels) {
                configRecyclerView(alertModels);
            }
        };
        alertViewModel.getUserAlerts().observe(this, obs);
    }

    private void configSwipeToRemove(){
        swipeRemoveCallback = new SwipeRemoveCallback(this){

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i){
                int pos = viewHolder.getAdapterPosition();
                AlertModel removed = alertAdapter.removeItem(pos);
                alertViewModel.removeUserAlert(removed);

                Snackbar snackbar = Snackbar.make(binding.coordinator, getString(R.string.remove_from_alerts), Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.setAction(getString(R.string.undo), v -> {
                    alertAdapter.insertItem(removed, pos);
                    binding.alertRecyclerView.scrollToPosition(pos);
                    alertViewModel.addUserAlert(removed);
                    binding.alertNoData.setVisibility(View.GONE); // Ocultar el texto de favoritos vacio
                });
                snackbar.show();

                if(alertAdapter.getItemCount()==0){ // Mostrar el texto si no hay favoritos
                    binding.alertNoData.setVisibility(View.VISIBLE);
                }
            }

        };
    }

    private void configView(){
        // Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.configAlertTitle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Binding
        binding = ActivityAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // configview
        configView();

        // configviewmodel
        configViewModel();

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() { // Back arrow button
        onBackPressed();
        return true;
    }
}
