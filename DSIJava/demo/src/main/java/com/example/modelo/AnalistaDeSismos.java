package com.example.modelo;

public class AnalistaDeSismos extends Usuario {
    private String mail;
    private int legajo;

    public AnalistaDeSismos(String nombre, String contrasenia, String mail, int legajo) {
        super(nombre, contrasenia);
        this.mail = mail;
        this.legajo = legajo;
    }

    public void getAnalistaSismos() {
        System.out.println("Datos del analista: " + getNombre());
    }

    // Getters y Setters
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public int getLegajo() { return legajo; }
    public void setLegajo(int legajo) { this.legajo = legajo; }
}