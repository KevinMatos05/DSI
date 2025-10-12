package com.example.modelo;

public class Sismografo {
    private int nroSismografo;
    private String descripcion;
    private EstacionSismologica estacion;

    public Sismografo(int nroSismografo, String descripcion, EstacionSismologica estacion) {
        this.nroSismografo = nroSismografo;
        this.descripcion = descripcion;
        this.estacion = estacion;
    }
    
    public boolean esDeSerieTemporal() {
        // La lógica de este método depende de un contexto que no está totalmente definido,
        // por ahora devolvemos true como marcador de posición.
        return true;
    }

    // Getters y Setters
    public int getNroSismografo() { return nroSismografo; }
    public void setNroSismografo(int nroSismografo) { this.nroSismografo = nroSismografo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EstacionSismologica getEstacion() { return estacion; }
    public void setEstacion(EstacionSismologica estacion) { this.estacion = estacion; }
}