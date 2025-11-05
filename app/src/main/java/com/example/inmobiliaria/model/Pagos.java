package com.example.inmobiliaria.model;

import java.time.LocalDateTime;

public class Pagos {
    private int idPago;
    private int nroPago;
    private int idAlquiler;
    private LocalDateTime fecha;
    private double importe;

    public int getIdPago() { return idPago; }
    public int getNumero() { return nroPago; }
    public int getIdAlquiler() { return idAlquiler; }
    public LocalDateTime getFecha() { return fecha; }
    public double getImporte() { return importe; }

}