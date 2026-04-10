package com.tienda.util;

public class Validador {

    public static boolean validarPlacaCarro(String placa) {
        return placa.matches("^[A-Z]{3}[0-9]{3}$");
    }

    public static boolean validarPlacaMoto(String placa) {
        return placa.matches("^[A-Z]{3}[0-9]{2}[A-Z]$");
    }
}
