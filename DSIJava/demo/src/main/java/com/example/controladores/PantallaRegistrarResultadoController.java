package com.example.controladores;

import com.example.modelo.GestorRegistroResultado;
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
import java.util.Map; // Importación necesaria para Map

public class PantallaRegistrarResultadoController {

    // ... (declaraciones @FXML sin cambios) ...
    @FXML private TableView<EventoSismico> tablaEventos;
    @FXML private TableColumn<EventoSismico, LocalDateTime> colFecha;
    @FXML private TableColumn<EventoSismico, String> colLugar;
    @FXML private TableColumn<EventoSismico, MagnitudRichter> colMagnitud;
    @FXML private TableColumn<EventoSismico, String> colEstado;
    
    @FXML private TextField txtMagnitudValor; 
    @FXML private ComboBox<AlcanceSismo> cmbAlcanceSismo;
    @FXML private ComboBox<OrigenDeGeneracion> cmbOrigenGeneracion;
    @FXML private TextArea txtAreaMuestras;

    @FXML private Button btnConfirmar, btnRechazar, btnSolicitarRevision, btnActualizarDatos, btnGenerarSismograma, btnVerMapa;

    private GestorRegistroResultado gestor;
    private EventoSismico eventoSeleccionado;

    // --- MÉTODO mostrarDetalles() MODIFICADO ---

    private void mostrarDetalles() {
        // El controlador llama al único método que pide el diagrama
        Map<String, Object> datosDelEvento = eventoSeleccionado.getDatosEventoSismico();

        // Ahora usamos los datos del Map para rellenar la interfaz
        txtMagnitudValor.setText(String.valueOf(datosDelEvento.get("valorMagnitud")));
        cmbAlcanceSismo.getSelectionModel().select((AlcanceSismo) datosDelEvento.get("alcanceSismo"));
        cmbOrigenGeneracion.getSelectionModel().select((OrigenDeGeneracion) datosDelEvento.get("origenDeGeneracion"));
        
        // El resto de la lógica permanece igual
        String infoMuestras = gestor.obtenerInfoMuestrasPorEstacion(eventoSeleccionado);
        txtAreaMuestras.setText(infoMuestras);

        habilitarControles(true);
    }

    // --- Resto de la clase sin cambios ---

    @FXML
    public void initialize() {
        this.gestor = new GestorRegistroResultado();
        configurarTabla();
        cargarCombos();

        tablaEventos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    this.eventoSeleccionado = newSelection;
                    gestor.seleccionarEvento(this.eventoSeleccionado);
                    mostrarDetalles();
                    tablaEventos.refresh();
                } else {
                    limpiarDetalles();
                }
            });

        refrescarTabla();
    }
    
    private void configurarTabla() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHoraOcurrencia"));
        colLugar.setCellValueFactory(new PropertyValueFactory<>("localizacionGeografica"));
        colMagnitud.setCellValueFactory(new PropertyValueFactory<>("magnitud"));
        colEstado.setCellValueFactory(cellData -> {
            String estadoNombre = cellData.getValue().getEstadoActual()
                                        .map(Estado::getNombre)
                                        .orElse("N/A");
            return new SimpleStringProperty(estadoNombre);
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        colFecha.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
    }

    private void cargarCombos() {
        List<AlcanceSismo> alcances = gestor.obtenerAlcancesSismo();
        cmbAlcanceSismo.setItems(FXCollections.observableArrayList(alcances));
        cmbAlcanceSismo.setConverter(new StringConverter<>() {
            @Override public String toString(AlcanceSismo object) { return object != null ? object.getNombre() : ""; }
            @Override public AlcanceSismo fromString(String string) { return null; }
        });

        List<OrigenDeGeneracion> origenes = gestor.obtenerOrigenesGeneracion();
        cmbOrigenGeneracion.setItems(FXCollections.observableArrayList(origenes));
        cmbOrigenGeneracion.setConverter(new StringConverter<>() {
            @Override public String toString(OrigenDeGeneracion object) { return object != null ? object.getNombre() : ""; }
            @Override public OrigenDeGeneracion fromString(String string) { return null; }
        });
    }
    
    private void refrescarTabla() {
        var eventos = gestor.buscarEventosSismicosAutoDetectados();
        tablaEventos.setItems(FXCollections.observableArrayList(eventos));
        limpiarDetalles();
    }
    
    private void limpiarDetalles() {
        txtMagnitudValor.clear();
        cmbAlcanceSismo.getSelectionModel().clearSelection();
        cmbOrigenGeneracion.getSelectionModel().clearSelection();
        txtAreaMuestras.clear();
        habilitarControles(false);
    }

    private void habilitarControles(boolean habilitar) {
        txtMagnitudValor.setDisable(!habilitar);
        cmbAlcanceSismo.setDisable(!habilitar);
        cmbOrigenGeneracion.setDisable(!habilitar);
        btnActualizarDatos.setDisable(!habilitar);
        btnGenerarSismograma.setDisable(!habilitar);
        btnVerMapa.setDisable(!habilitar);
        btnConfirmar.setDisable(!habilitar);
        btnRechazar.setDisable(!habilitar);
        btnSolicitarRevision.setDisable(!habilitar);
    }
    
    @FXML 
    private void actualizarDatosEvento() {
        try {
            float nuevoValorMagnitud = Float.parseFloat(txtMagnitudValor.getText());
            AlcanceSismo nuevoAlcance = cmbAlcanceSismo.getSelectionModel().getSelectedItem();
            OrigenDeGeneracion nuevoOrigen = cmbOrigenGeneracion.getSelectionModel().getSelectedItem();

            if (nuevoAlcance == null || nuevoOrigen == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Seleccione un Alcance y un Origen.");
                return;
            }
            gestor.actualizarDatosEventoSismico(eventoSeleccionado, nuevoValorMagnitud, nuevoAlcance, nuevoOrigen);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Datos actualizados.");
            tablaEventos.refresh();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "La Magnitud debe ser un número.");
        }
    }
    
    @FXML 
    private void generarSismograma() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Generar Sismograma", "Llamando al caso de uso 'Generar Sismograma'...");
    }
    
    @FXML 
    private void verMapa() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Visualizar Mapa", "Mostrando el mapa del evento sísmico...");
    }
    
    @FXML private void tomarAccionConfirmar() { 
        gestor.confirmarEvento(eventoSeleccionado);
        refrescarTabla(); 
    }
    @FXML private void tomarAccionRechazar() { 
        gestor.rechazarEvento(eventoSeleccionado);
        refrescarTabla(); 
    }
    @FXML private void tomarAccionSolicitarRevision() { 
        gestor.solicitarRevisionExperto(eventoSeleccionado);
        refrescarTabla(); 
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}