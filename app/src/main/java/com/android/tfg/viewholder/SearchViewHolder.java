package com.android.tfg.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.R;
import com.google.android.gms.maps.MapView;

import org.w3c.dom.Text;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    public MapView item_map;
    public TextView item_title;
    public TextView item_location;
    public TextView item_last_updated;
    public TextView item_temp;
    public TextView item_hum;
    public TextView item_press;
    public TextView item_uv;
    public Button expand_button;
    public LinearLayoutCompat expand_view;
    public Button more_button;
    public ImageView item_fav_check;
    public CardView card_view;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);

        /***********************
        * COMPONENTES DEL ITEM *
        ************************/
        item_map=itemView.findViewById(R.id.item_map);
        item_title=itemView.findViewById(R.id.item_title);
        item_location=itemView.findViewById(R.id.item_location);
        item_last_updated=itemView.findViewById(R.id.item_last_update);
        item_temp=itemView.findViewById(R.id.item_temp);
        item_hum=itemView.findViewById(R.id.item_hum);
        item_press=itemView.findViewById(R.id.item_pres);
        item_uv=itemView.findViewById(R.id.item_uv);
        expand_button=itemView.findViewById(R.id.expandButton);
        expand_view=itemView.findViewById(R.id.expandView);
        more_button=itemView.findViewById(R.id.moreButton);
        card_view=itemView.findViewById(R.id.cardViewSearch);
        item_fav_check=itemView.findViewById(R.id.item_fav_check);
    }
}
