package com.example.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;

public class Inquilino {

    @SerializedName("idInquilino") private int idInquilino;
    @SerializedName("nombre")      private String nombre;
    @SerializedName("apellido")    private String apellido;
    @SerializedName("dni")         private String dni;
    @SerializedName("telefono")    private String telefono;
    @SerializedName("email")       private String email;

    public int getIdInquilino() { return idInquilino; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
}
