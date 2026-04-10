package com.tienda.modelo;
import java.io.Serializable;

public class Espacio implements Serializable {

    private int numero;
    private boolean ocupado;

    public Espacio(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}
