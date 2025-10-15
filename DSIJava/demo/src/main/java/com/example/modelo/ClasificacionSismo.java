// Archivo: ClasificacionSismo.java
package com.example.modelo;

public class ClasificacionSismo {
    private String nombre;
    private float kmProfundidadDesde;
    private float kmProfundidadHasta;

    public ClasificacionSismo(String nombre, float desde, float hasta) {
        this.nombre = nombre;
        this.kmProfundidadDesde = desde;
        this.kmProfundidadHasta = hasta;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Comprueba si una profundidad dada (en km) pertenece a esta clasificación.
     * @param profundidadHipocentro La profundidad del evento sísmico en km.
     * @return true si la profundidad está dentro del rango, false en caso contrario.
     */
    public boolean pertenece(float profundidadHipocentro) {
        return profundidadHipocentro >= kmProfundidadDesde && profundidadHipocentro < kmProfundidadHasta;
    }

    @Override
    public String toString() {
        return nombre;
    }
}