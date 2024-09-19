package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences sharedPreferences;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiService apiService = retrofit.create(ApiService.class);
        setContentView(R.layout.crear_proyecto);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

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

                String creator = sharedPreferences.getString("username", null);
                Call<ResponseBody> call = apiService.postProyecto(JsonParser.parseString("{\"name\":\"" + nombre + "\",\"description\":\"" + descripcion  + "\",\"creator\":\"" +creator + "\",\"goal\":\"" + objetivo + "\",\"endDate\":\"" + fechalimnite + "\"}").getAsJsonObject());

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
