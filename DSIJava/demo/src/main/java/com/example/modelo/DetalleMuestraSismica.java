package com.example.modelo;

public class DetalleMuestraSismica {
    private float valor;
    private TipoDato tipoDato;

    public DetalleMuestraSismica(float valor, TipoDato tipoDato) {
        this.valor = valor;
        this.tipoDato = tipoDato;
    }

    // Getters y Setters
    public float getValor() { return valor; }
    public void setValor(float valor) { this.valor = valor; }
    public TipoDato getTipoDato() { return tipoDato; }
    public void setTipoDato(TipoDato tipoDato) { this.tipoDato = tipoDato; }
}