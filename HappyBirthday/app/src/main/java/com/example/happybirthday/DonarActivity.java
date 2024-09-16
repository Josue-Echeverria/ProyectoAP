package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DonarActivity   extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hacer_donacion);

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonarActivity.this, DetallesActivity.class);
                startActivity(intent);
            }
        });
        Button donar = findViewById(R.id.btn_donate);
        donar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  TODO : ARREGLAR LA PANTALLA (NO DEBERIA DE PEDIR TANTA INFO)
                EditText amountEditText = findViewById(R.id.donation_amount);
                String amount = amountEditText.getText().toString();

                // TODO : MANDAR LA DONACION
            }
        });
    }
}
