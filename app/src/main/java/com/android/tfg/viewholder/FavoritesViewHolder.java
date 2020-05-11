package com.android.tfg.viewholder;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemFavoritesBinding;
import com.android.tfg.model.DeviceModel;
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
        binding.itemLocationFav.setText(device.getName());
        binding.itemTempFav.setText(String.valueOf(mainViewModel.convertTemp(device.getLastMessage().getTemp())));
        binding.itemHumFav.setText(String.valueOf(device.getLastMessage().getHum()));
        binding.itemPresFav.setText(String.valueOf(mainViewModel.convertPres(device.getLastMessage().getPres())));
        binding.itemUvFav.setText(String.valueOf(mainViewModel.convertUv(device.getLastMessage().getUv())));
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
            i.putExtra("device", device.getId());
            v.getContext().startActivity(i);
        });
    }

}
