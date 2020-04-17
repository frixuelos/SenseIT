package com.android.tfg.adapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.LinearLayoutCompat;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.MoreActivity;
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
        final int pos = position;
        /***************
         * EXPAND VIEW *
         ***************/
        holder.expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.expand_view.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(holder.card_view, new AutoTransition());
                    holder.expand_view.setVisibility(View.VISIBLE);
                    holder.expand_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_24dp);
                }else{
                    TransitionManager.beginDelayedTransition(holder.card_view, new AutoTransition());
                    holder.expand_view.setVisibility(View.GONE);
                    holder.expand_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_24dp);
                }
            }
        });
        holder.more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MoreActivity.class);
                i.putExtra("device", filteredDevices.get(pos).getDeviceID());
                v.getContext().startActivity(i);
            }
        });

        /****************
         * DEFAULT VIEW *
         ****************/
        Geocoder geocoder = new Geocoder(holder.item_location.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(filteredDevices.get(position).getLastMessage().getComputedLocation().getLat(), filteredDevices.get(position).getLastMessage().getComputedLocation().getLng(), 1);
            if(addresses.get(0).getLocality()==null || addresses.get(0).getSubAdminArea()==null){throw new IOException();}
            holder.item_location.setText(String.format(holder.item_location.getContext().getString(R.string.locationFormat),addresses.get(0).getLocality(), addresses.get(0).getSubAdminArea()));
        } catch (IOException e) {
            // No se pudo encontrar una dirección se establece nulo
            holder.item_location.setVisibility(View.GONE);
        }
        holder.item_title.setText(filteredDevices.get(position).getName());
        Date lastUpdated = new Date(filteredDevices.get(position).getLastMessage().getDate()*1000L);
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        holder.item_last_updated.setText(mFormat.format(lastUpdated));
        holder.item_temp.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getTemp()));
        holder.item_hum.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getHum()));
        holder.item_press.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getPres()));
        holder.item_uv.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getUv()));
        holder.item_map.onCreate(null);
        holder.item_map.onResume();
        holder.item_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
