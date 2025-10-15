package com.example.modelo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestorRegistroResultado {
    private List<EventoSismico> eventosSismicos;
    private List<Estado> estados;
    private List<AlcanceSismo> alcancesSismo;
    private List<OrigenDeGeneracion> origenesGeneracion;
    private Empleado usuarioLogueado;
    
    // Atributo para las clasificaciones
    private List<ClasificacionSismo> clasificacionesSismo;

    public GestorRegistroResultado() {
        cargarDatosDePrueba();
    }
    
    /**
     * Busca y asigna la clasificación a un evento sísmico basado en su profundidad.
     * @param evento El evento al que se le asignará su clasificación.
     */
    public void obtenerYAsignarClasificacion(EventoSismico evento) {
        if (evento == null) return;

        float profundidad = evento.getLongitudHipocentro();

        for (ClasificacionSismo clasificacion : this.clasificacionesSismo) {
            if (clasificacion.pertenece(profundidad)) {
                evento.setClasificacionSismo(clasificacion);
                return; 
            }
        }
    }

    public List<EventoSismico> buscarEventosSismicosAutoDetectados() {
        return eventosSismicos.stream()
            .filter(evento -> evento.obtenerEstadoActual().map(Estado::esAutoDetectado).orElse(false))
            .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
            .collect(Collectors.toList());
    }

    public void bloquearEventoSismico(EventoSismico evento) {
        if (evento == null) return;
        if (!evento.obtenerEstadoActual().map(Estado::esAutoDetectado).orElse(false)) {
            throw new IllegalStateException("El evento ya fue tomado por otro analista o ya no está pendiente.");
        }
        buscarEstadoPorNombre(Estado.EN_REVISION).ifPresent(evento::setEstado);
        System.out.println("Evento bloqueado para revisión: " + evento.getLocalizacionGeografica());
    }

    public void actualizarDatosEventoSismico(EventoSismico evento, float nuevoValorMagnitud, AlcanceSismo nuevoAlcance, OrigenDeGeneracion nuevoOrigen) {
        if (evento == null) return;
        if (!evento.obtenerEstadoActual().map(Estado::esBloqueadoEnRevision).orElse(false)) {
            throw new IllegalStateException("La acción no es válida: El evento debe estar 'En revisión' para ser modificado.");
        }
        evento.setMagnitud(new MagnitudRichter("Actualizada", nuevoValorMagnitud));
        evento.setAlcanceSismo(nuevoAlcance);
        evento.setOrigenDeGeneracion(nuevoOrigen);
    }

    public void confirmarEvento(EventoSismico evento) { finalizarRevision(evento, Estado.CONFIRMADO); }
    public void rechazarEvento(EventoSismico evento) { finalizarRevision(evento, Estado.RECHAZADO); }
    public void solicitarRevisionExperto(EventoSismico evento) { finalizarRevision(evento, Estado.DERIVADO_EXPERTO); }

    private void finalizarRevision(EventoSismico evento, String nombreEstadoFinal) {
        if (evento == null) return;
        if (!evento.obtenerEstadoActual().map(Estado::esBloqueadoEnRevision).orElse(false)) {
            throw new IllegalStateException("La acción no es válida: El evento debe estar 'En revisión' para ser finalizado.");
        }
        buscarEstadoPorNombre(nombreEstadoFinal).ifPresent(estadoFinal -> {
            evento.setEstado(estadoFinal);
            System.out.println("--- Revisión Finalizada ---");
            System.out.println("Evento: " + evento.getLocalizacionGeografica());
            System.out.println("Acción: " + estadoFinal.getNombre());
            System.out.println("Usuario: " + usuarioLogueado.getNombre());
            System.out.println("---------------------------");
        });
    }

    private Optional<Estado> buscarEstadoPorNombre(String nombre) {
        return estados.stream()
            .filter(e -> e.esAmbitoEventoSismico() && nombre.equalsIgnoreCase(e.getNombre()))
            .findFirst();
    }
    
    public List<AlcanceSismo> obtenerAlcancesSismo() { return new ArrayList<>(this.alcancesSismo); }
    public List<OrigenDeGeneracion> obtenerOrigenesGeneracion() { return new ArrayList<>(this.origenesGeneracion); }
    
    public String obtenerInfoMuestrasPorEstacion(EventoSismico evento) {
        if (evento == null || evento.getSeriesTemporales() == null || evento.getSeriesTemporales().isEmpty()) {
            return "El evento no tiene series temporales registradas para mostrar.";
        }
        StringBuilder resultado = new StringBuilder();
        Map<EstacionSismologica, List<SerieTemporal>> seriesPorEstacion = evento.getSeriesTemporales().stream()
                .collect(Collectors.groupingBy(serie -> serie.getSismografo().getEstacion()));
        for (Map.Entry<EstacionSismologica, List<SerieTemporal>> entry : seriesPorEstacion.entrySet()) {
            EstacionSismologica estacion = entry.getKey();
            resultado.append("--- Estación Sismológica: ").append(estacion.getNombreEstacion()).append(" ---\n");
            int contadorMuestras = 0;
            for (SerieTemporal serie : entry.getValue()) {
                if (serie.getMuestras() == null) continue;
                for (MuestraSismica muestra : serie.getMuestras()) {
                    contadorMuestras++;
                    resultado.append("\tInstante de Tiempo #").append(contadorMuestras).append(":\n");
                    if (muestra.getDetallesMuestraSismica() == null) continue;
                    for (DetalleMuestraSismica detalle : muestra.getDetallesMuestraSismica()) {
                        resultado.append("\t\t- ")
                                 .append(detalle.getTipoDato().getDenominacion()).append(": ")
                                 .append(String.format(Locale.US, "%.2f", detalle.getValor())).append(" ")
                                 .append(detalle.getTipoDato().getValorUnidadMedida()).append("\n");
                    }
                }
            }
            resultado.append("\n");
        }
        return resultado.toString();
    }

    /**
     * Carga los datos iniciales del sistema.
     */
    private void cargarDatosDePrueba() {
        this.usuarioLogueado = new Empleado("Kevin Matos", "1234", "kevin@email.com", 95390);

        this.estados = new ArrayList<>(Arrays.asList(
            new Estado("Revision", Estado.PENDIENTE_REVISION, "Evento detectado."),
            new Estado("Revision", Estado.EN_REVISION, "Evento siendo analizado."),
            new Estado("Revision", Estado.CONFIRMADO, "Evento validado."),
            new Estado("Revision", Estado.RECHAZADO, "Evento descartado."),
            new Estado("Revision", Estado.DERIVADO_EXPERTO, "Requiere análisis experto.")
        ));

        this.alcancesSismo = new ArrayList<>(Arrays.asList(
            new AlcanceSismo("Local", "Afecta un área pequeña."), 
            new AlcanceSismo("Regional", "Afecta una región extensa."),
            new AlcanceSismo("Global", "Afecta a todo el planeta.")
        ));
        
        this.origenesGeneracion = new ArrayList<>(Arrays.asList(
            new OrigenDeGeneracion("Tectónico", "Movimiento de placas."), 
            new OrigenDeGeneracion("Volcánico", "Actividad volcánica."),
            new OrigenDeGeneracion("Inducido", "Actividad humana."),
            new OrigenDeGeneracion("Intraplaca", "Deformación interna de placas.")
        ));

        this.clasificacionesSismo = new ArrayList<>(Arrays.asList(
            new ClasificacionSismo("Superficial", 0, 70),
            new ClasificacionSismo("Intermedio", 70, 300),
            new ClasificacionSismo("Profundo", 300, Float.MAX_VALUE)
        ));
        
        this.eventosSismicos = new ArrayList<>();
        Estado estadoInicial = buscarEstadoPorNombre(Estado.PENDIENTE_REVISION)
                                .orElseThrow(() -> new IllegalStateException("El estado 'Pendiente de revisión' no pudo ser encontrado."));
        
        EstacionSismologica estacionCba = new EstacionSismologica("Observatorio de Córdoba", "CBA-01", -31.4, -64.1);
        Sismografo sismografoCba = new Sismografo(1, "Sismógrafo Principal", estacionCba);
        TipoDato tipoVelocidad = new TipoDato("Velocidad de onda", "km/s");
        TipoDato tipoFrecuencia = new TipoDato("Frecuencia de onda", "Hz");

        // Creación de evento 1 con profundidad
        EventoSismico evento1 = new EventoSismico(estadoInicial);
        evento1.setFechaHoraOcurrencia(LocalDateTime.of(2025, 10, 14, 21, 30, 16));
        evento1.setLatitudEpicentro(-31.4135f);
        evento1.setLongitudEpicentro(-64.1810f);
        evento1.setLongitudHipocentro(35.0f); // Profundidad en km para que sea "Superficial"
        evento1.setMagnitud(new MagnitudRichter("Leve", 3.5f));
        evento1.setAlcanceSismo(this.alcancesSismo.get(0));
        evento1.setOrigenDeGeneracion(this.origenesGeneracion.get(0));
        
        SerieTemporal serie1 = new SerieTemporal(sismografoCba, "Normal", evento1.getFechaHoraOcurrencia(), 100f);
        MuestraSismica muestra1 = new MuestraSismica();
        muestra1.getDetallesMuestraSismica().add(new DetalleMuestraSismica(5.5f, tipoVelocidad));
        serie1.agregarMuestra(muestra1);
        evento1.getSeriesTemporales().add(serie1);
        this.eventosSismicos.add(evento1);

        // Creación de evento 2 con profundidad
        EventoSismico evento2 = new EventoSismico(estadoInicial);
        evento2.setFechaHoraOcurrencia(LocalDateTime.of(2025, 10, 15, 0, 30, 16));
        evento2.setLatitudEpicentro(-32.8895f);
        evento2.setLongitudEpicentro(-68.8458f);
        evento2.setLongitudHipocentro(150.0f); // Profundidad en km para que sea "Intermedio"
        evento2.setMagnitud(new MagnitudRichter("Moderado", 4.8f));
        evento2.setAlcanceSismo(this.alcancesSismo.get(1));
        evento2.setOrigenDeGeneracion(this.origenesGeneracion.get(0));
        this.eventosSismicos.add(evento2);
    }
}