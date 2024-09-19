package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

public class EditarProyectoActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_proyecto);
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
                            EditText projectName = findViewById(R.id.project_name);
                            projectName.setText(jsonObject.get("name").getAsString());
                            EditText projectdescription = findViewById(R.id.project_description);
                            projectdescription.setText(jsonObject.get("description").getAsString());
                            EditText projectFinancingObjective = findViewById(R.id.financing_objective);
                            projectFinancingObjective.setText(jsonObject.get("gathered").getAsString());
                            EditText projectDeadline = findViewById(R.id.deadline);
                            projectDeadline.setText(jsonObject.get("endDate").getAsString());
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


        Button btnProyectos = findViewById(R.id.save_button);
        btnProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HOLAAAAAAAAA");
                EditText projectNameEdit = findViewById(R.id.project_name);
                String projectName = projectNameEdit.getText().toString();
                EditText projectdescriptionEdit = findViewById(R.id.project_description);
                String projectdescription = projectdescriptionEdit.getText().toString();
                EditText projectFinancingObjectiveEdit = findViewById(R.id.financing_objective);
                String projectFinancingObjective = projectFinancingObjectiveEdit.getText().toString();
                EditText projectDeadlineEdit = findViewById(R.id.deadline);
                String projectDeadline = projectDeadlineEdit.getText().toString();
                System.out.println(projectDeadline);
                System.out.println(projectFinancingObjective);
                System.out.println(projectdescription);
                System.out.println(projectName);
                Call<ResponseBody> call2 = apiService.putProyecto(
                        JsonParser.parseString("{\"description\":\"" + projectdescription + "\",\"name\":\"" + projectName +  "\",\"goal\":\"" + projectFinancingObjective +"\",\"endDate\":\"" + projectDeadline + "\"}")
                                .getAsJsonObject()
                );
                call2.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            // TODO TEST EL PUDATE
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            }
        });

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarProyectoActivity.this, MisProyectosActivity.class);
                startActivity(intent);
            }
        });

    }
}
