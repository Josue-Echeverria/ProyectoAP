package com.example.happybirthday;

import android.media.Image;

public class Proyecto {

    private String title;
    private String description;
    private String fechaLimite;
    private String recaudado;
    private double meta;
    private String img;

    public Proyecto(String title, String description, String fechaLimite) {
        this.title = title;
        this.description = description;
        this.fechaLimite = fechaLimite;
    }

    public Proyecto(String title, String description, String fechaLimite, String recaudado) {
        this.title = title;
        this.description = description;
        this.fechaLimite = fechaLimite;
        this.recaudado = recaudado;
    }

    public Proyecto(String title, String description, String fechaLimite, String recaudado, String imagen) {
        this.title = title;
        this.description = description;
        this.fechaLimite = fechaLimite;
        this.recaudado = recaudado;
        this.img = imagen;
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

    public String getRecaudado() {
        return recaudado;
    }

    public String getImg() {
        return img;
    }
}
