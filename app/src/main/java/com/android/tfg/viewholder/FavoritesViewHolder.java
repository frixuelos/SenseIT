package com.android.tfg.viewholder;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.databinding.ItemFavoritesBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.alert.AddAlertDialogFragment;
import com.android.tfg.view.more.MoreActivity;
import com.android.tfg.viewmodel.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {

    private ItemFavoritesBinding binding;
    private MainViewModel mainViewModel;

    public FavoritesViewHolder(@NonNull ItemFavoritesBinding itemFavoritesBinding, MainViewModel mainViewModel) {
        super(itemFavoritesBinding.getRoot());
        this.binding=itemFavoritesBinding;
        this.mainViewModel=mainViewModel;
    }

    public void bind(DeviceModel device){
        /***************
         * DEVICE INFO *
         ***************/
        binding.itemTitleFav.setText(device.getId());
        binding.itemTitleFav.setSelected(true);
        binding.itemLocationFav.setText(device.getName());
        binding.itemTempFav.setText(String.valueOf(mainViewModel.convertTemp(device.getLastMessage().getTemp())));
        binding.itemHumFav.setText(String.valueOf(device.getLastMessage().getHum()));
        binding.itemPresFav.setText(String.valueOf(mainViewModel.convertPres(device.getLastMessage().getPres())));
        binding.itemUvFav.setText(String.valueOf(mainViewModel.convertUv(device.getLastMessage().getUv())));
        Date lastUpdated = new Date(device.getLastMessage().getDate().getSeconds()*1000L);
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        binding.itemLastUpdateFav.setText(mFormat.format(lastUpdated));
        if(!device.isAlive()){
            binding.icDevice.setColorFilter(binding.getRoot().getContext().getResources().getColor(R.color.colorError));
        }else{
            binding.icDevice.setColorFilter(binding.getRoot().getContext().getResources().getColor(R.color.colorAccent));
        }

        /**********************
         * CARD VIEW ON CLICK *
         **********************/
        binding.cardViewFav.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), MoreActivity.class);
            i.putExtra("device", device.getId());
            v.getContext().startActivity(i);
        });

        /***********************
         * CARD VIEW ADD ALERT *
         ***********************/
        binding.itemAlert.setOnClickListener(v -> {
            new AddAlertDialogFragment(device.getId(), device.getName()).show(((AppCompatActivity)binding.getRoot().getContext()).getSupportFragmentManager(), "add_alert");
        });
    }

}
