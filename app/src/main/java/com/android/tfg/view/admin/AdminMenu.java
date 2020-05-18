package com.android.tfg.view.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.tfg.R;
import com.android.tfg.databinding.FragmentAdminMenuBinding;
import com.android.tfg.model.DeviceModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminMenu extends BottomSheetDialogFragment {

    private FragmentAdminMenuBinding binding;
    private DeviceModel device;

    public AdminMenu(DeviceModel device){
        this.device=device;
    }

    public AdminMenu(){}

    private void configView(){
        /****************
         * EXPAND ARROW *
         ****************/
        binding.expandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AdminMenu.this.getDialog()!=null) {
                    FrameLayout bottomSheet = (FrameLayout) AdminMenu.this.getDialog().findViewById(R.id.design_bottom_sheet);
                    if (BottomSheetBehavior.from(bottomSheet).getState() != BottomSheetBehavior.STATE_EXPANDED) { // expand bottomsheet
                        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            }
        });

        /*************
         * EDIT NAME *
         *************/
        binding.editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditNameDialogFragment(device).show(AdminMenu.this.getChildFragmentManager(), "edit_name");
            }
        });

        /*******************
         * UPDATE SETTINGS *
         *******************/
        binding.editConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateSettingsDialogFragment(device).show(AdminMenu.this.getChildFragmentManager(), "update_settings");
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminMenuBinding.inflate(inflater, container, false);

        setRetainInstance(true); // para sobrevivir a rotaciones de pantalla

        configView();

        return binding.getRoot();
    }


}
