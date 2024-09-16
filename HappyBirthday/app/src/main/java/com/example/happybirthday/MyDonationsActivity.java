package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyDonationsActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_donaciones);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMisDonaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: JALAR Lsas donaciones del mae
        List<Proyecto> proyectoList = new ArrayList<>();
        proyectoList.add(new Proyecto("Artículo 1", "Descripción del artssssssssssssssículo 1 sogniw oasbnl spgbmsam pgv ", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));

        ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_mi_donacion) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Proyecto proyecto = proyectoList.get(position);
                TextView title = holder.itemView.findViewById(R.id.project_title);
                TextView description = holder.itemView.findViewById(R.id.project_description);
                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                TextView price = holder.itemView.findViewById(R.id.project_price);
                // TODO Remplazar price por la donacion que haya hecho el mae
                price.setText("109");
                title.setText(proyecto.getTitle());
                description.setText(proyecto.getDescription());
                deadline.setText(proyecto.getFechaLimite());

            }
        };
        recyclerView.setAdapter(proyectoAdapter);
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDonationsActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }
}
