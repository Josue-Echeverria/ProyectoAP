package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallesActivity  extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_proyectos);
        ApiService apiService = retrofit.create(ApiService.class);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String proyectSelected = sharedPreferences.getString("currentProject", null);

        Call<ResponseBody> call = apiService.getProyecto(proyectSelected);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    JsonArray jsonArray = null;
                    try {
                        jsonArray = (JsonArray) JsonParser.parseString(responseBody.string());
                        List<JsonObject> jsonObjectList = new ArrayList<>();
                        for (JsonElement element : jsonArray) {
                            jsonObjectList.add(element.getAsJsonObject());
                        }
                        for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                            TextView title = findViewById(R.id.project_title);
                            title.setText(jsonObject.get("name").getAsString());
                            TextView description = findViewById(R.id.project_description);
                            description.setText(jsonObject.get("description").getAsString());
                            TextView deadline = findViewById(R.id.deadline_date);
                            deadline.setText(jsonObject.get("endDate").getAsString());
                            TextView recaudado = findViewById(R.id.raised_amount);
                            recaudado.setText(jsonObject.get("gathered").getAsString());

                            ImageView imagen = findViewById(R.id.project_image);
                            Picasso.get().load(jsonObject.get("logo").getAsString()).into(imagen);
                        };
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });


        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesActivity.this, ProyectosVistaUsuarioActivity.class);
                startActivity(intent);
            }
        });
        ImageView perfil = findViewById(R.id.btn_search);
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
