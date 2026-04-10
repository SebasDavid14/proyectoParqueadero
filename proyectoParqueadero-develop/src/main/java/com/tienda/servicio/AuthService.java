package com.tienda.servicio;

import com.tienda.AppContext;
import com.tienda.modelo.*;


import java.util.ArrayList;
import java.util.List;

public class AuthService {

    public Usuario login(String user, String pass) {
        for (Usuario u : AppContext.usuarioService.listar()) {
            if (u.getUsername().equals(user) && u.getPassword().equals(pass) && u.isActivo()) {
                return u;
            }
        }
        return null;
    }
}
