package com.example.happybirthday;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("verifyUser")
    Call<ResponseBody> login(@Body JsonObject usuario);

    @POST("addUser")
    Call<ResponseBody> postUser(@Body JsonObject usuario);

    @GET("user/{name}")
    Call<ResponseBody> getUser(@Path("name") String userName);

    @GET("users")
    Call<ResponseBody> getUsers();

    @PUT("user/{name}")
    Call<ResponseBody> putUser(@Path("name") String userName, @Body String newUser);

    @POST("update_password")
    Call<ResponseBody> update_password(@Body JsonObject usuario);

    @GET("projects")
    Call<ResponseBody> getProyectos();

    @GET("/projectFull/{projectName}")
    Call<ResponseBody> getProyecto(@Path("projectName") String projectName);

    @GET("/projectByName/{creatorName}")
    Call<ResponseBody> getProyectobyUser(@Path("creatorName") String creatorName);

    @POST("addProyecto")
    Call<ResponseBody> postProyecto(@Body JsonObject proyecto);

    @POST("updateProject")
    Call<ResponseBody> putProyecto(@Body JsonObject newProject);

    @DELETE("project/{name}")
    Call<ResponseBody> deleteProyecto(@Path("name") String projectName);



    @GET("donations")
    Call<ResponseBody> getDonaciones();

    @GET("donation/{donatorName}")
    Call<ResponseBody> getDonacionUser(@Path("donatorName") String userName);

    @POST("addDonacion")
    Call<ResponseBody> postDonacion(@Body JsonObject donacion);
}
