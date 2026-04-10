package com.tienda;

import com.tienda.modelo.*;
import com.tienda.repositorio.*;
import com.tienda.servicio.*;
import com.tienda.util.PersistenciaUtil;

import java.util.List;
import java.util.Map;

public class AppContext {

    public static RegistroRepositorio repo = new RegistroRepositorio();
    public static TarifaService tarifa = new TarifaService();
    public static ParqueaderoService service = new ParqueaderoService(repo, tarifa);

    public static AuthService auth = new AuthService();

    public static UsuarioService usuarioService = new UsuarioService();

    static {
        // Cargar registros
        Map<String, Registro> m = (Map<String, Registro>) PersistenciaUtil.cargarObjeto("repo.dat");
        if (m != null) repo.setMapa(m);
        
        // Cargar usuarios
        List<Usuario> lu = (List<Usuario>) PersistenciaUtil.cargarObjeto("usuarios.dat");
        if (lu != null && !lu.isEmpty()) {
            usuarioService.setLista(lu);
        } else {
            usuarioService.agregar(new Admin("admin", "123"));
            usuarioService.agregar(new Cajero("cajero", "456"));
        }
    }

    public static void guardarDatos() {
        PersistenciaUtil.guardarObjeto(repo.getMapa(), "repo.dat");
        PersistenciaUtil.guardarObjeto(usuarioService.getLista(), "usuarios.dat");
    }
}
