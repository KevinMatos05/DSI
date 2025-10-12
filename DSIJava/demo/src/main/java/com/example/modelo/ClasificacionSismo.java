package com.example.modelo;

public class ClasificacionSismo {
    private String nombre;
    private float profundidadDesde; // Interpretado de "tofProfundidadDesde"
    private float profundidadHasta; // Interpretado de "tofProfundidadHasta"

    public ClasificacionSismo(String nombre, float profundidadDesde, float profundidadHasta) {
        this.nombre = nombre;
        this.profundidadDesde = profundidadDesde;
        this.profundidadHasta = profundidadHasta;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public float getProfundidadDesde() {
        return profundidadDesde;
    }

    public float getProfundidadHasta() {
        return profundidadHasta;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProfundidadDesde(float profundidadDesde) {
        this.profundidadDesde = profundidadDesde;
    }

    public void setProfundidadHasta(float profundidadHasta) {
        this.profundidadHasta = profundidadHasta;
    }
}