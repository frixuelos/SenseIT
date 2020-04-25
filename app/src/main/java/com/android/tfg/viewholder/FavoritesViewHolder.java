package com.android.tfg.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.databinding.ItemFavoritesBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.MoreActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {

    private ItemFavoritesBinding binding;

    public FavoritesViewHolder(@NonNull ItemFavoritesBinding itemFavoritesBinding) {
        super(itemFavoritesBinding.getRoot());
        this.binding=itemFavoritesBinding;
    }

    public void bind(DeviceModel device){
        /***************
         * DEVICE INFO *
         ***************/
        binding.itemTitleFav.setText(device.getDeviceID());
        binding.itemLocationFav.setText(device.getName());
        binding.itemTempFav.setText(String.valueOf(device.getLastMessage().getTemp()));
        binding.itemHumFav.setText(String.valueOf(device.getLastMessage().getHum()));
        binding.itemPresFav.setText(String.valueOf(device.getLastMessage().getPres()));
        binding.itemUvFav.setText(String.valueOf(device.getLastMessage().getUv()));
        Date lastUpdated = new Date(device.getLastMessage().getDate().getSeconds()*1000L);
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        binding.itemLastUpdateFav.setText(mFormat.format(lastUpdated));

        /**************
         * DEVICE MAP *
         **************/
        binding.itemMapFav.onCreate(null);
        binding.itemMapFav.onResume();
        binding.itemMapFav.onLowMemory();
        binding.itemMapFav.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(device.getSite());
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(device.getSite()));
            //googleMap.setMinZoomPreference(15);
        });

        /**********************
         * CARD VIEW ON CLICK *
         **********************/
        binding.cardViewFav.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), MoreActivity.class);
            i.putExtra("device", device.getDeviceID());
            v.getContext().startActivity(i);
        });
    }

}
