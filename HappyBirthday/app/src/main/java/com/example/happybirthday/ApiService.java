package com.example.happybirthday;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("titulo/matrix")
    Call<ResponseBody> getClients();

    @POST("/endpoint")
    Call<Usuario> createClient(@Body Usuario client);

    @POST("Login")
    Call<ResponseBody> login(@Body String usuario);
}
