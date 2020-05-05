package com.android.tfg.view.More;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.android.tfg.R;
import com.android.tfg.adapter.MoreAdapter;
import com.android.tfg.adapter.MorePagerAdapter;
import com.android.tfg.adapter.SearchAdapter;
import com.android.tfg.databinding.ActivityMoreBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.android.tfg.viewholder.SearchViewHolder;
import com.android.tfg.viewmodel.MoreViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    private long since=0, until=0;
    private String device;
    private MoreViewModel moreViewModel;
    private ActivityMoreBinding binding;
    private MoreAdapter moreAdapter;
    private SELECTION CHART_SELECTED;
    private enum SELECTION{
            T,
            H,
            P,
            U
    }
    private HashMap<String, String> units;

    private void configViewModel(){
        /**************
         * MODEL VIEW *
         **************/
        moreViewModel = new ViewModelProvider(this).get(getString(R.string.moreViewModel), MoreViewModel.class);
        moreViewModel.registerMessagesFromDevice(device, since, until); // primera llamada para todos los mensajes
        // añadir datos al recyclerview
        final Observer<LinkedList<MessageModel>> obs = messageModels -> {
            if(messageModels.isEmpty()){
                binding.moreRecyclerView.setBackgroundColor(Color.GRAY);
                if(moreAdapter!=null){moreAdapter.clear();}
                return; }
            configRecyclerView(messageModels);
        };
        moreViewModel.getMessages().observe(this, obs); // mensajes
    }

    // Necesario para actualizar la vista conforme a los datos de la BBDD
    private void configRecyclerView(LinkedList<MessageModel> messages){
        if(moreAdapter!=null){
            moreAdapter.updateItems(moreViewModel.getMessages().getValue());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        moreAdapter = new MoreAdapter(messages, moreViewModel);
        binding.moreRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(binding.moreRecyclerView.getContext(), layoutManager.getOrientation());
        divider.getDrawable().setTint(Color.WHITE);
        divider.getDrawable().setTintMode(PorterDuff.Mode.OVERLAY);
        binding.moreRecyclerView.addItemDecoration(divider);
        binding.moreRecyclerView.setAdapter(moreAdapter);
        binding.moreRecyclerView.setLayoutManager(layoutManager);
    }

    public void configView(){
        // Device
        device=(String) Objects.requireNonNull(getIntent().getExtras()).get("device");
        setTitle(device);

        /*********
         * UNITS *
         *********/
        setupUnits();

        /*********
         * PAGER *
         *********/
        binding.morePager.setOffscreenPageLimit(4);
        binding.morePager.setAdapter(new MorePagerAdapter(getSupportFragmentManager(),getApplicationContext(), device));

        /*****************
         * TOGGLE BUTTON *
         *****************/
        if(CHART_SELECTED==null){CHART_SELECTED=SELECTION.T;}
        binding.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(!isChecked){return;}
            switch(checkedId){
                case R.id.toggleTemp:   binding.morePager.setCurrentItem(0);
                                        CHART_SELECTED=SELECTION.T;
                                        break;
                case R.id.toggleHum:    binding.morePager.setCurrentItem(1);
                                        CHART_SELECTED=SELECTION.H;
                                        break;
                case R.id.togglePres:   binding.morePager.setCurrentItem(2);
                                        CHART_SELECTED=SELECTION.P;
                                        break;
                case R.id.toggleUV:     binding.morePager.setCurrentItem(3);
                                        CHART_SELECTED=SELECTION.U;
                                        break;
            }
        });

        /**************************
         * FLOATING ACTION BUTTON *
         **************************/
        long timeInMillis = Calendar.getInstance().getTimeInMillis(); // Filtros por defecto
        since=timeInMillis - (24 * 60 * 60 * 1000);
        until=timeInMillis;
        binding.fbDate.setOnClickListener(v -> {
            // Current time
            long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

            // Builder
            MaterialDatePicker.Builder<Pair<Long,Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setSelection(new Pair<Long, Long>(since, until));
            TypedValue value = new TypedValue();
            getTheme().resolveAttribute(R.attr.materialCalendarTheme, value,true);
            builder.setTheme(value.resourceId);
            builder.setTitleText(R.string.titleDateRange);

            // Restricciones fechas
            CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
            calendarConstraintsBuilder.setEnd(currentTimeInMillis);
            CalendarConstraints calendarConstraints = calendarConstraintsBuilder.build();
            builder.setCalendarConstraints(calendarConstraints);

            // Picker
            MaterialDatePicker<Pair<Long,Long>> picker = builder.build();
            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                @Override
                public void onPositiveButtonClick(Pair<Long, Long> selection) {
                    if(selection.first>currentTimeInMillis || selection.second>currentTimeInMillis){
                        new MaterialAlertDialogBuilder(MoreActivity.this).setTitle("ERROR").setMessage("Can't select this range !").setNeutralButton("OK", null).show();
                        return;
                    }
                    since=selection.first;
                    until=selection.second; // Se le suma un dia puesto que de partida sera a las 00:00:00 (es decir un dia menos)
                    moreViewModel.registerMessagesFromDevice(device, since, until);
                }
            });
            picker.show(getSupportFragmentManager(), picker.toString());
        });

    }

    public void configToolbar(){
        /***********
         * TOOLBAR *
         ***********/
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configToolbar();
        binding = ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configView();
        configViewModel();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Cuando se restaura el estado de la actividad restablecemos el estado de los toggle
        String selected = savedInstanceState.getString("chart_selected");
        if(selected==null){return;}
        CHART_SELECTED = SELECTION.valueOf(selected);
        switch (CHART_SELECTED){
            case T: binding.toggleTemp.setChecked(true);
                CHART_SELECTED=SELECTION.T;
                break;
            case H: binding.toggleHum.setChecked(true);
                CHART_SELECTED=SELECTION.H;
                break;
            case P: binding.togglePres.setChecked(true);
                CHART_SELECTED=SELECTION.P;
                break;
            case U: binding.toggleUV.setChecked(true);
                CHART_SELECTED=SELECTION.U;
                break;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    // Cuando se guarda el estado de la actividad almacenamos el estado de los toggle
        outState.putString("chart_selected", CHART_SELECTED.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        moreViewModel.unregisterMessagesFromDevice(device); // Eliminar listener de mensajes asociado al dispositivo
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflar boton favoritos
        getMenuInflater().inflate(R.menu.more_favorites_menu, menu);

        // cargar preferencias favoritos
        if(moreViewModel.isFavorite(device)){ // Añadido a favoritos
            menu.findItem(R.id.favoriteEvent).setIcon(R.drawable.ic_favorite_checked_24dp);
            menu.findItem(R.id.favoriteEvent).setChecked(true);

        }else{ // eliminado de favoritos
            menu.findItem(R.id.favoriteEvent).setIcon(R.drawable.ic_favorite_24dp);
            menu.findItem(R.id.favoriteEvent).setChecked(false);
        }

        // capturar el evento fav
        menu.findItem(R.id.favoriteEvent).setOnMenuItemClickListener(item -> {
            if(!item.isChecked()){ // Cambiar a añadido a favoritos
                addFav(item);
            }else{ // Cambiar a eliminado de favoritos
                removeFav(item);
            }
            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addFav(MenuItem item){
        // configurar icono
        item.setIcon(R.drawable.ic_favorite_checked_24dp);
        item.setChecked(true);

        // añadir a favoritos
        moreViewModel.add2Favorites(device);
        Toast.makeText(getApplicationContext(), getString(R.string.add_to_favorites), Toast.LENGTH_SHORT).show();
    }

    private void removeFav(MenuItem item){
        // configurar icono
        item.setIcon(R.drawable.ic_favorite_24dp);
        item.setChecked(false);

        // eliminar de favoritos
        moreViewModel.removeFromFavorites(device);
        Toast.makeText(getApplicationContext(), getString(R.string.remove_from_favorites), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupUnits(){
        this.units=new HashMap<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(binding.getRoot().getContext());
        this.units.put(getString(R.string.keyUnitTemp), preferences.getString(getString(R.string.keyUnitTemp), getString(R.string.defUnitTemp)));
        this.units.put(getString(R.string.keyUnitPres), preferences.getString(getString(R.string.keyUnitPres), getString(R.string.defUnitPres)));
        this.units.put(getString(R.string.keyUnitUV), preferences.getString(getString(R.string.keyUnitUV), getString(R.string.defUnitUV)));
    }

    protected HashMap<String,String> getUnits(){return this.units;}
}
