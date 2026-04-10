package com.tienda.modelo;

import com.tienda.enums.Rol;

public class Admin extends Usuario{

    public Admin(String user, String pass) {
        username = user;
        password = pass;
        rol = Rol.ADMIN;
        // Permisos plenos por defecto para admin
        setPermiso("INGRESOS", true);
        setPermiso("SALIDAS", true);
        setPermiso("TARIFAS", true);
        setPermiso("REPORTES", true);
    }
}
