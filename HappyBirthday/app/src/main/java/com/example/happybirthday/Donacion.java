package com.example.happybirthday;

import android.media.Image;

public class Donacion {

    private String donante;
    private String proyecto;
    private Double monto;
    private String fecha;

    public Donacion(String title, String donante, Double monto, String fecha) {
        this.donante = donante;
        this.proyecto = title;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getDonante() {
        return donante;
    }

    public String getProyecto() {
        return proyecto;
    }


    public String getFecha() {
        return fecha;
    }

    public Double getMonto() {
        return monto;
    }
}
