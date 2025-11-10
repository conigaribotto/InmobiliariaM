package com.example.inmobiliaria.model;

public class Inmueble {
    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private double superficie;
    private Double latitud;
    private Double longitud;
    private Double valor;
    private String imagen;
    private boolean disponible;
    private int idPropietario;
    private Propietario duenio;
    private Boolean tieneContratoVigente;

    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }

    public double getSuperficie() { return superficie; }
    public void setSuperficie(double superficie) { this.superficie = superficie; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }

    public Propietario getDuenio() { return duenio; }
    public void setDuenio(Propietario duenio) { this.duenio = duenio; }

    public Boolean getTieneContratoVigente() { return tieneContratoVigente; }
    public void setTieneContratoVigente(Boolean tieneContratoVigente) { this.tieneContratoVigente = tieneContratoVigente; }

    public String getTitulo() {
        return tipo == null ? "" : tipo;
    }

    public boolean isHabilitado() {
        return disponible;
    }

    public void setHabilitado(boolean habilitado) {
        this.disponible = habilitado;
    }

    public String getFotoUrl() {
        if (imagen == null) return "";
        // normaliza backslashes -> slashes
        String clean = imagen.replace("\\", "/");
        return clean;
    }

    public String getDireccionSafe() {
        return direccion == null ? "" : direccion;
    }
}
