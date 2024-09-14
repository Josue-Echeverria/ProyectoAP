package com.example.happybirthday


//import android.R

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var proyectoAdapter: ProyectoAdapter
    private lateinit var proyectoList: MutableList<Proyecto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_screen1)

        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))


        // Crear y cargar la lista de artículos
        proyectoList = ArrayList<Proyecto>()
        proyectoList.add(Proyecto("Artículo 1", "Descripción del artículo 1", "10/12/2025"))
        proyectoList.add(Proyecto("Artículo 2", "Descripción del artículo 2", "10/12/2025"))


        // Agrega más artículos aquí...
        proyectoAdapter = ProyectoAdapter(proyectoList)
        recyclerView.setAdapter(proyectoAdapter)

        val donacionesText = findViewById<TextView>(R.id.donaciones_text)
        donacionesText.setOnClickListener {
            setSelected(donacionesText)
        }
        val usuariosText = findViewById<TextView>(R.id.usuarios_text)
        usuariosText.setOnClickListener {
            setSelected(usuariosText)
        }
        val estadisticasText = findViewById<TextView>(R.id.estadisticas_text)
        estadisticasText.setOnClickListener {
            setSelected(estadisticasText)
        }
        val proyectosText = findViewById<TextView>(R.id.proyectos_text)
        proyectosText.setOnClickListener {
            setSelected(proyectosText)
        }

    }

    fun setSelected(textView: TextView){
        findViewById<TextView>(R.id.donaciones_text).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.usuarios_text).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.estadisticas_text).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.proyectos_text).setTypeface(null, Typeface.NORMAL)
        textView.setTypeface(null, Typeface.BOLD)
    }
}