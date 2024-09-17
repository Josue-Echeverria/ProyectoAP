package com.example.happybirthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
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

public class RegisterActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://8464-201-192-142-225.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        ApiService apiService = retrofit.create(ApiService.class);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        Button register = findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameEditText = findViewById(R.id.nombre);
                String name = nameEditText.getText().toString();

                EditText amountEditText = findViewById(R.id.money);
                String amount = amountEditText.getText().toString();

                EditText correoEditText = findViewById(R.id.correo);
                String correo = correoEditText.getText().toString();

                EditText passwordEditText = findViewById(R.id.password);
                String password = passwordEditText.getText().toString();

                EditText phoneEditText = findViewById(R.id.phone);
                String phone = phoneEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", name);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                Call<ResponseBody> call = apiService.postUser(JsonParser.parseString("{name:"+name+",email:"+correo+",password:"+password+",phone:"+phone+",wallet:"+amount+"}").getAsJsonObject());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle the error
                    }
                });

                Intent intent = new Intent(RegisterActivity.this, ProyectosVistaUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }
}
