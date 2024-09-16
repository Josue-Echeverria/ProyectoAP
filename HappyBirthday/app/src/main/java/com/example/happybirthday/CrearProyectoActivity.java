package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearProyectoActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_proyecto);
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearProyectoActivity.this, MisProyectosActivity.class);
                startActivity(intent);
            }
        });
        Button create = findViewById(R.id.save_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextNombre = findViewById(R.id.project_name);
                String nombre = editTextNombre.getText().toString();
                EditText editTextdescripcion = findViewById(R.id.project_description);
                String descripcion = editTextdescripcion.getText().toString();
                EditText editTextobjetivo = findViewById(R.id.financing_objective);
                String objetivo = editTextobjetivo.getText().toString();
                EditText editTextfechalimnite = findViewById(R.id.deadline);
                String fechalimnite = editTextfechalimnite.getText().toString();


                System.out.println(nombre);
                // TODO : RECOLECTAR TODA LA INFO Y HACER UN POST A LA BASE DE DATOS
            }
        });
    }
}
