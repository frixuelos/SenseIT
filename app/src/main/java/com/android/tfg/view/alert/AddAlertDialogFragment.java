package com.android.tfg.view.alert;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.databinding.DialogAddAlertBinding;
import com.android.tfg.model.AlertModel;
import com.android.tfg.viewmodel.AddAlertViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddAlertDialogFragment extends DialogFragment {

    private DialogAddAlertBinding binding;
    private AddAlertViewModel addAlertViewModel;
    private String deviceID, deviceName;
    private AlertModel alert;

    public AddAlertDialogFragment(String deviceID, String deviceName){
        this.deviceID=deviceID;
        this.deviceName=deviceName;
    }

    private void configView(){
        /***************
         * TEMPERATURE *
         ***************/
        binding.switchMinTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.minTemp.setEnabled(isChecked);
            }
        });

        binding.switchMaxTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.maxTemp.setEnabled(isChecked);
            }
        });

        /************
         * HUMIDITY *
         ************/
        binding.switchMinHum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.minHum.setEnabled(isChecked);
            }
        });

        binding.switchMaxHum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.maxHum.setEnabled(isChecked);
            }
        });

        /************
         * PRESSURE *
         ************/
        binding.switchMinPres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.minPres.setEnabled(isChecked);
            }
        });

        binding.switchMaxPres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.maxPres.setEnabled(isChecked);
            }
        });

        /******
         * UV *
         ******/
        binding.switchMinUV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.minUV.setEnabled(isChecked);
            }
        });

        binding.switchMaxUV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.maxUV.setEnabled(isChecked);
            }
        });

        /*************
         * OK BUTTON *
         *************/
        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        /*****************
         * CANCEL BUTTON *
         *****************/
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void validate(){
        double minTemp=0, maxTemp=0, minHum=0, maxHum=0, minPres=0, maxPres=0, minUV=0, maxUV=0;

        /********
         * TEMP *
         ********/
        if(binding.switchMinTemp.isChecked() && binding.minTemp.getText().length()!=0){
            minTemp=Double.parseDouble(binding.minTemp.getText().toString());
        }
        if(binding.switchMaxTemp.isChecked() && binding.maxTemp.getText().length()!=0){
            maxTemp=Double.parseDouble(binding.maxTemp.getText().toString());
            Log.e("MaxTemp -> ", String.valueOf(maxTemp));
        }

        if(binding.switchMinTemp.isChecked() && binding.switchMaxTemp.isChecked() && minTemp>maxTemp){
            binding.minTemp.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.maxTemp.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.minTemp.requestFocus();
            return;
        }

        /************
         * HUMIDITY *
         ************/
        if(binding.switchMinHum.isChecked() && binding.minHum.getText().length()!=0) {
            minHum = Double.parseDouble(binding.minHum.getText().toString());
            if (minHum < 0) {
                binding.minHum.setError(getString(R.string.errorAddAlertInvalidNumber));
                binding.minHum.requestFocus();
                return;
            }
        }

        if(binding.switchMaxHum.isChecked() && binding.maxHum.getText().length()!=0){
            maxHum=Double.parseDouble(binding.maxHum.getText().toString());
        }

        if(binding.switchMinHum.isChecked() && binding.switchMaxHum.isChecked() && minHum>maxHum){
            binding.minHum.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.maxHum.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.minHum.requestFocus();
            return;
        }


        /************
         * PRESSURE *
         ************/
        if(binding.switchMinPres.isChecked() && binding.minPres.getText().length()!=0){
            minPres=Double.parseDouble(binding.minPres.getText().toString());
            if(minPres<0){
                binding.minPres.setError(getString(R.string.errorAddAlertInvalidNumber));
                binding.minPres.requestFocus();
                return;
            }
        }

        if(binding.switchMaxPres.isChecked() && binding.maxPres.getText().length()!=0) {
            maxPres = Double.parseDouble(binding.maxPres.getText().toString());
        }

        if(binding.switchMinPres.isChecked() && binding.switchMaxPres.isChecked() && minPres>maxPres){
            binding.minPres.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.maxPres.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.minPres.requestFocus();
            return;
        }

        /******
         * UV *
         ******/
        if(binding.switchMinUV.isChecked() && binding.minUV.getText().length()!=0) {
            minUV = Double.parseDouble(binding.minUV.getText().toString());
            if (minUV < 0) {
                binding.minUV.setError(getString(R.string.errorAddAlertInvalidNumber));
                binding.minUV.requestFocus();
                return;
            }
        }

        if(binding.switchMaxUV.isChecked() && binding.maxUV.getText().length()!=0){
            maxUV=Double.parseDouble(binding.maxUV.getText().toString());
        }

        if(binding.switchMinUV.isChecked() && binding.switchMaxUV.isChecked() && minUV>maxUV){
            binding.minUV.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.maxUV.setError(getString(R.string.errorAddAlertInvalidRange));
            binding.minUV.requestFocus();
            return;
        }

        // all empty fields
        if( !binding.switchMinTemp.isChecked() &&
                !binding.switchMaxTemp.isChecked() &&
                !binding.switchMinHum.isChecked() &&
                !binding.switchMaxHum.isChecked() &&
                !binding.switchMinPres.isChecked() &&
                !binding.switchMaxPres.isChecked() &&
                !binding.switchMinUV.isChecked() &&
                !binding.switchMaxUV.isChecked()){

            // Aqui se eliminará si existen alertas
            if(addAlertViewModel.getDeviceAlert(deviceID)!=null) { // Existe la alerta
                new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                        .setTitle(getString(R.string.removeDialogTitle))
                        .setMessage(getString(R.string.removeDialog))
                        .setPositiveButton(getString(R.string.removeDialogPositive), (dialog, which) -> {
                            addAlertViewModel.removeDeviceAlert(addAlertViewModel.getDeviceAlert(deviceID));
                            dismiss();
                            AddAlertDialogFragment.this.dismiss();
                        }) // Se eliminará
                        .setNegativeButton(getString(R.string.removeDialogNegative), null)
                        .show();

            }else { // No existe ninguna alerta

                new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                        .setTitle(getString(R.string.createEmptyDialogTitle))
                        .setMessage(getString(R.string.createEmptyDialog))
                        .setNeutralButton(getString(R.string.addAlertOK), null)
                        .show();
            }
            return;
        }

        // Convert units
        minTemp=addAlertViewModel.convertTemp2Def(minTemp);
        maxTemp=addAlertViewModel.convertTemp2Def(maxTemp);
        minPres=addAlertViewModel.convertPres2Def(minPres);
        maxPres=addAlertViewModel.convertPres2Def(maxPres);
        minUV=addAlertViewModel.convertUv2Def(minUV);
        maxUV=addAlertViewModel.convertUv2Def(maxUV);

        // ADD ALERT
        addAlertViewModel.addUserAlert(new AlertModel(deviceID,
                deviceName,
                binding.switchMinTemp.isChecked(),
                binding.switchMinHum.isChecked(),
                binding.switchMinPres.isChecked(),
                binding.switchMinUV.isChecked(),
                binding.switchMaxTemp.isChecked(),
                binding.switchMaxHum.isChecked(),
                binding.switchMaxPres.isChecked(),
                binding.switchMaxUV.isChecked(),
                minTemp,
                maxTemp,
                minHum,
                maxHum,
                minPres,
                maxPres,
                minUV,
                maxUV));

        if(addAlertViewModel.getDeviceAlert(deviceID)!=null) {// Si ya existia la alerta mensaje de actualizado
            new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                    .setTitle(getString(R.string.addAlertSuccessUpdateTitle))
                    .setMessage(getString(R.string.addAlertSuccessUpdate))
                    .setNeutralButton(getString(R.string.ok), null);

        }else{// Si no existia mensaje de creado
            new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                    .setTitle(getString(R.string.addAlertSuccessTitle))
                    .setMessage(getString(R.string.addAlertSuccess))
                    .setNeutralButton(getString(R.string.ok), null)
                    .show();
        }

        dismiss();
    }

    private void loadDefaults(AlertModel alert){
        if(alert==null){// no existe alerta
            binding.switchMaxTemp.setChecked(false);
            binding.switchMinTemp.setChecked(false);
            binding.switchMinHum.setChecked(false);
            binding.switchMaxHum.setChecked(false);
            binding.switchMinPres.setChecked(false);
            binding.switchMaxPres.setChecked(false);
            binding.switchMinUV.setChecked(false);
            binding.switchMaxUV.setChecked(false);
            binding.minTemp.getText().clear();
            binding.maxTemp.getText().clear();
            binding.minHum.getText().clear();
            binding.maxHum.getText().clear();
            binding.minPres.getText().clear();
            binding.maxPres.getText().clear();
            binding.minUV.getText().clear();
            binding.maxUV.getText().clear();
            binding.ok.setText(getString(R.string.createButton));
            hideProgress();
            return;
        }

        // SWITCH
        binding.switchMinTemp.setChecked(alert.isMinTemp());
        binding.switchMaxTemp.setChecked(alert.isMaxTemp());
        binding.switchMinHum.setChecked(alert.isMinHum());
        binding.switchMaxHum.setChecked(alert.isMaxHum());
        binding.switchMinPres.setChecked(alert.isMinPres());
        binding.switchMaxPres.setChecked(alert.isMaxPres());
        binding.switchMinUV.setChecked(alert.isMinUv());
        binding.switchMaxUV.setChecked(alert.isMaxUv());

        // VALUES
        if(alert.isMinTemp()){
            binding.minTemp.setEnabled(true);
            binding.minTemp.setText(
                    String.valueOf(
                            addAlertViewModel.convertTempFromDef(
                                    alert.getMinTempValue()
                            )
                    )
            );
        }

        if(alert.isMaxTemp()){
            binding.maxTemp.setEnabled(true);
            binding.maxTemp.setText(
                    String.valueOf(
                            addAlertViewModel.convertTempFromDef(
                                    alert.getMaxTempValue()
                            )
                    )
            );
        }

        if(alert.isMinHum()){
            binding.minHum.setEnabled(true);
            binding.minHum.setText(
                    String.valueOf(
                            alert.getMinHumValue()
                    )
            );
        }

        if(alert.isMaxHum()){
            binding.maxHum.setEnabled(true);
            binding.maxHum.setText(
                    String.valueOf(
                            alert.getMaxHumValue()
                    )
            );
        }

        if(alert.isMinPres()){
            binding.minPres.setEnabled(true);
            binding.minPres.setText(
                    String.valueOf(
                            addAlertViewModel.convertPresFromDef(
                                    alert.getMinPresValue()
                            )
                    )
            );
        }

        if(alert.isMaxPres()){
            binding.maxPres.setEnabled(true);
            binding.maxPres.setText(
                    String.valueOf(
                            addAlertViewModel.convertPresFromDef(
                                    alert.getMaxPresValue()
                            )
                    )
            );
        }

        if(alert.isMinUv()){
            binding.minUV.setEnabled(true);
            binding.minUV.setText(
                    String.valueOf(
                            addAlertViewModel.convertUvFromDef(
                                    alert.getMinUuValue()
                            )
                    )
            );
        }

        if(alert.isMaxUv()){
            binding.maxUV.setEnabled(true);
            binding.maxUV.setText(
                    String.valueOf(
                            addAlertViewModel.convertUvFromDef(
                                    alert.getMaxUuValue()
                            )
                    )
            );
        }

        // BUTTON
        binding.ok.setText(getString(R.string.updateButton));

        hideProgress();

    }

    private void showProgress(){
        binding.loadStub.inflate();
    }

    private void hideProgress(){
        binding.loadStub.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = DialogAddAlertBinding.inflate(inflater, container, false);

        showProgress();

        setRetainInstance(true); // para sobrevivir a rotaciones de pantalla

        // Config viewmodel
        configViewModel();

        // Config view
        configView();

        return binding.getRoot();
    }

    private void configViewModel(){
        addAlertViewModel=new ViewModelProvider(this).get(AddAlertViewModel.class);
        alert=addAlertViewModel.getDeviceAlert(deviceID);
        loadDefaults(alert);
    }
}
