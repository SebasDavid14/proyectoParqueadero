package com.tienda;

import com.tienda.modelo.*;
import com.tienda.repositorio.*;
import com.tienda.servicio.*;

public class AppContext {

    public static RegistroRepositorio repo = new RegistroRepositorio();
    public static TarifaService tarifa = new TarifaService();
    public static ParqueaderoService service = new ParqueaderoService(repo, tarifa);

    public static AuthService auth = new AuthService();

    public static UsuarioService usuarioService = new UsuarioService();
    static {
        usuarioService.agregar(new Admin("admin", "123"));
        usuarioService.agregar(new Cajero("cajero", "456"));
    }
}
