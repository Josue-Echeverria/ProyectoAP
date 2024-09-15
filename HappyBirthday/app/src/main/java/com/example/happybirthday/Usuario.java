package com.example.happybirthday;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Usuario {

    private String nombre;
    private String telefono;
    private String correo;

    public Usuario(String nombre, String telefono, String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
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
