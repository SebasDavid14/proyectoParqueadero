package com.tienda.servicio;

import com.tienda.enums.TipoVehiculo;

public final class TarifaService {

    private double carro = 5000;
    private double moto = 3000;

    public double getTarifa(TipoVehiculo t) {
        return t == TipoVehiculo.CARRO ? carro : moto;
    }

    public void setTarifaCarro(double valor) {
        this.carro = valor;
    }

    public void setTarifaMoto(double valor) {
        this.moto = valor;
    }

    public double calcular(long tiempoMs, TipoVehiculo tipo) {

        long minutos = tiempoMs / (1000 * 60);

        if (minutos < 1) minutos = 1;

        long horas = (long) Math.ceil(minutos / 60.0);

        return horas * getTarifa(tipo);
    }
}
