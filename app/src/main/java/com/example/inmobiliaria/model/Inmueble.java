package com.example.inmobiliaria.model;

import java.io.Serializable;

public class Inmueble {
    private int idInmueble;
    private String titulo;
    private String descripcion;
    private String direccion;
    private boolean habilitado;
    private int propietarioId;
    private String fotoUrl;

    public Inmueble() {}

    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }

    public int getPropietarioId() { return propietarioId; }
    public void setPropietarioId(int propietarioId) { this.propietarioId = propietarioId; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}
