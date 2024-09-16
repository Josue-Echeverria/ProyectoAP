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

public class MisProyectosActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_proyectos);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUserSProyects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: JALAR LOS PROYECTOS De LA BASE DE DATOS CON BASE AL USAURIO LOGEADO
        List<Proyecto> proyectoList = new ArrayList<>();
        proyectoList.add(new Proyecto("Artículo 1", "Descripción del artssssssssssssssículo 1 sogniw oasbnl spgbmsam pgv ", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));
        proyectoList.add(new Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"));

        ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_mi_proyecto) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Proyecto proyecto = proyectoList.get(position);
                TextView title = holder.itemView.findViewById(R.id.project_title);
                TextView description = holder.itemView.findViewById(R.id.project_description);
                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                TextView recaudado = holder.itemView.findViewById(R.id.project_price);
                // TODO : QUitar este dato
                recaudado.setText("1222222");
                title.setText(proyecto.getTitle());
                description.setText(proyecto.getDescription());
                deadline.setText(proyecto.getFechaLimite());
                holder.itemView.findViewById(R.id.perfil).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MisProyectosActivity.this, EditarProyectoActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(proyectoAdapter);

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisProyectosActivity.this, ProyectosVistaUsuarioActivity.class);
                startActivity(intent);
            }
        });
        Button new_proyect = findViewById(R.id.crear_proyecto);
        new_proyect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisProyectosActivity.this, CrearProyectoActivity.class);
                startActivity(intent);
            }
        });

    }

}
