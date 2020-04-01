package com.android.tfg.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.google.android.gms.maps.MapView;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    public MapView item_map;
    public TextView item_title;
    public TextView item_description;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);

        /***********************
        * COMPONENTES DEL ITEM *
        ************************/
        item_map=itemView.findViewById(R.id.item_map);
        item_title=itemView.findViewById(R.id.item_title);
        item_description=itemView.findViewById(R.id.item_description);

    }
}
