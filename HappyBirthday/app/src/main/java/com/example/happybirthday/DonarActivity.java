package com.example.happybirthday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DonarActivity   extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hacer_donacion);
        ApiService apiService = retrofit.create(ApiService.class);

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonarActivity.this, DetallesActivity.class);
                startActivity(intent);
            }
        });
        Button donar = findViewById(R.id.btn_donate);
        donar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amountEditText = findViewById(R.id.donation_amount);
                String amount = amountEditText.getText().toString();

                sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                String proyectSelected = sharedPreferences.getString("currentProject", null);
                String name = sharedPreferences.getString("username", null);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateAndTime = sdf.format(Calendar.getInstance().getTime());
                Call<ResponseBody> call = apiService.postDonacion(
                        JsonParser.parseString("{\"name\":\"" + name + "\",\"date\":\"" + currentDateAndTime + "\",\"projectName\":\"" + proyectSelected + "\",\"amount\":\"" + amount + "\"}")
                                .getAsJsonObject()
                );

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            Intent intent = new Intent(DonarActivity.this, DetallesActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle the error
                    }
                });
            }
        });
    }
}
