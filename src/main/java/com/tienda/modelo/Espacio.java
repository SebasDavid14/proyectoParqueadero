package com.tienda.modelo;

public class Espacio {

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
