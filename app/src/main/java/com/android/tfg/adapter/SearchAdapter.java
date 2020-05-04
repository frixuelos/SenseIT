package com.android.tfg.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemSearchBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.viewholder.SearchViewHolder;
import com.android.tfg.viewmodel.MainViewModel;

import java.util.LinkedList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {

    private LinkedList<DeviceModel> devices;
    private LinkedList<DeviceModel> filteredDevices;
    private MainViewModel mainViewModel;

    public SearchAdapter(LinkedList<DeviceModel> devices, MainViewModel mainViewModel){
        this.devices=devices;
        this.filteredDevices=new LinkedList<DeviceModel>();
        this.filteredDevices.addAll(this.devices);
        this.mainViewModel=mainViewModel;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Vista cardview
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSearchBinding binding = ItemSearchBinding.inflate(layoutInflater, parent, false);

        return new SearchViewHolder(binding, mainViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        DeviceModel device = filteredDevices.get(position);
        holder.bind(device);
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

    public void clearFilter(Filter filter){
        filter=null;
        if(this.filteredDevices.equals(devices)){return;} // Si son iguales no es necesario
        // Si no lo son se elimina el filtro
        this.filteredDevices.clear();
        this.filteredDevices.addAll(devices);
        notifyDataSetChanged();
    }

    public void updateItems(LinkedList<DeviceModel> devices){
        this.devices.clear();
        this.devices.addAll(devices);
        notifyDataSetChanged();
    }

    public void updateItem(String device){
        for(DeviceModel deviceModel : this.devices){
            if(deviceModel.getId().equals(device)){
                notifyItemChanged(devices.indexOf(deviceModel));
            }
        }
    }

}
