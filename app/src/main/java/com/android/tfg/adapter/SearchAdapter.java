package com.android.tfg.adapter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.android.tfg.databinding.ItemSearchBinding;
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSearchBinding binding = ItemSearchBinding.inflate(layoutInflater, parent, false);

        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        DeviceModel device = filteredDevices.get(position);
        holder.bind(device);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), MoreActivity.class);
            i.putExtra("device", device.getDeviceID());
            v.getContext().startActivity(i);
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
