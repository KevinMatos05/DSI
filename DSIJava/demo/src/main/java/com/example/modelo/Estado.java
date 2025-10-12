package com.example.modelo;

public class Estado {
    private String ambito;
    private String nombre;
    private String descripcion;
    
    public Estado(String ambito, String nombre, String descripcion) {
        this.ambito = ambito;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public boolean esAmbitoEventoSismico() { return "Revision".equals(this.ambito); }
    public boolean esBloqueadoEnRevision() { return "En revisión".equals(this.nombre); }
    public boolean esRechazado() { return "Rechazado".equals(this.nombre); }
    public boolean esAutoDetectado() { return "Pendiente de revisión".equals(this.nombre); }
    
    // Getters y Setters
    public String getAmbito() { return ambito; }
    public void setAmbito(String ambito) { this.ambito = ambito; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}