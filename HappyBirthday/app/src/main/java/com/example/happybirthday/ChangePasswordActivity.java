package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity    extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambio_contrasenna);
        ApiService apiService = retrofit.create(ApiService.class);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        Button donar = findViewById(R.id.btn_cambiarpass);
        donar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String nombre = sharedPreferences.getString("username", null);
                 EditText passActualEditText = findViewById(R.id.current_password);
                 String passActual = passActualEditText.getText().toString();
                 JsonObject loginData = JsonParser.parseString("{username: " + nombre + ", password: " + passActual + "}").getAsJsonObject();


                 Call<ResponseBody> login = apiService.login(loginData);
                 login.enqueue(new Callback<ResponseBody>() {
                     @Override
                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                         if (response.isSuccessful()) {
                             ResponseBody responseBody = response.body();
                             if (responseBody != null) {
                                 JsonObject jsonresponse = null;
                                 try {
                                     jsonresponse = JsonParser.parseString(responseBody.string()).getAsJsonObject();
                                     int existe = jsonresponse.get("existe").getAsInt();
                                     System.out.println(loginData);
                                     System.out.println(jsonresponse);

                                     if (existe == 0) {
                                         // TODO: Mostrar un pop-up
                                     } else {
                                         EditText passNuevoEditText = findViewById(R.id.new_password);
                                         String passNuevo = passNuevoEditText.getText().toString();
                                         EditText passNuevoConfirmadoEditText = findViewById(R.id.confirm_new_password);
                                         String passNuevoConfirmado = passNuevoConfirmadoEditText.getText().toString();
                                         if (passNuevo.equals(passNuevoConfirmado)){

                                             JsonObject loginData2 = JsonParser.parseString("{userName: " + nombre + ", password: " + passNuevo + "}").getAsJsonObject();
                                             System.out.println("{userName: " + nombre + ", password: " + passNuevo + "}");
                                             Call<ResponseBody> change = apiService.update_password(loginData2);
                                             change.enqueue(new Callback<ResponseBody>() {
                                                 @Override
                                                 public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                     if (response.isSuccessful()) {
                                                         ResponseBody responseBody = response.body();
                                                         Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                                         startActivity(intent);
                                                     }
                                                 }

                                                 @Override
                                                 public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                 }
                                             });
                                         }
                                     }
                                 } catch (IOException e) {
                                     throw new RuntimeException(e);
                                 }
                             }
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
                Intent intent = new Intent(ChangePasswordActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }
}
