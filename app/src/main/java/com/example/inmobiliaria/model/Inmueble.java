package com.example.inmobiliaria.model;

public class Inmueble {
    private int idInmueble;
    private String direccion;
    private String uso;          // en la API suele venir como "Residencial/Comercial"
    private String tipo;         // "Casa/Departamento" (lo usamos como "titulo" en la UI)
    private int ambientes;
    private double superficie;
    private Double latitud;
    private Double longitud;
    private Double valor;
    private String imagen;       // e.g. "uploads\\file.jpg"
    private boolean disponible;  // en la API: "disponible": true/false
    private int idPropietario;   // dueños/relación
    private Propietario duenio;  // opcional, por si viene expandido
    private Boolean tieneContratoVigente;

    // ====== Getters/Setters base ======
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

    // ====== Helpers para TU UI (compatibilidad con tu Adapter/ViewModel) ======

    // Tu adapter usa "getTitulo()" para mostrar: devolvemos "tipo" como título.
    public String getTitulo() {
        return tipo == null ? "" : tipo;
    }

    // Tu adapter usa "isHabilitado()" (nosotros mapeamos a 'disponible')
    public boolean isHabilitado() {
        return disponible;
    }

    // Para que funcione tanto setHabilitado como setDisponible
    public void setHabilitado(boolean habilitado) {
        this.disponible = habilitado;
    }

    // Tu adapter usa "getFotoUrl()". Normalizamos barras invertidas.
    // (Si la API devuelve rutas relativas, Glide podría necesitar que las transformes a absolutas.)
    public String getFotoUrl() {
        if (imagen == null) return "";
        // normaliza backslashes -> slashes
        String clean = imagen.replace("\\", "/");
        return clean;
    }

    // Por si en otros lados esperás "getIdPropietario()" (ya definido arriba)
    // y métodos de compatibilidad que pudieras haber usado antes:
    public String getDireccionSafe() {
        return direccion == null ? "" : direccion;
    }
}
