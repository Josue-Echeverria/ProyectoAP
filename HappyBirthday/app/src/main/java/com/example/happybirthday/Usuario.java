package com.example.happybirthday;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Usuario {

    private String nombre;
    private String telefono;
    private String correo;
    private int activo;

    public Usuario(String nombre, String telefono, String correo, int activo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.activo = activo;
    }

    public String getnombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String toJson(){
        return  "{name:"+this.nombre+", correo:"+this.correo+", telefono:"+this.telefono+"}";
    }

    public int getActivo() {
        return activo;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @NotNull
    public String string() {
        return null;
    }


}
