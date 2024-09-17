package com.example.happybirthday;

import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHelper {
    private SharedPreferences sharedPreferences;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://8464-201-192-142-225.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ApiService apiService = retrofit.create(ApiService.class);
    public APIHelper(){

    }

    public List<JsonObject> getProyecto(String proyectSelected){
        List<JsonObject> toReturn = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        Call<ResponseBody> call = apiService.getProyecto(proyectSelected);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    JsonArray jsonArray = null;
                    try {
                        jsonArray = (JsonArray) JsonParser.parseString(responseBody.string());
                        for (JsonElement element : jsonArray) {
                            toReturn.add(element.getAsJsonObject());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                latch.countDown();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        try {
            latch.await(10, TimeUnit.SECONDS); // Espera hasta 10 segundos para que la llamada termine
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (com.google.gson.JsonObject jsonObject : toReturn) {
            System.out.println(jsonObject);
        }
        return toReturn;
    }
}
