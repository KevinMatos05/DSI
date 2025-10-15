package com.example.controladores;

import com.example.modelo.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PantallaRegistrarResultadoController {

    @FXML private TableView<EventoSismico> tablaEventos;
    @FXML private TableColumn<EventoSismico, LocalDateTime> colFecha;
    @FXML private TableColumn<EventoSismico, String> colLugar;
    @FXML private TableColumn<EventoSismico, String> colMagnitud;
    @FXML private TableColumn<EventoSismico, String> colEstado;
    @FXML private TextField txtMagnitudValor;
    @FXML private ComboBox<AlcanceSismo> cmbAlcanceSismo;
    @FXML private ComboBox<OrigenDeGeneracion> cmbOrigenGeneracion;
    @FXML private TextField txtClasificacion;
    @FXML private TextArea txtAreaMuestras;
    @FXML private Button btnConfirmar, btnRechazar, btnSolicitarRevision, btnActualizarDatos, btnGenerarSismograma, btnVerMapa;

    private GestorRegistroResultado gestor;
    private EventoSismico eventoSeleccionado;

    @FXML
    public void initialize() {
        System.out.println("--- El Controlador se ha iniciado correctamente! ---");
        this.gestor = new GestorRegistroResultado();
        configurarTabla();
        cargarCombos();
        configurarListenerTabla();
        refrescarTabla();
    }
    
    private void configurarTabla() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrencia"));
        colLugar.setCellValueFactory(new PropertyValueFactory<>("localizacionGeografica"));
        colMagnitud.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getMagnitud().toString()));
        colEstado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().obtenerEstadoActual().map(Estado::getNombre).orElse("N/A")));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        colFecha.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
    }

    private void configurarListenerTabla() {
        tablaEventos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    this.eventoSeleccionado = newSelection;
                    try {
                        if (eventoSeleccionado.obtenerEstadoActual().map(Estado::esAutoDetectado).orElse(false)) {
                            gestor.bloquearEventoSismico(this.eventoSeleccionado);
                            tablaEventos.refresh();
                        }
                        mostrarDetalles();
                    } catch (IllegalStateException e) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Acción no válida", e.getMessage());
                        refrescarTabla();
                    }
                }
            });
    }

    private void refrescarTabla() {
        List<EventoSismico> eventos = gestor.buscarEventosSismicosAutoDetectados();
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
        limpiarDetalles();
    }

    private void mostrarDetalles() {
        if (eventoSeleccionado == null) return;
        
        boolean estaEnRevision = eventoSeleccionado.obtenerEstadoActual().map(Estado::esBloqueadoEnRevision).orElse(false);
        habilitarControles(estaEnRevision);

        // Pide al gestor que calcule y asigne la clasificación.
        gestor.obtenerYAsignarClasificacion(eventoSeleccionado);
        
        Map<String, Object> datos = eventoSeleccionado.getDatosEventoSismico();
        txtMagnitudValor.setText(String.valueOf(datos.get("valorMagnitud")));
        cmbAlcanceSismo.setValue((AlcanceSismo) datos.get("alcanceSismo"));
        cmbOrigenGeneracion.setValue((OrigenDeGeneracion) datos.get("origenDeGeneracion"));

        // Muestra la clasificación obtenida.
        if (eventoSeleccionado.getClasificacionSismo() != null) {
            txtClasificacion.setText(eventoSeleccionado.getClasificacionSismo().getNombre());
        } else {
            txtClasificacion.setText("No clasificado");
        }

        String infoMuestras = gestor.obtenerInfoMuestrasPorEstacion(eventoSeleccionado);
        txtAreaMuestras.setText(infoMuestras);
    }

    private void finalizarAccion() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Acción Registrada", "La operación se completó con éxito.");
        refrescarTabla();
    }

    // --- Métodos de Acción ---

    @FXML private void tomarAccionConfirmar() { 
        if (eventoSeleccionado != null) {
            gestor.confirmarEvento(eventoSeleccionado);
            finalizarAccion(); 
        }
    }
    @FXML private void tomarAccionRechazar() { 
        if (eventoSeleccionado != null) {
            gestor.rechazarEvento(eventoSeleccionado);
            finalizarAccion(); 
        }
    }
    @FXML private void tomarAccionSolicitarRevision() { 
        if (eventoSeleccionado != null) {
            gestor.solicitarRevisionExperto(eventoSeleccionado);
            finalizarAccion(); 
        }
    }
    @FXML private void actualizarDatosEvento() {
        if (eventoSeleccionado == null) return;
        try {
            float nuevoValorMagnitud = Float.parseFloat(txtMagnitudValor.getText());
            AlcanceSismo nuevoAlcance = cmbAlcanceSismo.getValue();
            OrigenDeGeneracion nuevoOrigen = cmbOrigenGeneracion.getValue();

            if (nuevoAlcance == null || nuevoOrigen == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Debe seleccionar un Alcance y un Origen.");
                return;
            }
            gestor.actualizarDatosEventoSismico(eventoSeleccionado, nuevoValorMagnitud, nuevoAlcance, nuevoOrigen);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Los datos del evento han sido actualizados.");
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El valor de la magnitud debe ser un número.");
        } catch (IllegalStateException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Acción no válida", e.getMessage());
        }
    }

    // --- Métodos Auxiliares ---

    private void cargarCombos() {
        cmbAlcanceSismo.setItems(FXCollections.observableArrayList(gestor.obtenerAlcancesSismo()));
        cmbOrigenGeneracion.setItems(FXCollections.observableArrayList(gestor.obtenerOrigenesGeneracion()));
    }

    private void limpiarDetalles() {
        txtMagnitudValor.clear();
        cmbAlcanceSismo.getSelectionModel().clearSelection();
        cmbOrigenGeneracion.getSelectionModel().clearSelection();
        txtClasificacion.clear();
        txtAreaMuestras.clear();
        habilitarControles(false);
    }

    private void habilitarControles(boolean habilitar) {
        txtMagnitudValor.setDisable(!habilitar);
        cmbAlcanceSismo.setDisable(!habilitar);
        cmbOrigenGeneracion.setDisable(!habilitar);
        txtClasificacion.setDisable(true); // Este campo siempre es de solo lectura.
        btnActualizarDatos.setDisable(!habilitar);
        btnGenerarSismograma.setDisable(!habilitar);
        btnVerMapa.setDisable(!habilitar);
        btnConfirmar.setDisable(!habilitar);
        btnRechazar.setDisable(!habilitar);
        btnSolicitarRevision.setDisable(!habilitar);
    }
    
    @FXML private void generarSismograma() { 
        mostrarAlerta(Alert.AlertType.INFORMATION, "Funcionalidad no implementada", "Aquí se llamaría al CU 'Generar Sismograma'.");
    }
    
    @FXML private void verMapa() { 
        mostrarAlerta(Alert.AlertType.INFORMATION, "Funcionalidad no implementada", "Aquí se mostraría el mapa del evento sísmico.");
    }
    
    private void mostrarAlerta(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}