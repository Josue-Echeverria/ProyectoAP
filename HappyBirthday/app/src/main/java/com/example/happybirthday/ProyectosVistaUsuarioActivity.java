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

import com.google.gson.Gson;
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

public class ProyectosVistaUsuarioActivity  extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://8464-201-192-142-225.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proyectos);
        ApiService apiService = retrofit.create(ApiService.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Proyecto> proyectoList = new ArrayList<>();
        Call<ResponseBody> call = apiService.getProyectos();
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
                            proyectoList.add(new Proyecto(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("endDate").getAsString()));

                            System.out.println(jsonObject);
                        }
                        ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_proyecto) {
                            @Override
                            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                Proyecto proyecto = proyectoList.get(position);
                                TextView title = holder.itemView.findViewById(R.id.project_title);
                                TextView description = holder.itemView.findViewById(R.id.project_description);
                                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);

                                title.setText(proyecto.getTitle());
                                description.setText(proyecto.getDescription());
                                deadline.setText(proyecto.getFechaLimite());
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserSession", MODE_PRIVATE);
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("currentProject", proyecto.getTitle());
                                        editor.apply();
                                        Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, DetallesActivity.class);
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


        Button btnProyectos = findViewById(R.id.btn_proyectos);
        btnProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, MisProyectosActivity.class);
                startActivity(intent);
            }
        });
        ImageView perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
        ImageView search = findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView projectNameSearch = findViewById(R.id.search_project);
                String projectName = projectNameSearch.getText().toString();

                if (projectName.isEmpty()) {
                    List<Proyecto> proyectoList = new ArrayList<>();
                    proyectoList.add(new Proyecto("Artículo 1", "Descripción del artssssssssssssssículo 1 sogniw oasbnl spgbmsam pgv ", "10/12/2025"));

                    ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_proyecto) {

                        @Override
                        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                            Proyecto proyecto = proyectoList.get(position);
                            TextView title = holder.itemView.findViewById(R.id.project_title);
                            TextView description = holder.itemView.findViewById(R.id.project_description);
                            TextView deadline = holder.itemView.findViewById(R.id.project_deadline);

                            title.setText(proyecto.getTitle());
                            description.setText(proyecto.getDescription());
                            deadline.setText(proyecto.getFechaLimite());
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserSession", MODE_PRIVATE);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("currentProject", proyecto.getTitle());
                                    editor.apply();
                                    Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, DetallesActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    recyclerView.setAdapter(proyectoAdapter);
                }
                else{
                    Call<ResponseBody> call = apiService.getProyecto(projectName);
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
                                                     proyectoList.add(new Proyecto(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("endDate").getAsString()));

                                                     System.out.println(jsonObject);
                                                 }
                                                 ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_proyecto) {
                                                     @Override
                                                     public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                                         Proyecto proyecto = proyectoList.get(position);
                                                         TextView title = holder.itemView.findViewById(R.id.project_title);
                                                         TextView description = holder.itemView.findViewById(R.id.project_description);
                                                         TextView deadline = holder.itemView.findViewById(R.id.project_deadline);

                                                         title.setText(proyecto.getTitle());
                                                         description.setText(proyecto.getDescription());
                                                         deadline.setText(proyecto.getFechaLimite());
                                                         SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserSession", MODE_PRIVATE);
                                                         holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                 editor.putString("currentProject", proyecto.getTitle());
                                                                 editor.apply();
                                                                 Intent intent = new Intent(ProyectosVistaUsuarioActivity.this, DetallesActivity.class);
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

                }

            }
        });

    }
}
