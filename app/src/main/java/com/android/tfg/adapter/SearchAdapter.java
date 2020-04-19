package com.android.tfg.adapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.view.MoreActivity;
import com.android.tfg.viewholder.SearchViewHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.rpc.Help;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {

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
        DeviceModel device = filteredDevices.get(position);
        /***************
         * EXPAND VIEW *
         ***************
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
        });*/


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
        Geocoder geocoder = new Geocoder(holder.item_location.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(device.getLastMessage().getComputedLocation().getLat(), device.getLastMessage().getComputedLocation().getLng(), 1);
            if(addresses.get(0).getLocality()==null || addresses.get(0).getSubAdminArea()==null){throw new IOException();}
            holder.item_location.setText(String.format(holder.item_location.getContext().getString(R.string.locationFormat),addresses.get(0).getLocality(), addresses.get(0).getSubAdminArea()));
        } catch (IOException e) {
            // No se pudo encontrar una dirección se establece nulo
            holder.item_location.setVisibility(View.GONE);
        }
        holder.item_title.setText(device.getName());
        Date lastUpdated = new Date(device.getLastMessage().getDate().getSeconds());
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        holder.item_last_updated.setText(mFormat.format(lastUpdated));
        holder.item_temp.setText(String.valueOf(device.getLastMessage().getTemp()));
        holder.item_hum.setText(String.valueOf(device.getLastMessage().getHum()));
        holder.item_press.setText(String.valueOf(device.getLastMessage().getPres()));
        holder.item_uv.setText(String.valueOf(device.getLastMessage().getUv()));
        holder.item_map.onCreate(null);
        holder.item_map.onResume();
        holder.item_map.getMapAsync(new OnMapReadyCallback() {
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
        return filteredDevices.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String text=constraint.toString().toLowerCase();
                if(text.isEmpty()){
                    filteredDevices=devices;
                }else{
                    LinkedList<DeviceModel> filterList=new LinkedList<>();
                    for(DeviceModel deviceModel: devices){
                        if(deviceModel.getName().toLowerCase().contains(text)){
                            filterList.add(deviceModel);
                        }
                    }
                    filteredDevices=filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=filteredDevices;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredDevices = (LinkedList<DeviceModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateItems(LinkedList<DeviceModel> devices){
        this.devices.clear();
        this.devices.addAll(devices);
        notifyDataSetChanged();
    }

}
