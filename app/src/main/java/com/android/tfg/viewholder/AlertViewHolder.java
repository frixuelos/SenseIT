package com.android.tfg.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemAlertBinding;
import com.android.tfg.model.AlertModel;
import com.android.tfg.model.DeviceModel;

public class AlertViewHolder extends RecyclerView.ViewHolder {

    public ItemAlertBinding binding;

    public AlertViewHolder(ItemAlertBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }

    public void bind(AlertModel alert) {
        binding.itemTitle.setText(alert.getDeviceName());
        binding.itemId.setText(alert.getDeviceID());
    }
}
