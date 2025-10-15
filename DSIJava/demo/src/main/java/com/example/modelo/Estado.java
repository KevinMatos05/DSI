package com.example.modelo;

public class Estado {
    // Nombres oficiales de los estados para evitar errores de tipeo
    public static final String PENDIENTE_REVISION = "Pendiente de revision";
    public static final String EN_REVISION = "En revisión";
    public static final String CONFIRMADO = "Confirmado";
    public static final String RECHAZADO = "Rechazado";
    public static final String DERIVADO_EXPERTO = "Derivado a experto";

    private String ambito;
    private String nombre;
    private String descripcion;

    public Estado(String ambito, String nombre, String descripcion) {
        this.ambito = ambito;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- MÉTODOS ALINEADOS CON EL DIAGRAMA DE CLASES ---
    
    /**
     * Corresponde a 'esAutoDetectado()' en el diagrama.
     */
    public boolean esAutoDetectado() {
        return PENDIENTE_REVISION.equalsIgnoreCase(this.nombre);
    }

    /**
     * Corresponde a 'esBloqueadoEnRevision()' en el diagrama.
     */
    public boolean esBloqueadoEnRevision() {
        return EN_REVISION.equalsIgnoreCase(this.nombre);
    }

    public boolean esAmbitoEventoSismico() {
        return "Revision".equalsIgnoreCase(this.ambito);
    }

    // --- Getters y Setters ---
    public String getAmbito() { return ambito; }
    public void setAmbito(String ambito) { this.ambito = ambito; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}