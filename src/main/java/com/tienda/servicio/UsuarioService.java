package com.tienda.servicio;

import com.tienda.modelo.Usuario;
import java.util.*;

public class UsuarioService {

    private List<Usuario> lista = new ArrayList<>();

    public void agregar(Usuario u) {
        lista.add(u);
    }

    public List<Usuario> listar() {
        return lista;
    }
}
