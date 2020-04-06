package com.android.tfg.adapter;

import android.content.Intent;
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

import java.util.LinkedList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    // EXPAND VIEW
    LinearLayoutCompat expandView;
    Button expandButton;
    Button moreButton;
    CardView cardView;

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
        /***************
         * EXPAND VIEW *
         ***************/
        expandView = holder.itemView.findViewById(R.id.expandView);
        expandButton = holder.itemView.findViewById(R.id.expandButton);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandView.setVisibility(View.VISIBLE);
                    expandButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_24dp);
                }else{
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandView.setVisibility(View.GONE);
                    expandButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_24dp);
                }
            }
        });
        moreButton = holder.itemView.findViewById(R.id.moreButton);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("BOTON", "DONE");
                Intent i = new Intent(v.getContext(), MoreActivity.class);
                v.getContext().startActivity(i);
            }
        });
        cardView = holder.itemView.findViewById(R.id.cardViewSearch);


        /****************
         * DEFAULT VIEW *
         ****************/
        holder.item_title.setText(filteredDevices.get(position).getName());
        holder.item_temp.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getTemp())+" ºC");
        holder.item_hum.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getHum())+" %");
        holder.item_press.setText(String.valueOf(filteredDevices.get(position).getLastMessage().getPres())+" hPa");
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
