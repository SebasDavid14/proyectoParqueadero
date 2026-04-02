package com.tienda.modelo;

import com.tienda.enums.TipoVehiculo;

public class Vehiculo {

    private final String placa;
    private TipoVehiculo tipo;

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
}
