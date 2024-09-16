package com.example.happybirthday


//import .R

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {


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
        .baseUrl(" https://611d-201-192-142-225.ngrok-free.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = retrofit.create(ApiService::class.java)
        setContentView(R.layout.loginpage)



        findViewById<Button>(R.id.btn_login).setOnClickListener{
            val nombre : String = findViewById<TextInputEditText>(R.id.username).text.toString()
            val contrasena : String = findViewById<TextInputEditText>(R.id.password).text.toString()
            // TODO : QUE SE CONECTE CON LA API BASE DE DATOS (DESCOMENTAR LO DE ABAJO )
            /**   val login = apiService.login("{username: $nombre, password: $contrasena}")
            login.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
            val jsonresponse = JsonParser.parseString(responseBody.string())
            AQUI DEBERIA DE VENIR LA RESPUESTA
            }
            }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            // Handle the error
            }
            })*/
            if(nombre.equals("hola")  && contrasena.equals("hola")){
                val intent = Intent(this@MainActivity, ProyectosVistaUsuarioActivity::class.java)
                startActivity(intent)

            }

        }


        val aa = apiService.getClients()
        aa.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        val gson = Gson()
                        val jsonArray = JsonParser.parseString(responseBody.string()).asJsonArray
                        val jsonObjectList: List<JsonObject> = jsonArray.map { it.asJsonObject }

                        jsonObjectList.forEach { jsonObject ->
                            val name = jsonObject.get("cast")
                            println("Name: $name")
                            println(jsonObject)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle the error
            }
        })

/**
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))


         * AQUI DEBERIA DE CARGAR LOS PROYECTOS

        proyectoList = ArrayList<Proyecto>()
        proyectoList.add(Proyecto("Artículo 1", "Descripción del artículo 1", "10/12/2025"))
        proyectoList.add(Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"))
hola
        proyectoAdapter = ItemAdapter(proyectoList, R.layout.item_proyecto) { holder, proyecto, position ->
                val title: TextView = holder.findView(R.id.project_title)
                val description: TextView = holder.findView(R.id.project_description)
                val deadline: TextView = holder.findView(R.id.project_deadline)

                title.setText(proyecto.getTitle())
                description.setText(proyecto.getDescription())
                deadline.setText(proyecto.getFechaLimite())
            }
        recyclerView.adapter = proyectoAdapter


        val donacionesText = findViewById<TextView>(R.id.donaciones_text)
        donacionesText.setOnClickListener {
            setSelected(donacionesText)

            donacionList = ArrayList<Donacion>()
            donacionList.add(Donacion("ayuda", "Descripción del artículo 1", 12.2))
            donacionList.add(Donacion("pppppp2", "Descripción del artículo 2", 56.2))

            donacionAdapter = ItemAdapter(donacionList, R.layout.item_donacion) { holder, donacion, position ->
                val donante: TextView = holder.findView(R.id.donante_item)
                val nombreProyecto: TextView = holder.findView(R.id.nombre_proyecto_item)
                val monto: TextView = holder.findView(R.id.monto_item)

                donante.setText(donacion.getDonante())
                nombreProyecto.setText(donacion.getProyecto())
                monto.setText(donacion.getmonto().toString())
            }
//            recyclerView.setAdapter(donacionAdapter)
            recyclerView.adapter = donacionAdapter
        }
        val usuariosText = findViewById<TextView>(R.id.usuarios_text)
        usuariosText.setOnClickListener {
            setSelected(usuariosText)

            usuarioList = ArrayList<Usuario>()
            usuarioList.add(Usuario("pepe", "5432", "spam@pinga.com"))
            usuarioList.add(Usuario("chibolo", "99999999", "spam2@caracoles,.com"))

            usuarioAdapter = ItemAdapter(usuarioList, R.layout.item_usuario) { holder, usuario, position ->
                val nombre: TextView = holder.findView(R.id.nombre_item)
                val correo: TextView = holder.findView(R.id.telefono_item)
                val telefono: TextView = holder.findView(R.id.correo_item)

                nombre.setText(usuario.getnombre())
                correo.setText(usuario.getCorreo())
                telefono.setText(usuario.getTelefono())
            }
            recyclerView.adapter = usuarioAdapter
        }
        val estadisticasText = findViewById<TextView>(R.id.estadisticas_text)
        estadisticasText.setOnClickListener {
            setSelected(estadisticasText)
            proyectoList = ArrayList<Proyecto>()
            proyectoList.add(Proyecto("Artículo 1", "Descripción del artículo 1", "10/12/2025"))
            proyectoList.add(Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"))

            proyectoAdapter = ItemAdapter(proyectoList, R.layout.item_proyecto) { holder, proyecto, position ->
                val title: TextView = holder.findView(R.id.project_title)
                val description: TextView = holder.findView(R.id.project_description)
                val deadline: TextView = holder.findView(R.id.project_deadline)

                title.setText(proyecto.getTitle())
                description.setText(proyecto.getDescription())
                deadline.setText(proyecto.getFechaLimite())
            }
            recyclerView.adapter = proyectoAdapter;
            setSelected(estadisticasText)
        }
        val proyectosText = findViewById<TextView>(R.id.proyectos_text)
        proyectosText.setOnClickListener {
            setSelected(proyectosText)
            proyectoList = ArrayList<Proyecto>()
            proyectoList.add(Proyecto("Artículo 1", "Descripción del artículo 1", "10/12/2025"))
            proyectoList.add(Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"))

            proyectoAdapter = ItemAdapter(proyectoList, R.layout.item_proyecto) { holder, proyecto, position ->
                val title: TextView = holder.findView(R.id.project_title)
                val description: TextView = holder.findView(R.id.project_description)
                val deadline: TextView = holder.findView(R.id.project_deadline)

                title.setText(proyecto.getTitle())
                description.setText(proyecto.getDescription())
                deadline.setText(proyecto.getFechaLimite())
            }
            recyclerView.adapter = proyectoAdapter;
        }
    }
    **/
    fun setSelected(textView: TextView){
        findViewById<TextView>(R.id.donaciones_text).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.usuarios_text).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.estadisticas_text).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.proyectos_text).setTypeface(null, Typeface.NORMAL)
        textView.setTypeface(null, Typeface.BOLD)
    }

}

}