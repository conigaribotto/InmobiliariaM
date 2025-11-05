package com.example.inmobiliaria.model;

import java.io.Serializable;
import java.time.LocalDateTime; // o LocalDateTime si necesitás hora

public class Alquiler implements Serializable {
    private int idAlquiler;
    private int idInmueble;
    private int idInquilino;
    private double precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;


    public int getIdAlquiler() { return idAlquiler; }
    public int getIdInmueble() { return idInmueble; }
    public int getIdInquilino() { return idInquilino; }
    public double getPrecio() { return precio; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }


}