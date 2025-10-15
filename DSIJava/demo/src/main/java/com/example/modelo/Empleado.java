package com.example.modelo;

// --- PASO 1: CORRECCIÓN ---

public class Empleado extends Usuario {
    private String mail;
    private int legajo;

    // El constructor ahora se llama "Empleado" para coincidir con el nombre de la clase.
    public Empleado(String nombre, String contrasenia, String mail, int legajo) {
        super(nombre, contrasenia);
        this.mail = mail;
        this.legajo = legajo;
    }

    // Renombré el método para que sea más claro y no use "get" si no devuelve nada.
    public void mostrarInformacion() {
        // También actualicé el texto para que diga "empleado" en lugar de "analista".
        System.out.println("Datos del empleado: " + getNombre() + ", Legajo: " + this.legajo);
    }

    // Getters y Setters (Estos estaban bien, no necesitan cambios)
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public int getLegajo() { return legajo; }
    public void setLegajo(int legajo) { this.legajo = legajo; }
}