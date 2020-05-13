package com.android.tfg.view.admin;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.tfg.databinding.FragmentAdminMenuBinding;
import com.android.tfg.model.DeviceModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialDialogs;

public class AdminMenu extends BottomSheetDialogFragment {

    private FragmentAdminMenuBinding binding;
    private DeviceModel device;

    public AdminMenu(DeviceModel device){
        this.device=device;
    }

    private void configView(){
        /*************
         * EDIT NAME *
         *************/
        binding.editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new EditDialogFragment(device).show(AdminMenu.this.getChildFragmentManager(), "edit_name");
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminMenuBinding.inflate(inflater, container, false);

        configView();

        return binding.getRoot();
    }
}
