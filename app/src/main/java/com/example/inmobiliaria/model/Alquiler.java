package com.example.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;

public class Alquiler {

    @SerializedName("idContrato") private int idContrato;

    @SerializedName("fechaInicio")        private String fechaInicio;
    @SerializedName("fechaFinalizacion")  private String fechaFinalizacion;
    @SerializedName("montoAlquiler")      private double montoAlquiler;
    @SerializedName("estado")             private boolean estado;

    @SerializedName("idInquilino") private int idInquilino;
    @SerializedName("idInmueble")  private int idInmueble;

    @SerializedName("inquilino") private Inquilino inquilino;
    @SerializedName("inmueble")  private Inmueble inmueble;

    // === Getters ===
    public int getIdContrato() { return idContrato; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFinalizacion() { return fechaFinalizacion; }
    public double getMontoAlquiler() { return montoAlquiler; }
    public boolean isEstado() { return estado; }

    public int getIdInquilino() { return idInquilino; }
    public int getIdInmueble() { return idInmueble; }

    public Inquilino getInquilino() { return inquilino; }
    public Inmueble getInmueble() { return inmueble; }
}
