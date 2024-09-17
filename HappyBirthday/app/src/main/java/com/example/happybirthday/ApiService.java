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
    @POST("Login")
    Call<ResponseBody> login(@Body String usuario);

    @POST("addUser")
    Call<ResponseBody> postUser(@Body JsonObject usuario);

    @GET("user/{name}")
    Call<ResponseBody> getUser(@Path("name") String userName);

    @PUT("user/{name}")
    Call<ResponseBody> putUser(@Path("name") String userName, @Body String newUser);


    @GET("projects")
    Call<ResponseBody> getProyectos();

    @GET("/project/{projectName}")
    Call<ResponseBody> getProyecto(@Path("projectName") String projectName);

    @POST("project")
    Call<ResponseBody> postProyecto(@Body String proyecto);

    @PUT("project/{name}")
    Call<ResponseBody> putProyecto(@Path("name") String projectName, @Body String newProject);

    @DELETE("project/{name}")
    Call<ResponseBody> deleteProyecto(@Path("name") String projectName);



    @GET("donaciones")
    Call<ResponseBody> getDonaciones();

    @GET("donacion/{name}")
    Call<ResponseBody> getDonacionUser(@Path("name") String userName);

    @POST("donacion")
    Call<ResponseBody> postDonacion(@Body String donacion);
}
