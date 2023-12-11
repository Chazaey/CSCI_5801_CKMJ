package com.example.ckmj_se_project.ui.Backend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ckmj_se_project.R;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList = new ArrayList<>();

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
        holder.itemNameTextView.setText(currentItem.getName());
        holder.itemQuantityTextView.setText(String.valueOf(currentItem.getAmount()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Update dataset of adapter & refresh the view
    public void setItems(List<Item> newItems) {
        itemList = newItems;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemQuantityTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemQuantityTextView = itemView.findViewById(R.id.item_quantity);
        }
    }
}
