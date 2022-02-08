package com.example.appdelclima;

import java.io.Serializable;

public class Clima implements Serializable {
    String dia, temp, ciudad;

    public Clima(String dia, String temp) {
        this.dia = dia;
        this.temp = temp;
    }

    public Clima(String ciudad,String dia, String temp) {
        this.dia = dia;
        this.temp = temp;
        this.ciudad = ciudad;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Clima{" +
                "dia='" + dia + '\'' +
                ", temp='" + temp + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
