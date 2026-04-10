package com.tienda.servicio;

import com.tienda.modelo.Usuario;
import java.util.*;

public class UsuarioService implements java.io.Serializable {

    private List<Usuario> lista = new ArrayList<>();

    public List<Usuario> getLista() { return lista; }

    public void setLista(List<Usuario> lista) { this.lista = lista; }

    public void agregar(Usuario u) {
        lista.add(u);
    }

    public List<Usuario> listar() {
        return lista;
    }
}
