package com.example.happybirthday


//import .R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var proyectoAdapter: ItemAdapter<Proyecto>
    private lateinit var proyectoList: MutableList<Proyecto>
    private lateinit var donacionAdapter: ItemAdapter<Donacion>
    private lateinit var donacionList: MutableList<Donacion>
    private lateinit var usuarioAdapter: ItemAdapter<Usuario>
    private lateinit var usuarioList: MutableList<Usuario>
    private lateinit var estadisticaAdapter: ItemAdapter<Donacion>
    private lateinit var estadisticaList: MutableList<Donacion>
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://oyster-robust-ghost.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = retrofit.create(ApiService::class.java)
        setContentView(R.layout.loginpage)

        findViewById<Button>(R.id.btn_login).setOnClickListener{
            val nombre : String = findViewById<TextInputEditText>(R.id.search_project).text.toString()
            val contrasena : String = findViewById<TextInputEditText>(R.id.password).text.toString()
            System.out.println("{username: $nombre, password: $contrasena}")

            val login = apiService.login(JsonParser.parseString("{username: $nombre, password: $contrasena}").asJsonObject)
            login.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val jsonresponse = JsonParser.parseString(responseBody.string()).asJsonObject
                        val existe: Int = jsonresponse.get("existe").asInt
                        if (existe == 0){
                            // TODO TIRAR UN POP UP
                        }else{
                            val isAdmin: Int = jsonresponse.get("isAdmin").asInt
                            if(isAdmin  == 0){
                                sharedPreferences = applicationContext.getSharedPreferences("UserSession", MODE_PRIVATE);
                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("username", nombre)
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()
                                val intent = Intent(this@MainActivity, ProyectosVistaUsuarioActivity::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@MainActivity, AdminActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}})
        }
        findViewById<TextView>(R.id.register_text).setOnClickListener{
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}