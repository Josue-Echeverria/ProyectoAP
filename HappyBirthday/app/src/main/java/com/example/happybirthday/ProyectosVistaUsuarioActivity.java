package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProyectosVistaUsuarioActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proyectos);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: JALAR LOS PROYECTOS De LA BASE DE DATOS
        List<Proyecto> proyectoList = new ArrayList<>();
        proyectoList.add(new Proyecto("Artículo 1", "Descripción del artssssssssssssssículo 1 sogniw oasbnl spgbmsam pgv ", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));

        ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_proyecto) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Proyecto proyecto = proyectoList.get(position);
                TextView title = holder.itemView.findViewById(R.id.project_title);
                TextView description = holder.itemView.findViewById(R.id.project_description);
                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);

                title.setText(proyecto.getTitle());
                description.setText(proyecto.getDescription());
                deadline.setText(proyecto.getFechaLimite());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, DetallesActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(proyectoAdapter);

        Button btnProyectos = findViewById(R.id.btn_proyectos);
        btnProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, MisProyectosActivity.class);
                startActivity(intent);
            }
        });
        ImageView perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

    }

}
