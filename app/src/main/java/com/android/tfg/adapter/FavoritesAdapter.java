package com.android.tfg.adapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.MoreActivity;
import com.android.tfg.viewholder.FavoritesViewHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private LinkedList<DeviceModel> devices;

    public FavoritesAdapter(LinkedList<DeviceModel> devices){
        this.devices=devices;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Vista cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites, parent, false);

        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, final int position) {
        DeviceModel device = devices.get(position);

        /**********************
         * CARD VIEW LISTENER *
         **********************/
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MoreActivity.class);
                i.putExtra("device", device.getDeviceID());
                v.getContext().startActivity(i);
            }
        });

        /****************
         * DEFAULT VIEW *
         ****************/
        holder.item_location_fav.setText(device.getName());
        holder.item_title_fav.setText(device.getDeviceID());
        Date lastUpdated = new Date(device.getLastMessage().getDate().getSeconds()*1000L);
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        holder.item_last_updated.setText(mFormat.format(lastUpdated));
        holder.item_temp_fav.setText(String.valueOf(device.getLastMessage().getTemp()));
        holder.item_hum_fav.setText(String.valueOf(device.getLastMessage().getHum()));
        holder.item_press_fav.setText(String.valueOf(device.getLastMessage().getPres()));
        holder.item_uv_fav.setText(String.valueOf(device.getLastMessage().getUv()));
        holder.item_map_fav.onCreate(null);
        holder.item_map_fav.onResume();
        holder.item_map_fav.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(device.getSite());
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(device.getSite()));
                googleMap.setMinZoomPreference(15);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public DeviceModel removeItem(int pos){
        DeviceModel removed = devices.get(pos);
        devices.remove(pos);
        notifyItemRemoved(pos);
        return removed;
    }

    public void insertItem(DeviceModel device, int pos){
        devices.add(pos, device);
        notifyItemInserted(pos);
    }

    public void updateItems(LinkedList<DeviceModel> devices){
        this.devices.clear();
        this.devices.addAll(devices);
        notifyDataSetChanged();
    }

}
