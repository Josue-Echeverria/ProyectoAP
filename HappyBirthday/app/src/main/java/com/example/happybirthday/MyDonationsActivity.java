package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class MyDonationsActivity  extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String username;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_donaciones);;
        ApiService apiService = retrofit.create(ApiService.class);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMisDonaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<ResponseBody> call = apiService.getDonacionUser(username);
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

                        List<Donacion> donacionList = new ArrayList<>();
                        for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                            donacionList.add(new Donacion(jsonObject.get("project").getAsString(), jsonObject.get("nameDonator").getAsString(), jsonObject.get("amount").getAsDouble(), jsonObject.get("date").getAsString()));
                        }

                        ItemAdapter proyectoAdapter = new ItemAdapter(donacionList, R.layout.item_mi_donacion) {
                            @Override
                            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                Donacion donacion = donacionList.get(position);
                                TextView title = holder.itemView.findViewById(R.id.project_title);
                                TextView description = holder.itemView.findViewById(R.id.project_description);
                                TextView deadline = holder.itemView.findViewById(R.id.project_deadline);
                                TextView amount = holder.itemView.findViewById(R.id.project_price);

                                amount.setText(donacion.getMonto().toString());
                                title.setText(donacion.getProyecto());
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

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDonationsActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }
}
