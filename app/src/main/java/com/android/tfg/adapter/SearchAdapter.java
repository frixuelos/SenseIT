package com.android.tfg.adapter;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewholder.SearchViewHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private LinkedList<DeviceModel> devices;
    private LinkedList<DeviceModel> filteredDevices;

    public SearchAdapter(LinkedList<DeviceModel> devices){
        this.devices=devices;
        this.filteredDevices=new LinkedList<DeviceModel>();
        this.filteredDevices.addAll(this.devices);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Vista cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        // Establecer la informacion de cada item
        holder.item_title.setText(filteredDevices.get(position).getName());
        holder.item_description.setText(filteredDevices.get(position).getLastMessage().toString());
        holder.item_map.onCreate(null);
        holder.item_map.onResume();
        holder.item_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(filteredDevices.get(position).getSite());
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(filteredDevices.get(position).getSite()));
                googleMap.setMinZoomPreference(15);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDevices.size();
    }

    public void filter(String text){
        this.filteredDevices.clear();
        this.filteredDevices.addAll(devices);
        if(!text.isEmpty()){ // Texto a filtrar
            for(DeviceModel deviceModel: this.filteredDevices){
                if(!deviceModel.getName().contains(text)){
                    this.filteredDevices.remove(deviceModel);
                }
            }
        }
        notifyDataSetChanged();
    }

}
