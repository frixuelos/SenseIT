package com.android.tfg.adapter;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tfg.databinding.ItemMoreBinding;
import com.android.tfg.model.DeviceModel;
import com.android.tfg.model.MessageModel;
import com.android.tfg.viewholder.MoreViewHolder;
import com.android.tfg.viewmodel.MoreViewModel;

import java.util.LinkedList;

public class MoreAdapter extends RecyclerView.Adapter<MoreViewHolder> {

    private LinkedList<MessageModel> messages;
    private MoreViewModel moreViewModel;

    public MoreAdapter(LinkedList<MessageModel> messages, MoreViewModel moreViewModel){
        this.messages=messages;
        this.moreViewModel=moreViewModel;
    }

    @NonNull
    @Override
    public MoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Vista cardview
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMoreBinding binding = ItemMoreBinding.inflate(layoutInflater, parent, false);

        return new MoreViewHolder(binding, moreViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreViewHolder holder, final int position) {
        MessageModel message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public MessageModel removeItem(int pos){
        MessageModel removed = messages.get(pos);
        messages.remove(pos);
        notifyItemRemoved(pos);
        return removed;
    }

    public void insertItem(MessageModel message, int pos){
        messages.add(pos, message);
        notifyItemInserted(pos);
    }

    public void updateItems(LinkedList<MessageModel> messages){
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void clear(){
        this.messages.clear();
        notifyDataSetChanged();
    }

}
