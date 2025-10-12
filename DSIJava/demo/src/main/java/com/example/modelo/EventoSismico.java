package com.example.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap; // Importación necesaria para Map
import java.util.List;
import java.util.Map;   // Importación necesaria para Map
import java.util.Optional;

public class EventoSismico {
    // ... (atributos y constructor sin cambios) ...
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
    
    // --- MÉTODO NUEVO QUE SIGUE EL DIAGRAMA ---

    /**
     * Recopila los datos principales del evento sísmico en una sola estructura.
     * Este método corresponde al getDatosEventoSismico() del diagrama de secuencia.
     * @return Un Map que contiene los datos del evento.
     */
    public Map<String, Object> getDatosEventoSismico() {
        Map<String, Object> datos = new HashMap<>();
        // El método llama a sus propios getters internos para recopilar la información
        datos.put("fechaHoraOcurrencia", this.getFechaHoraOcurrencia());
        datos.put("latitudEpicentro", this.getLatitudEpicentro());
        datos.put("longitudEpicentro", this.getLongitudEpicentro());
        datos.put("longitudHipocentro", this.getLongitudHipocentro());
        datos.put("valorMagnitud", this.getMagnitud().getValor()); // getValorMagnitud()
        // También agregamos los otros datos que la pantalla necesita
        datos.put("magnitudObjeto", this.getMagnitud());
        datos.put("alcanceSismo", this.getAlcanceSismo());
        datos.put("origenDeGeneracion", this.getOrigenDeGeneracion());
        return datos;
    }

    // --- Resto de métodos y getters/setters sin cambios ---
    
    public Optional<Estado> getEstadoActual() {
        return buscarActualCE().map(CambioEstado::getEstado);
    }

    public void cambiarEstado(Estado nuevoEstado) {
        LocalDateTime fechaHoraDelCambio = LocalDateTime.now();
        buscarActualCE().ifPresent(actual -> actual.setFechaHoraFin(fechaHoraDelCambio));
        crearNuevoCambioEstado(nuevoEstado, fechaHoraDelCambio);
    }

    private Optional<CambioEstado> buscarActualCE() {
        return historialDeEstados.stream().filter(CambioEstado::sosActual).findFirst();
    }

    private void crearNuevoCambioEstado(Estado estado, LocalDateTime fechaHoraInicio) {
        this.historialDeEstados.add(new CambioEstado(estado, fechaHoraInicio));
    }

    public boolean esAutoDetectado() {
        return getEstadoActual().map(Estado::esAutoDetectado).orElse(false);
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
    public List<CambioEstado> getHistorialDeEstados() { return historialDeEstados; }
    public void setHistorialDeEstados(List<CambioEstado> historialDeEstados) { this.historialDeEstados = historialDeEstados; }
    public String getLocalizacionGeografica() {
        return String.format("%.4f, %.4f", this.latitudEpicentro, this.longitudEpicentro);
    }
}