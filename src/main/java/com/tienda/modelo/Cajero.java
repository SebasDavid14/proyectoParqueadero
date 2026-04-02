package com.tienda.modelo;

import com.tienda.enums.Rol;

public class Cajero extends Usuario{

    public Cajero(String user, String pass) {
        username = user;
        password = pass;
        rol = Rol.CAJERO;
    }
}
