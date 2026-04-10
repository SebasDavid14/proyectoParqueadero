package com.tienda.modelo;

import com.tienda.enums.Rol;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Usuario implements Serializable {

    protected String username;
    protected String password;
    protected Rol rol;
    private boolean activo = true;
    private Map<String, Boolean> permisos = new HashMap<>();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActivo() {
        return activo;
    }

    public Rol getRol() {
        return rol;
    }

    public Map<String, Boolean> getPermisos() {
        return permisos;
    }

    public void setPermiso(String modulo, boolean valor) {
        permisos.put(modulo, valor);
    }
}
