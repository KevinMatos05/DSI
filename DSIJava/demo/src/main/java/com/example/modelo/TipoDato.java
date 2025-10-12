package com.example.modelo;

public class TipoDato {
    private String denominacion;
    private String valorUnidadMedida;
    
    public TipoDato(String denominacion, String valorUnidadMedida) {
        this.denominacion = denominacion;
        this.valorUnidadMedida = valorUnidadMedida;
    }

    // Getters y Setters
    public String getDenominacion() { return denominacion; }
    public void setDenominacion(String denominacion) { this.denominacion = denominacion; }
    public String getValorUnidadMedida() { return valorUnidadMedida; }
    public void setValorUnidadMedida(String valorUnidadMedida) { this.valorUnidadMedida = valorUnidadMedida; }
}