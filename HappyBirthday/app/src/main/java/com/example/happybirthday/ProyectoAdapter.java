package com.example.happybirthday;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder> {


    private List<Proyecto> ProyectoList;

    public ProyectoAdapter(List<Proyecto> ProyectoList) {
        this.ProyectoList = ProyectoList;
    }

    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
        return new ProyectoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProyectoViewHolder holder, int position) {
        Proyecto Proyecto = ProyectoList.get(position);
        holder.ProyectoTitle.setText(Proyecto.getTitle());
        holder.ProyectoDescription.setText(Proyecto.getDescription());
        holder.ProyectoFechaLimite.setText(Proyecto.getFechaLimite());
    }

    @Override
    public int getItemCount() {
        return ProyectoList.size();
    }

    public static class ProyectoViewHolder extends RecyclerView.ViewHolder {

        TextView ProyectoTitle;
        TextView ProyectoDescription;
        TextView ProyectoFechaLimite;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);
            ProyectoTitle = itemView.findViewById(R.id.project_title);
            ProyectoDescription = itemView.findViewById(R.id.project_description);
            ProyectoFechaLimite = itemView.findViewById(R.id.project_deadline);
        }
    }
}
