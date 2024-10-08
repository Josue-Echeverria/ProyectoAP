package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
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

public class AdminActivity    extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Proyecto> proyectoList;
    private ItemAdapter<Proyecto> proyectoAdapter;
    private ArrayList<Donacion> donacionList;
    private ItemAdapter<Donacion> donacionAdapter;
    private ArrayList<Usuario> usuarioList;
    private ItemAdapter<Usuario> usuarioAdapter;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_screen1);
        ApiService apiService = retrofit.create(ApiService.class);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                            proyectoList.add(new Proyecto(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("endDate").getAsString(), jsonObject.get("gathered").getAsString(), jsonObject.get("logo").getAsString()));

                            System.out.println(jsonObject);
                        }
                        ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_proyecto) {
                            @Override
                            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                Proyecto proyecto = proyectoList.get(position);
                                TextView title = holder.itemView.findViewById(R.id.project_title);
                                TextView description = holder.itemView.findViewById(R.id.project_description);
                                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                                ImageView imagen = holder.itemView.findViewById(R.id.project_image1);

                                Picasso.get().load(proyecto.getImg()).into(imagen);
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
                                        Intent intent = new Intent(AdminActivity.this, DetallesActivity.class);
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

        recyclerView.setAdapter(proyectoAdapter);

        TextView donacionesText = findViewById(R.id.donaciones_text);
        donacionesText.setOnClickListener(v -> {
            setSelected(donacionesText);
            Call<ResponseBody> call1 = apiService.getDonaciones();
            call1.enqueue(new Callback<ResponseBody>() {
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

                            List<Donacion> donacionList = new ArrayList<>();
                            for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                                System.out.println(jsonObject);
                                donacionList.add(new Donacion(jsonObject.get("project").getAsString(), jsonObject.get("nameDonator").getAsString(), jsonObject.get("amount").getAsDouble(), jsonObject.get("date").getAsString()));
                            }

                            ItemAdapter proyectoAdapter = new ItemAdapter(donacionList, R.layout.item_donacion) {
                                @Override
                                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                    Donacion donacion = donacionList.get(position);
                                    TextView title = holder.itemView.findViewById(R.id.project_title);
                                    TextView amount = holder.itemView.findViewById(R.id.project_price);
                                    TextView donante = holder.itemView.findViewById(R.id.donante);
                                    TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                                    ImageView img = holder.itemView.findViewById(R.id.img_url_donacion);

                                    Call<ResponseBody> call1 = apiService.getProyecto(donacion.getProyecto());
                                    call1.enqueue(new Callback<ResponseBody>() {
                                                      @Override
                                                      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                          if (response.isSuccessful()) {
                                                              ResponseBody responseBody = response.body();
                                                              JsonArray jsonArray = null;
                                                              try {
                                                                  jsonArray = (JsonArray) JsonParser.parseString(responseBody.string());
                                                                  List<JsonObject> jsonObjectList = new ArrayList<>();
                                                                  for (JsonElement element : jsonArray) {
                                                                      System.out.println(element.getAsJsonObject());
                                                                      jsonObjectList.add(element.getAsJsonObject());
                                                                  }
                                                                  for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                                                                      Picasso.get().load(jsonObject.get("logo").getAsString()).into(img);
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
                                    amount.setText(donacion.getMonto().toString());
                                    title.setText(donacion.getProyecto());
                                    donante.setText(donacion.getDonante());
                                    deadline.setText(donacion.getFecha());
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
        });

        TextView usuariosText = findViewById(R.id.usuarios_text);
        usuariosText.setOnClickListener(v -> {
            setSelected(usuariosText);

            Call<ResponseBody> call0 = apiService.getUsers();
            call0.enqueue(new Callback<ResponseBody>() {
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
                            List<Usuario> userList = new ArrayList<>();
                            for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                                System.out.println(jsonObject);
                                if(!jsonObject.get("name").getAsString().equals("admin"))
                                    userList.add(new Usuario(jsonObject.get("name").getAsString(), jsonObject.get("phone").getAsString(), jsonObject.get("email").getAsString(), jsonObject.get("isActive").getAsInt()));
                            }
                            ItemAdapter userAdapter = new ItemAdapter(userList, R.layout.item_usuario) {
                                @Override
                                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                    Usuario usuario = userList.get(position);
                                    TextView nombre = holder.itemView.findViewById(R.id.nombre_item);
                                    TextView telefono = holder.itemView.findViewById(R.id.telefono_item);
                                    TextView correo = holder.itemView.findViewById(R.id.correo_item);
                                    Switch activo = holder.itemView.findViewById(R.id.switch1);

                                    if(usuario.getActivo() == 0) {
                                        activo.setChecked(false);
                                        activo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Call<ResponseBody> call07 = apiService.activateUser(
                                                        JsonParser.parseString("{\"name\":\"" + usuario.getnombre() + "\",\"action\":\"" + "activate" + "\"}")
                                                                .getAsJsonObject());
                                                call07.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            ResponseBody responseBody = response.body();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else {
                                        activo.setChecked(true);
                                        activo.setOnClickListener(v -> {
                                            Call<ResponseBody> call07 = apiService.activateUser(
                                                    JsonParser.parseString("{\"name\":\"" + usuario.getnombre() + "\",\"action\":\"" + "deactivate" + "\"}")
                                                            .getAsJsonObject());
                                            call07.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        ResponseBody responseBody = response.body();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                }
                                            });
                                        });
                                    }

                                    nombre.setText(usuario.getnombre());
                                    telefono.setText(usuario.getTelefono());
                                    correo.setText(usuario.getCorreo());

                                }
                            };
                            recyclerView.setAdapter(userAdapter);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        });

        TextView estadisticasText = findViewById(R.id.estadisticas_text);
        estadisticasText.setOnClickListener(v -> {
            setSelected(estadisticasText);

            Call<ResponseBody> call2 = apiService.getChartLink();
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        ResponseBody responseBody = response.body();
                        try {
                            JsonObject jsonObject = JsonParser.parseString(responseBody.string()).getAsJsonObject();
                            System.out.println(jsonObject);
                            List<Chart> chartlist = new ArrayList<>();
                            chartlist.add(new Chart(jsonObject.get("grafico_pastel_proyectos").getAsString()));
                            chartlist.add(new Chart(jsonObject.get("user_activity_chart").getAsString()));


                            ItemAdapter itemAdapter2 = new ItemAdapter(chartlist, R.layout.item_chart) {
                                @Override
                                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                    Chart chart = chartlist.get(position);
                                    ImageView chartView = holder.itemView.findViewById(R.id.chart);
                                    Picasso.get().load(chart.getImg_url()).into(chartView);
                                }
                            };
                            recyclerView.setAdapter(itemAdapter2);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });

        });

        TextView proyectosText = findViewById(R.id.proyectos_text);
        proyectosText.setOnClickListener(v -> {
            setSelected(proyectosText);

            Call<ResponseBody> call7 = apiService.getProyectos();
            call7.enqueue(new Callback<ResponseBody>() {
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
                            ItemAdapter proyectoAdapter = new ItemAdapter(proyectoList, R.layout.item_proyecto) {
                                @Override
                                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                    Proyecto proyecto = proyectoList.get(position);
                                    TextView title = holder.itemView.findViewById(R.id.project_title);
                                    TextView description = holder.itemView.findViewById(R.id.project_description);
                                    TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                                    ImageView imagen = holder.itemView.findViewById(R.id.project_image1);

                                    Picasso.get().load(proyecto.getImg()).into(imagen);
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
                                            Intent intent = new Intent(AdminActivity.this, DetallesActivity.class);
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
        });

        Button volver = findViewById(R.id.volver_button);
        volver.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void setSelected(TextView textView) {
        ((TextView) findViewById(R.id.donaciones_text)).setTypeface(null, Typeface.NORMAL);
        ((TextView) findViewById(R.id.usuarios_text)).setTypeface(null, Typeface.NORMAL);
        ((TextView) findViewById(R.id.estadisticas_text)).setTypeface(null, Typeface.NORMAL);
        ((TextView) findViewById(R.id.proyectos_text)).setTypeface(null, Typeface.NORMAL);
        textView.setTypeface(null, Typeface.BOLD);
    }

}
