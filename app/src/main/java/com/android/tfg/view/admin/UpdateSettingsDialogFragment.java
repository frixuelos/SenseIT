package com.android.tfg.view.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.databinding.DialogUpdateSettingsBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.UpdateSettingsViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;
import java.util.Observable;

public class UpdateSettingsDialogFragment extends DialogFragment {

    private UpdateSettingsViewModel updateSettingsViewModel;
    private DialogUpdateSettingsBinding binding;
    private final int SLEEP_TIME_MIN = 11;
    private DeviceModel device;

    public UpdateSettingsDialogFragment(DeviceModel deviceModel){
        this.device=deviceModel;
    }

    public void configViewModel(){
        updateSettingsViewModel = new ViewModelProvider(this).get(UpdateSettingsViewModel.class);
        final Observer<Task<Void>> updateSettingsObvserver = new Observer<Task<Void>>() {
            @Override
            public void onChanged(Task<Void> voidTask) {
                if(voidTask.isSuccessful()){
                    // Ha tenido exito
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.editDeviceConfigChangedTitle))
                            .setMessage(getString(R.string.editDeviceConfigChanged))
                            .setNeutralButton(getString(R.string.editDeviceConfigOK), (dialog, which) -> UpdateSettingsDialogFragment.this.dismiss())
                            .show();
                }else{
                    // No ha tenido exito
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.errorEditDeviceConfigTitle))
                            .setMessage(getString(R.string.errorEditDeviceConfig))
                            .setNeutralButton(getString(R.string.editDeviceConfigOK), (dialog, which) -> UpdateSettingsDialogFragment.this.dismiss())
                            .show();
                }
            }
        };
        updateSettingsViewModel.getUpdateSettingsResult().observe(getViewLifecycleOwner(), updateSettingsObvserver);
    }

    // Para convertir a texto el progreso (tiempo sleep)
    private String timeToTXT(int time){
        return String.format(Locale.getDefault(), "%d min", time);
    }

    private void configView(){
        /**************
         * SLEEP TIME *
         **************/
        binding.sleepTimeText.setText(timeToTXT(device.getConfig().getSleepTime()));
        binding.sleepTime.setProgress(device.getConfig().getSleepTime());
        binding.sleepTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.sleepTimeText.setText(timeToTXT(progress));
                if(progress<SLEEP_TIME_MIN){
                    seekBar.setProgress(SLEEP_TIME_MIN);
                    binding.sleepTimeText.setText(getString(R.string.sleepTimeDefaultText));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*****************
         * UPDATE BUTTON *
         *****************/
        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update config
                updateSettingsViewModel.updateSettings(device, binding.sleepTime.getProgress());
            }
        });

        /*****************
         * CANCEL BUTTON *
         *****************/
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                UpdateSettingsDialogFragment.this.dismiss();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Binding
        binding = DialogUpdateSettingsBinding.inflate(inflater, container, false);

        setRetainInstance(true); // para sobrevivir a rotaciones de pantalla

        configView();

        configViewModel();

        return binding.getRoot();
    }
}
