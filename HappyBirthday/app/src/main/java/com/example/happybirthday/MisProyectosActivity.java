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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;

public class MisProyectosActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String username;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_proyectos);
        ApiService apiService = retrofit.create(ApiService.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUserSProyects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        List<Proyecto> proyectoList = new ArrayList<>();
        Call<ResponseBody> call = apiService.getProyectobyUser(username);
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

                        List<Proyecto> proyectoList = new ArrayList<>();
                        for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                            proyectoList.add(new Proyecto(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("endDate").getAsString(), jsonObject.get("gathered").getAsString(), jsonObject.get("logo").getAsString()));
                        }

                        ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_mi_proyecto) {
                            @Override
                            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                Proyecto proyecto = proyectoList.get(position);
                                TextView title = holder.itemView.findViewById(R.id.project_title);
                                TextView description = holder.itemView.findViewById(R.id.project_description);
                                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                                TextView recaudado = holder.itemView.findViewById(R.id.project_price);
                                ImageView imagen = holder.itemView.findViewById(R.id.project_image);

                                Picasso.get().load(proyecto.getImg()).into(imagen);
                                title.setText(proyecto.getTitle());
                                description.setText(proyecto.getDescription());
                                deadline.setText(proyecto.getFechaLimite());
                                recaudado.setText(proyecto.getRecaudado());

                                holder.itemView.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MisProyectosActivity.this, EditarProyectoActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        };

                        recyclerView.setAdapter(proyectoAdapter);

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
