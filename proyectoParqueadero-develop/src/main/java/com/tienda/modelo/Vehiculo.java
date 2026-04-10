package com.tienda.modelo;

import com.tienda.enums.TipoVehiculo;
import java.io.Serializable;

public class Vehiculo implements Serializable {

    private final String placa;
    private TipoVehiculo tipo;
    private String observaciones; // Informativo para motos u otros rescates

    public Vehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa.toUpperCase();
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
