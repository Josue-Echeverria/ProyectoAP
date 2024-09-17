package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EditarProyectoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_proyecto);
        // TODO : LLENAR TODOS LOS CAMPOS CON LOS DATOS ACTUALES DEL PROYECTO

        EditText projectName = findViewById(R.id.project_name);
        projectName.setText("Datos de la base de datos");
        EditText projectdescription = findViewById(R.id.project_description);
        projectdescription.setText("Datos de la base de datos");
        EditText projectFinancingObjective = findViewById(R.id.financing_objective);
        projectFinancingObjective.setText("Datos de la base de datos");
        EditText projectDeadline = findViewById(R.id.deadline);
        projectDeadline.setText("Datos de la base de datos");

        Button btnProyectos = findViewById(R.id.save_button);
        btnProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : GUARDAR LOS DATOS EN LA BASE DE DATOS AQUI
            }
        });

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarProyectoActivity.this, MisProyectosActivity.class);
                startActivity(intent);
            }
        });

    }
}
