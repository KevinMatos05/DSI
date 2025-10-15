package com.example.modelo;

import java.time.LocalDateTime;
import java.util.*;

public class EventoSismico {
    private LocalDateTime fechaHoraOcurrencia;
    private float latitudEpicentro;
    private float longitudEpicentro;
    private float longitudHipocentro;
    private MagnitudRichter magnitud;
    private OrigenDeGeneracion origenDeGeneracion;
    private ClasificacionSismo clasificacionSismo;
    private AlcanceSismo alcanceSismo;
    private List<SerieTemporal> seriesTemporales;
    private List<CambioEstado> historialDeEstados;

    public EventoSismico(Estado estadoInicial) {
        this.historialDeEstados = new ArrayList<>();
        this.seriesTemporales = new ArrayList<>();
        crearNuevoCambioEstado(estadoInicial, LocalDateTime.now());
    }
    
    /**
     * Implementa el 'getDatosEventoSismico()' del Diagrama de Secuencia.
     * Recopila los datos que la pantalla necesita en una estructura de Mapa.
     * @return Un Map que contiene los datos principales del evento.
     */
    public Map<String, Object> getDatosEventoSismico() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fechaHoraOcurrencia", this.getFechaHoraOcurrencia());
        datos.put("latitudEpicentro", this.getLatitudEpicentro());
        datos.put("longitudEpicentro", this.getLongitudEpicentro());
        datos.put("valorMagnitud", this.getValorMagnitud());
        datos.put("alcanceSismo", this.getAlcanceSismo());
        datos.put("origenDeGeneracion", this.getOrigenDeGeneracion());
        return datos;
    }

    public Optional<Estado> obtenerEstadoActual() {
        return historialDeEstados.stream()
            .max(Comparator.comparing(CambioEstado::getFechaHoraInicio))
            .map(CambioEstado::getEstado);
    }
    
    /**
     * Corresponde a 'setEstado()' en el diagrama.
     */
    public void setEstado(Estado nuevoEstado) {
        crearNuevoCambioEstado(nuevoEstado, LocalDateTime.now());
    }

    private void crearNuevoCambioEstado(Estado estado, LocalDateTime fechaHoraInicio) {
        this.historialDeEstados.add(new CambioEstado(estado, fechaHoraInicio));
    }

    /**
     * Corresponde a 'getValorMagnitud()' en el diagrama.
     */
    public float getValorMagnitud() {
        return this.magnitud != null ? this.magnitud.getValor() : 0.0f;
    }

    // --- Getters y Setters ---
    public LocalDateTime getFechaHoraOcurrencia() { return fechaHoraOcurrencia; }
    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) { this.fechaHoraOcurrencia = fechaHoraOcurrencia; }
    public float getLatitudEpicentro() { return latitudEpicentro; }
    public void setLatitudEpicentro(float latitudEpicentro) { this.latitudEpicentro = latitudEpicentro; }
    public float getLongitudEpicentro() { return longitudEpicentro; }
    public void setLongitudEpicentro(float longitudEpicentro) { this.longitudEpicentro = longitudEpicentro; }
    public float getLongitudHipocentro() { return longitudHipocentro; }
    public void setLongitudHipocentro(float longitudHipocentro) { this.longitudHipocentro = longitudHipocentro; }
    public MagnitudRichter getMagnitud() { return magnitud; }
    public void setMagnitud(MagnitudRichter magnitud) { this.magnitud = magnitud; }
    public OrigenDeGeneracion getOrigenDeGeneracion() { return origenDeGeneracion; }
    public void setOrigenDeGeneracion(OrigenDeGeneracion origenDeGeneracion) { this.origenDeGeneracion = origenDeGeneracion; }
    public ClasificacionSismo getClasificacionSismo() { return clasificacionSismo; }
    public void setClasificacionSismo(ClasificacionSismo clasificacionSismo) { this.clasificacionSismo = clasificacionSismo; }
    public AlcanceSismo getAlcanceSismo() { return alcanceSismo; }
    public void setAlcanceSismo(AlcanceSismo alcanceSismo) { this.alcanceSismo = alcanceSismo; }
    public List<SerieTemporal> getSeriesTemporales() { return seriesTemporales; }
    public void setSeriesTemporales(List<SerieTemporal> seriesTemporales) { this.seriesTemporales = seriesTemporales; }
    public String getLocalizacionGeografica() { return String.format("%.4f, %.4f", this.latitudEpicentro, this.longitudEpicentro); }
}