package com.android.tfg.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.google.android.gms.maps.MapView;

import org.w3c.dom.Text;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    public MapView item_map;
    public TextView item_title;
    public TextView item_temp;
    public TextView item_hum;
    public TextView item_press;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);

        /***********************
        * COMPONENTES DEL ITEM *
        ************************/
        item_map=itemView.findViewById(R.id.item_map);
        item_title=itemView.findViewById(R.id.item_title);
        item_temp=itemView.findViewById(R.id.item_temp);
        item_hum=itemView.findViewById(R.id.item_hum);
        item_press=itemView.findViewById(R.id.item_press);


    }
}
