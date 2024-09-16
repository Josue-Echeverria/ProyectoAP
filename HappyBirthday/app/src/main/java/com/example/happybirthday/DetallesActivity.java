package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DetallesActivity  extends AppCompatActivity {
    // TODO : LLENAR LA INFO CON LOS DATOS QUE VENGAN DE LA BASE DE DATOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_proyectos);

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesActivity.this, ProyectosVistaUsuarioActivity.class);
                startActivity(intent);
            }
        });
        ImageView perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
        Button donate = findViewById(R.id.donate_button);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesActivity.this, DonarActivity.class);
                startActivity(intent);
            }
        });
    }
}
