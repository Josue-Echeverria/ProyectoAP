package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class PerfilActivity   extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String username;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://8464-201-192-142-225.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_usuario_verificado);
        ApiService apiService = retrofit.create(ApiService.class);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        Call<ResponseBody> call = apiService.getUser(username);
        call.enqueue(new Callback<ResponseBody>()  {
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
                        } // TODO TEST THIS (INFO DEL USER SE FORMATEA BIEN EN LA INTERFAZ)
                        for (com.google.gson.JsonObject jsonObject : jsonObjectList) {
                            TextView name = findViewById(R.id.full_name);
                            name.setText(jsonObject.get("name").getAsString());
                            TextView correo = findViewById(R.id.correo);
                            correo.setText(jsonObject.get("email").getAsString());
                            TextView amount = findViewById(R.id.digital_wallet_amount);
                            amount.setText(jsonObject.get("phone").getAsString());
                            TextView phone = findViewById(R.id.phone_number);
                            phone.setText(jsonObject.get("wallet").getAsString());
                        }
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
