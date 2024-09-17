package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity   extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_usuario_verificado);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        // TODO : LLENAR LA INFO CON LOS DATOS QUE VENGAN DE LA BASE DE DATOS

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, ProyectosVistaUsuarioActivity.class);
                startActivity(intent);
            }
        });
        Button changePassword = findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        Button donaciones = findViewById(R.id.my_donations);
        donaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, MyDonationsActivity.class);
                startActivity(intent);
            }
        });
    }
}
