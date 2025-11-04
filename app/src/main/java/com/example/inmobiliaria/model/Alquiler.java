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

    }