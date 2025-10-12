package com.example.modelo;

public class MagnitudRichter {
    private String nombre;
    private float valor;

    public MagnitudRichter(String nombre, float valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public float getValor() {
        return valor;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        // Esto ayuda a que se vea bien en la TableView de JavaFX
        return String.format("%.1f (%s)", valor, nombre);
    }
}