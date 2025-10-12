package com.example.modelo;

import java.time.LocalDateTime;

public class CambioEstado {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Estado estado;

    public CambioEstado(Estado estado, LocalDateTime fechaHoraInicio) {
        this.estado = estado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = null;
    }

    public boolean sosActual() { return this.fechaHoraFin == null; }
    public void setFechaHoraFin(LocalDateTime fechaFin) { this.fechaHoraFin = fechaFin; }
    
    // Getters y Setters
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}