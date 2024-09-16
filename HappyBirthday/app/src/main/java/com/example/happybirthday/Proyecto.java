package com.example.happybirthday;

import android.media.Image;

public class Proyecto {

    private String title;
    private String description;
    private String fechaLimite;
    private double recaudado;
    private double meta;
    private Image img;

    public Proyecto(String title, String description, String fechaLimite) {
        this.title = title;
        this.description = description;
        this.fechaLimite = fechaLimite;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public double getRecaudado() {
        return recaudado;
    }

    public void setRecaudado(double recaudado) {
        this.recaudado = recaudado;
    }
}
