package com.example.appdelclima;

import java.io.Serializable;

public class Ciudad implements Serializable {
    String nombre;
    Clima clima;

    public Ciudad(String nombre) {
        this.nombre = nombre;
    }

    public Ciudad(String nombre, Clima clima) {
        this.nombre = nombre;
        this.clima = clima;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Clima getClima() {
        return clima;
    }

    public void setClima(Clima clima) {
        this.clima = clima;
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "nombre='" + nombre + '\'' +
                ", clima=" + clima +
                '}';
    }
}
