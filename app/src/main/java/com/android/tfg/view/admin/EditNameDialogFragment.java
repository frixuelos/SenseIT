package com.android.tfg.view.admin;

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
import com.android.tfg.databinding.DialogEditNameBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewmodel.EditNameViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class EditNameDialogFragment extends DialogFragment {

    private DialogEditNameBinding binding;
    private EditNameViewModel editNameViewModel;
    private DeviceModel device;

    public EditNameDialogFragment(DeviceModel device){
        this.device=device;
    }

    public EditNameDialogFragment(){}

    private void configViewModel(){
        editNameViewModel =new ViewModelProvider(this).get(EditNameViewModel.class);
        final Observer<Task<Void>> editNameObserver = new Observer<Task<Void>>() {
            @Override
            public void onChanged(Task<Void> voidTask) {
                // hide progress
                binding.loadStub.setVisibility(View.GONE);

                if(voidTask.isSuccessful()){
                    // Ha tenido exito
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.editNameChangedTitle))
                            .setMessage(getString(R.string.editNameChanged))
                            .setNeutralButton(getString(R.string.editNameOK), (dialog, which) -> EditNameDialogFragment.this.dismiss())
                            .show();
                }else{
                    // No ha tenido exito
                    new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                            .setTitle(getString(R.string.errorEditNameFailTitle))
                            .setMessage(getString(R.string.errorEditNameFail))
                            .setNeutralButton(getString(R.string.editNameOK), (dialog, which) -> EditNameDialogFragment.this.dismiss())
                            .show();
                }
            }
        };
        editNameViewModel.getEditNameResult().observe(getViewLifecycleOwner(), editNameObserver);
    }

    private void configView(){
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
            // show progress
            binding.loadStub.inflate();

            // Cambiar nombre
            device.setName(binding.editName.getText().toString());
            editNameViewModel.editName(device);
        });
        // CANCEL BUTTON
        binding.cancel.setOnClickListener(v ->{
            dismiss();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditNameBinding.inflate(inflater, container, false);

        setRetainInstance(true); // para sobrevivir a rotaciones de pantalla

        configView();

        configViewModel();

        return binding.getRoot();
    }
}
