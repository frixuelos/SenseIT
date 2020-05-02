package com.android.tfg.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemMoreBinding;
import com.android.tfg.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoreViewHolder extends RecyclerView.ViewHolder {

    private ItemMoreBinding binding;

    public MoreViewHolder(@NonNull ItemMoreBinding itemMoreBinding) {
        super(itemMoreBinding.getRoot());
        this.binding=itemMoreBinding;
    }

    public void bind(MessageModel message) {
        /***************
         * DEVICE INFO *
         ***************/
        binding.itemTemp.setText(String.valueOf(message.getTemp()));
        binding.itemHum.setText(String.valueOf(message.getHum()));
        binding.itemPres.setText(String.valueOf(message.getPres()));
        binding.itemUv.setText(String.valueOf(message.getUv()));
        Date lastUpdated = new Date(message.getDate().getSeconds() * 1000L);
        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy", Locale.getDefault());
        binding.itemTime.setText(mFormat.format(lastUpdated));
    }

}
