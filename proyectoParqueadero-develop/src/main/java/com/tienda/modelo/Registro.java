package com.tienda.modelo;

import com.tienda.enums.EstadoRegistro;
import java.io.Serializable;

public class Registro implements Serializable {

    private final Vehiculo vehiculo; // composición
    private Espacio espacio;
    private long horaEntrada;
    private long horaSalida;
    private double total;
    private EstadoRegistro estado;
    private String motivo;

    public Registro(Vehiculo vehiculo, Espacio espacio) {
        this.vehiculo = vehiculo;
        this.espacio = espacio;
        this.horaEntrada = System.currentTimeMillis();
        this.estado = EstadoRegistro.ACTIVO;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public long getHoraEntrada() {
        return horaEntrada;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }

    public long getHoraSalida() {
        return horaSalida;
    }

    public Espacio getEspacio() {
        return espacio;
    }

    public double getTotal() {
        return total;
    }

    public final void finalizar(double total) {
        this.horaSalida = System.currentTimeMillis();
        this.total = total;
        this.estado = EstadoRegistro.FINALIZADO;
    }

    public void anular(String motivo) {
        this.motivo = motivo;
        this.estado = EstadoRegistro.ANULADO;
    }
}
