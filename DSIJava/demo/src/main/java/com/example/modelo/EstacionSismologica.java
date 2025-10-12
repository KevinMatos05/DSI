package com.example.modelo;

public class EstacionSismologica {
    private String nombreEstacion;
    private String codigoEstacion;
    private boolean fechaActualizacionCertificacion;
    private double latitud;
    private double longitud;
    
    public EstacionSismologica(String nombreEstacion, String codigoEstacion, double latitud, double longitud) {
        this.nombreEstacion = nombreEstacion;
        this.codigoEstacion = codigoEstacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public void obtenerConfigEstacion() {
        System.out.println("Configuración obtenida para la estación: " + this.nombreEstacion);
    }
    
    // --- Getters y Setters Agregados ---

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public void setCodigoEstacion(String codigoEstacion) {
        this.codigoEstacion = codigoEstacion;
    }

    public boolean isFechaActualizacionCertificacion() {
        return fechaActualizacionCertificacion;
    }

    public void setFechaActualizacionCertificacion(boolean fechaActualizacionCertificacion) {
        this.fechaActualizacionCertificacion = fechaActualizacionCertificacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}