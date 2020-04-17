package com.android.tfg.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.google.android.gms.maps.MapView;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {

    public MapView item_map_fav;
    public TextView item_title_fav;
    public TextView item_location_fav;
    public TextView item_last_updated;
    public TextView item_temp_fav;
    public TextView item_hum_fav;
    public TextView item_press_fav;
    public TextView item_uv_fav;

    public FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);

        /***********************
        * COMPONENTES DEL ITEM *
        ************************/
        item_map_fav=itemView.findViewById(R.id.item_map_fav);
        item_title_fav=itemView.findViewById(R.id.item_title_fav);
        item_location_fav=itemView.findViewById(R.id.item_location_fav);
        item_last_updated=itemView.findViewById(R.id.item_last_update_fav);
        item_temp_fav=itemView.findViewById(R.id.item_temp_fav);
        item_hum_fav=itemView.findViewById(R.id.item_hum_fav);
        item_press_fav=itemView.findViewById(R.id.item_pres_fav);
        item_uv_fav=itemView.findViewById(R.id.item_uv_fav);
    }
}
