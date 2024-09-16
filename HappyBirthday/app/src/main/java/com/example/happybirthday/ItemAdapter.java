package com.example.happybirthday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter<T> extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder<T>> {

    private List<T> itemList;
    private int layoutId;
    private Binder<T> binder;

    // Interfaz para realizar el enlace de datos en cada ViewHolder
    public interface Binder<T> {
        void bind(ItemViewHolder<T> holder, T item, int position);
    }

    // Constructor genérico
    public ItemAdapter(List<T> itemList, int layoutId) {
        this.itemList = itemList;
        this.layoutId = layoutId;
        this.binder = binder;
    }

    @NonNull
    @Override
    public ItemViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ItemViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder<T> holder, int position) {
        T item = itemList.get(position);
        binder.bind(holder, item, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ItemViewHolder<T> extends RecyclerView.ViewHolder {

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        // Método genérico para obtener las vistas desde el itemView
        public <V extends View> V findView(int id) {
            return itemView.findViewById(id);
        }
    }
}
