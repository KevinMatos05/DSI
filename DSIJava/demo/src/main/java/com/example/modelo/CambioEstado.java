package com.example.modelo;

import java.time.LocalDateTime;

public class CambioEstado {
    private LocalDateTime fechaHoraInicio;
    // --- CAMBIO: Eliminamos fechaHoraFin ---
    // Ya no es necesario, el estado actual es simplemente el último en el tiempo.
    // private LocalDateTime fechaHoraFin; 
    private Estado estado;

    public CambioEstado(Estado estado, LocalDateTime fechaHoraInicio) {
        this.estado = estado;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    // --- CAMBIO: Eliminamos el método sosActual() ---
    // La responsabilidad de encontrar el estado actual ahora recae en la clase EventoSismico,
    // que es la que contiene la lista completa (la serie temporal).
    // public boolean sosActual() { return this.fechaHoraFin == null; }

    // --- CAMBIO: Eliminamos el setFechaHoraFin() ---
    // public void setFechaHoraFin(LocalDateTime fechaFin) { this.fechaHoraFin = fechaFin; }
    
    // Getters y Setters (sin cambios, excepto los eliminados)
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}