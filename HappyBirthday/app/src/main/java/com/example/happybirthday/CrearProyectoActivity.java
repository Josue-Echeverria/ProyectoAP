package com.example.happybirthday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonParser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearProyectoActivity  extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://8464-201-192-142-225.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiService apiService = retrofit.create(ApiService.class);
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
//{"_id":{"$oid":"66e7bdc8245bf9fd12c80c9a"},"name":"Give Ana a hand","description":"Help my daughter Ana, a little girl with cancer","goal":{"$numberInt":"5000"},"endDate":"12-10-2024","gathered":{"$numberInt":"2000"},"state":{"$numberInt":"1"},"creator":"harlen"}


                Call<ResponseBody> call = apiService.postUser(JsonParser.parseString("{name:"+nombre+",description:"+descripcion+",goal:"+objetivo+",endDate:"+fechalimnite+"}").getAsJsonObject());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            Intent intent = new Intent(CrearProyectoActivity.this, MisProyectosActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle the error
                    }
                });
            }
        });
    }
}
