package com.tienda.modelo;

import com.tienda.enums.Rol;

public abstract class Usuario {

    protected String username;
    protected String password;
    protected Rol rol;
    private boolean activo = true;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActivo() {
        return activo;
    }
}
