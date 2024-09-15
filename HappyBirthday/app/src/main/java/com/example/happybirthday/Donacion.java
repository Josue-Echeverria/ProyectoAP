package com.example.happybirthday;

import android.media.Image;

public class Donacion {

    private String donante;
    private String proyecto;
    private Double monto;

    public Donacion(String title, String description, Double monto) {
        this.donante = title;
        this.proyecto = description;
        this.monto = monto;
    }

    public String getDonante() {
        return donante;
    }

    public String getProyecto() {
        return proyecto;
    }

    public Double getmonto() {
        return monto;
    }
}
