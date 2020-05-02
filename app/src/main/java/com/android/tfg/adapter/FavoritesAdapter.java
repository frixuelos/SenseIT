package com.android.tfg.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemFavoritesBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewholder.FavoritesViewHolder;

import java.util.LinkedList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private LinkedList<DeviceModel> devices;

    public FavoritesAdapter(LinkedList<DeviceModel> devices){
        this.devices=devices;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Vista cardview
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFavoritesBinding binding = ItemFavoritesBinding.inflate(layoutInflater, parent, false);

        return new FavoritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, final int position) {
        DeviceModel device = devices.get(position);
        holder.bind(device);
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
