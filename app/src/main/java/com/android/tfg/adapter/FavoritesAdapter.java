package com.android.tfg.adapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.MoreActivity;
import com.android.tfg.viewholder.FavoritesViewHolder;
import com.android.tfg.viewholder.SearchViewHolder;
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
        final int pos = position;


        /****************
         * DEFAULT VIEW *
         ****************/
        Geocoder geocoder = new Geocoder(holder.item_location_fav.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(devices.get(position).getLastMessage().getComputedLocation().getLat(), devices.get(position).getLastMessage().getComputedLocation().getLng(), 1);
            holder.item_location_fav.setText(String.format(holder.item_location_fav.getContext().getString(R.string.locationFormat),addresses.get(0).getLocality(), addresses.get(0).getSubAdminArea()));
        } catch (IOException e) {
            // No se pudo encontrar una dirección se establece nulo
            holder.item_location_fav.setVisibility(View.GONE);
        }
        holder.item_title_fav.setText(devices.get(position).getName());
        Date lastUpdated = new Date(devices.get(position).getLastMessage().getDate()*1000L);
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        holder.item_last_updated.setText(mFormat.format(lastUpdated));
        holder.item_temp_fav.setText(String.valueOf(devices.get(position).getLastMessage().getTemp()));
        holder.item_hum_fav.setText(String.valueOf(devices.get(position).getLastMessage().getHum()));
        holder.item_press_fav.setText(String.valueOf(devices.get(position).getLastMessage().getPres()));
        holder.item_uv_fav.setText(String.valueOf(devices.get(position).getLastMessage().getUv()));
        holder.item_map_fav.onCreate(null);
        holder.item_map_fav.onResume();
        holder.item_map_fav.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(devices.get(position).getSite());
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(devices.get(position).getSite()));
                googleMap.setMinZoomPreference(15);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

}
