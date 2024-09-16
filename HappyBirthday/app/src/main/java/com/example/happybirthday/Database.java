package com.example.happybirthday;


import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Database {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://611d-201-192-142-225.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    /**
    public Boolean Login(String nombre, String contrasena) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> login = apiService.login("{username: " + nombre + ", password: " + contrasena + "}");
        ResponseBody responseBody;
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    responseBody = response.body();
                    try {
                        JsonObject jsonResponse = JsonParser.parseString(responseBody.string()).getAsJsonObject();
                        findViewById<Button>(R.id.btn_login).setOnClickListener{
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Manejar el error
            }
        });
*/
    }
