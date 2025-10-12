package com.example.modelo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestorRegistroResultado {
    private List<EventoSismico> eventosSismicos;
    private List<Estado> estados;
    private List<AlcanceSismo> alcancesSismo;
    private List<OrigenDeGeneracion> origenesGeneracion;
    private AnalistaDeSismos usuarioLogueado;

    public GestorRegistroResultado() {
        cargarDatosDePrueba();
    }

    /**
     * Busca y devuelve una lista de eventos sísmicos que están en estado "Pendiente de revisión".
     * La lista se devuelve ordenada por fecha de ocurrencia.
     * @return Una lista de objetos EventoSismico.
     */
    public List<EventoSismico> buscarEventosSismicosAutoDetectados() {
        return eventosSismicos.stream()
                .filter(EventoSismico::esAutoDetectado)
                .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
                .collect(Collectors.toList());
    }

    /**
     * Cambia el estado de un evento a "En revisión" para bloquearlo.
     * @param evento El evento a seleccionar y bloquear.
     */
    public void seleccionarEvento(EventoSismico evento) {
        if (evento == null) return;
        buscarEstadoPorNombre("En revisión").ifPresent(evento::cambiarEstado);
    }
    
    /**
     * Finaliza la revisión de un evento marcándolo como "Confirmado".
     * @param evento El evento a confirmar.
     */
    public void confirmarEvento(EventoSismico evento) {
        finalizarRevision(evento, "Confirmado");
    }

    /**
     * Finaliza la revisión de un evento marcándolo como "Rechazado".
     * @param evento El evento a rechazar.
     */
    public void rechazarEvento(EventoSismico evento) {
        finalizarRevision(evento, "Rechazado");
    }

    /**
     * Envía un evento a un experto para una revisión más detallada.
     * @param evento El evento a derivar.
     */
    public void solicitarRevisionExperto(EventoSismico evento) {
        finalizarRevision(evento, "Derivado a experto");
    }

    /**
     * Orquesta el proceso final de revisión, cambiando el estado del evento
     * y registrando la información de la acción.
     * @param evento El evento que se está finalizando.
     * @param nombreEstadoFinal El nombre del estado final ("Confirmado", "Rechazado", etc.).
     */
    private void finalizarRevision(EventoSismico evento, String nombreEstadoFinal) {
        if (evento == null) return;
        buscarEstadoPorNombre(nombreEstadoFinal).ifPresent(estadoFinal -> {
            evento.cambiarEstado(estadoFinal);
            System.out.println("--- Revisión Finalizada ---");
            System.out.println("Evento: " + evento.getLocalizacionGeografica());
            System.out.println("Acción: " + nombreEstadoFinal);
            System.out.println("Fecha y Hora: " + LocalDateTime.now());
            System.out.println("Usuario: " + usuarioLogueado.getNombre());
            System.out.println("---------------------------");
        });
    }

    /**
     * Busca un objeto Estado en la lista de estados por su nombre.
     * @param nombre El nombre del estado a buscar.
     * @return Un Optional que contiene el Estado si se encuentra.
     */
    private Optional<Estado> buscarEstadoPorNombre(String nombre) {
        return estados.stream()
                .filter(e -> e.esAmbitoEventoSismico() && nombre.equals(e.getNombre()))
                .findFirst();
    }
    
    /**
     * Devuelve la lista de todos los posibles alcances de un sismo.
     * @return una lista de objetos AlcanceSismo.
     */
    public List<AlcanceSismo> obtenerAlcancesSismo() {
        return new ArrayList<>(this.alcancesSismo); // Devolver una copia
    }

    /**
     * Devuelve la lista de todos los posibles orígenes de un sismo.
     * @return una lista de objetos OrigenDeGeneracion.
     */
    public List<OrigenDeGeneracion> obtenerOrigenesGeneracion() {
        return new ArrayList<>(this.origenesGeneracion); // Devolver una copia
    }

    /**
     * Actualiza los datos de un evento sísmico con los nuevos valores proporcionados.
     * @param evento El evento a modificar.
     * @param nuevoValorMagnitud El nuevo valor numérico de la magnitud.
     * @param nuevoAlcance El nuevo objeto AlcanceSismo.
     * @param nuevoOrigen El nuevo objeto OrigenDeGeneracion.
     */
    public void actualizarDatosEventoSismico(EventoSismico evento, float nuevoValorMagnitud, AlcanceSismo nuevoAlcance, OrigenDeGeneracion nuevoOrigen) {
        if (evento == null) return;

        MagnitudRichter nuevaMagnitud = new MagnitudRichter("Actualizada", nuevoValorMagnitud);
        evento.setMagnitud(nuevaMagnitud);
        evento.setAlcanceSismo(nuevoAlcance);
        evento.setOrigenDeGeneracion(nuevoOrigen);

        System.out.println("--- Datos del Evento Actualizados ---");
        System.out.println("Evento: " + evento.getLocalizacionGeografica());
        System.out.println("Nueva Magnitud: " + evento.getMagnitud().toString());
        System.out.println("Nuevo Alcance: " + evento.getAlcanceSismo().getNombre());
        System.out.println("Nuevo Origen: " + evento.getOrigenDeGeneracion().getNombre());
        System.out.println("-------------------------------------");
    }

    /**
     * Implementa el CU 9.2: Recorre las series, muestras y detalles de un evento,
     * y agrupa los valores por la estación sismológica que los registró.
     * @param evento El evento sísmico a procesar.
     * @return Un String formateado con la información clasificada por estación.
     */
    public String obtenerInfoMuestrasPorEstacion(EventoSismico evento) {
        if (evento == null || evento.getSeriesTemporales() == null || evento.getSeriesTemporales().isEmpty()) {
            return "El evento no tiene series temporales registradas.";
        }

        StringBuilder resultadoFinal = new StringBuilder();
        
        // Creamos un mapa para agrupar las series por estación
        Map<EstacionSismologica, List<SerieTemporal>> seriesPorEstacion = evento.getSeriesTemporales().stream()
                .collect(Collectors.groupingBy(serie -> serie.getSismografo().getEstacion()));

        // Ahora iteramos sobre el mapa agrupado
        for (Map.Entry<EstacionSismologica, List<SerieTemporal>> entry : seriesPorEstacion.entrySet()) {
            EstacionSismologica estacion = entry.getKey();
            List<SerieTemporal> seriesDeLaEstacion = entry.getValue();

            resultadoFinal.append("--- Estación Sismológica: ")
                          .append(estacion.getNombreEstacion())
                          .append(" (").append(estacion.getCodigoEstacion()).append(") ---\n");

            int contadorMuestras = 0;
            // Loop [Mientras haya muestras para la serie temporal]
            for (SerieTemporal serie : seriesDeLaEstacion) {
                for (MuestraSismica muestra : serie.getMuestras()) {
                    contadorMuestras++;
                    resultadoFinal.append("\tInstante de Tiempo #").append(contadorMuestras).append(":\n");

                    // Obtenemos los valores de cada detalle (velocidad, frecuencia, etc.)
                    for (DetalleMuestraSismica detalle : muestra.getDetallesMuestraSismica()) {
                        resultadoFinal.append("\t\t- ")
                                      .append(detalle.getTipoDato().getDenominacion()).append(": ")
                                      .append(String.format("%.2f", detalle.getValor())).append(" ")
                                      .append(detalle.getTipoDato().getValorUnidadMedida()).append("\n");
                    }
                }
            }
            resultadoFinal.append("\n");
        }

        return resultadoFinal.toString();
    }

    /**
     * Inicializa el sistema con datos de prueba para la demostración.
     */
    private void cargarDatosDePrueba() {
        this.usuarioLogueado = new AnalistaDeSismos("Kevin Matos", "1234", "kevin@email.com", 95390);

        this.estados = new ArrayList<>(Arrays.asList(
            new Estado("Revision", "Pendiente de revisión", "Evento detectado."),
            new Estado("Revision", "En revisión", "Evento siendo analizado."),
            new Estado("Revision", "Confirmado", "Evento validado."),
            new Estado("Revision", "Rechazado", "Evento descartado."),
            new Estado("Revision", "Derivado a experto", "Requiere análisis experto.")
        ));

        this.alcancesSismo = new ArrayList<>(Arrays.asList(
            new AlcanceSismo("Local", "Afecta un área pequeña."),
            new AlcanceSismo("Regional", "Afecta una región extensa.")
        ));
        
        this.origenesGeneracion = new ArrayList<>(Arrays.asList(
            new OrigenDeGeneracion("Tectónico", "Movimiento de placas."),
            new OrigenDeGeneracion("Volcánico", "Actividad volcánica.")
        ));

        EstacionSismologica estacionCba = new EstacionSismologica("Observatorio de Córdoba", "CBA-01", -31.4, -64.1);
        Sismografo sismografoCba = new Sismografo(1, "Sismógrafo Principal", estacionCba);
        EstacionSismologica estacionMdz = new EstacionSismologica("Observatorio de Mendoza", "MDZ-01", -32.8, -68.8);
        Sismografo sismografoMdz = new Sismografo(2, "Sismógrafo Secundario", estacionMdz);
        
        TipoDato tipoVelocidad = new TipoDato("Velocidad de onda", "km/s");
        TipoDato tipoFrecuencia = new TipoDato("Frecuencia de onda", "Hz");

        this.eventosSismicos = new ArrayList<>();
        Estado estadoInicial = this.estados.get(0);

        // --- Evento 1 ---
        EventoSismico evento1 = new EventoSismico(estadoInicial);
        evento1.setFechaHoraOcurrencia(LocalDateTime.now().minusHours(5));
        evento1.setLatitudEpicentro(-31.4135f);
        evento1.setLongitudEpicentro(-64.1810f);
        evento1.setMagnitud(new MagnitudRichter("Leve", 3.5f));
        evento1.setAlcanceSismo(this.alcancesSismo.get(0));
        evento1.setOrigenDeGeneracion(this.origenesGeneracion.get(0));

        SerieTemporal serie1Cba = new SerieTemporal(sismografoCba, "Normal", LocalDateTime.now().minusHours(5), 100f);
        MuestraSismica muestra1_1 = new MuestraSismica();
        muestra1_1.getDetallesMuestraSismica().add(new DetalleMuestraSismica(5.5f, tipoVelocidad));
        muestra1_1.getDetallesMuestraSismica().add(new DetalleMuestraSismica(2.1f, tipoFrecuencia));
        serie1Cba.agregarMuestra(muestra1_1);
        evento1.getSeriesTemporales().add(serie1Cba);
        this.eventosSismicos.add(evento1);

        // --- Evento 2 ---
        EventoSismico evento2 = new EventoSismico(estadoInicial);
        evento2.setFechaHoraOcurrencia(LocalDateTime.now().minusHours(2));
        evento2.setLatitudEpicentro(-32.8895f);
        evento2.setLongitudEpicentro(-68.8458f);
        evento2.setMagnitud(new MagnitudRichter("Moderado", 4.8f));
        evento2.setAlcanceSismo(this.alcancesSismo.get(1));
        evento2.setOrigenDeGeneracion(this.origenesGeneracion.get(0));

        SerieTemporal serie2Mdz = new SerieTemporal(sismografoMdz, "Alarma", LocalDateTime.now().minusHours(2), 200f);
        MuestraSismica muestra2_1 = new MuestraSismica();
        muestra2_1.getDetallesMuestraSismica().add(new DetalleMuestraSismica(6.1f, tipoVelocidad));
        serie2Mdz.agregarMuestra(muestra2_1);
        
        SerieTemporal serie2Cba = new SerieTemporal(sismografoCba, "Normal", LocalDateTime.now().minusHours(2), 100f);
        MuestraSismica muestra2_2 = new MuestraSismica();
        muestra2_2.getDetallesMuestraSismica().add(new DetalleMuestraSismica(5.9f, tipoVelocidad));
        serie2Cba.agregarMuestra(muestra2_2);

        evento2.getSeriesTemporales().add(serie2Mdz);
        evento2.getSeriesTemporales().add(serie2Cba);
        this.eventosSismicos.add(evento2);
    }
}