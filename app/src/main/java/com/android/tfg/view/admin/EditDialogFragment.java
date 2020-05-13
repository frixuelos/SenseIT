package com.android.tfg.view.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.tfg.R;
import com.android.tfg.databinding.DialogEditBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.EditViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class EditDialogFragment extends DialogFragment {

    private DialogEditBinding binding;
    private EditViewModel editViewModel;
    private DeviceModel device;

    public EditDialogFragment(DeviceModel device){
        this.device=device;
    }

    private void configViewModel(){
        editViewModel=new ViewModelProvider(this).get(EditViewModel.class);
        final Observer<Task<Void>> editNameObserver = new Observer<Task<Void>>() {
            @Override
            public void onChanged(Task<Void> voidTask) {
                if(voidTask.isSuccessful()){
                    // Ha tenido exito
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.editNameChangedTitle))
                            .setMessage(getString(R.string.editNameChanged))
                            .setNeutralButton(getString(R.string.editNameOK), (dialog, which) -> EditDialogFragment.this.dismiss())
                            .show();
                }else{
                    // No ha tenido exito
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.errorEditNameFailTitle))
                            .setMessage(getString(R.string.errorEditNameFail))
                            .setNeutralButton(getString(R.string.editNameOK), (dialog, which) -> EditDialogFragment.this.dismiss())
                            .show();
                }
            }
        };
        editViewModel.getEditNameResult().observe(getViewLifecycleOwner(), editNameObserver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditBinding.inflate(inflater, container, false);

        // Configurar viewmodel
        configViewModel();

        /*************
         * EDIT NAME *
         *************/
        binding.editName.setText(device.getName()); // current name
        // OK BUTTON
        binding.ok.setOnClickListener(v -> {
            if(binding.editName.getText().toString().isEmpty()){ // Si está vacio
                binding.editName.setError(getString(R.string.editNameEmpty));
                binding.editName.requestFocus();
                return;
            }
            if(binding.editName.getText().toString().equals(device.getName())){ // Si ya tiene ese nombre
                binding.editName.setError(getString(R.string.errorEditNameSame));
                binding.editName.requestFocus();
                return;
            }
            // Cambiar nombre
            device.setName(binding.editName.getText().toString());
            editViewModel.editName(device);
        });
        // CANCEL BUTTON
        binding.cancel.setOnClickListener(v ->{
            dismiss();
        });

        return binding.getRoot();
    }
}
