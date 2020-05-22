package com.android.tfg.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemAlertBinding;
import com.android.tfg.databinding.ItemFavoritesBinding;
import com.android.tfg.model.AlertModel;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewholder.AlertViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;

public class AlertAdapter extends RecyclerView.Adapter<AlertViewHolder> {

    private ArrayList<AlertModel> userAlerts;

    public AlertAdapter(ArrayList<AlertModel> userAlerts){
        this.userAlerts=userAlerts;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // vista
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAlertBinding binding = ItemAlertBinding.inflate(layoutInflater, parent, false);

        return new AlertViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertModel alert = userAlerts.get(position);
        holder.bind(alert);
    }

    @Override
    public int getItemCount() {
        return userAlerts.size();
    }

    public AlertModel removeItem(int pos){
        AlertModel removed = userAlerts.get(pos);
        userAlerts.remove(pos);
        notifyItemRemoved(pos);
        return removed;
    }

    public void insertItem(AlertModel alert, int pos){
        userAlerts.add(pos, alert);
        notifyItemInserted(pos);
    }

}
