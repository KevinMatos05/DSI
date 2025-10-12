package com.example.modelo;
import java.util.ArrayList;
import java.util.List;

public class MuestraSismica {
    private List<DetalleMuestraSismica> detallesMuestraSismica;

    public MuestraSismica() {
        this.detallesMuestraSismica = new ArrayList<>();
    }
    
    // Getters y Setters
    public List<DetalleMuestraSismica> getDetallesMuestraSismica() { return detallesMuestraSismica; }
    public void setDetallesMuestraSismica(List<DetalleMuestraSismica> detalles) { this.detallesMuestraSismica = detalles; }
}