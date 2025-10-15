package com.example.modelo;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para transferir los datos de un EventoSismico
 * a la capa de presentación de forma segura y desacoplada.
 */
public class EventoSismicoDTO {
    // Usamos un identificador para referirnos al evento original.
    // En una app real, sería un ID de la base de datos. Aquí usaremos su hash.
    private final int id; 
    
    // Atributos que la vista necesita mostrar
    private final LocalDateTime fechaHoraOcurrencia;
    private final String localizacionGeografica;
    private final String magnitud;
    private final String estadoActual;
    private final String alcance;
    private final String origen;

    public EventoSismicoDTO(int id, LocalDateTime fecha, String loc, String mag, String estado, String alcance, String origen) {
        this.id = id;
        this.fechaHoraOcurrencia = fecha;
        this.localizacionGeografica = loc;
        this.magnitud = mag;
        this.estadoActual = estado;
        this.alcance = alcance;
        this.origen = origen;
    }

    // Getters para que la vista pueda leer los datos
    public int getId() { return id; }
    public LocalDateTime getFechaHoraOcurrencia() { return fechaHoraOcurrencia; }
    public String getLocalizacionGeografica() { return localizacionGeografica; }
    public String getMagnitud() { return magnitud; }
    public String getEstadoActual() { return estadoActual; }
    public String getAlcance() { return alcance; }
    public String getOrigen() { return origen; }

    // Opcional: Sobreescribimos toString() para facilitar la visualización en listas o combos.
    @Override
    public String toString() {
        return String.format("Fecha: %s - Loc: %s - Mag: %s - Estado: %s",
            fechaHoraOcurrencia.toLocalDate(),
            localizacionGeografica,
            magnitud,
            estadoActual);
    }
}