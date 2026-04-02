package com.tienda.repositorio;

import com.tienda.modelo.Registro;
import java.util.*;

public class RegistroRepositorio implements ICrud<Registro>{

    private final Map<String, Registro> mapa = new HashMap<>();

    @Override
    public void crear(Registro obj) {
        mapa.put(obj.getVehiculo().getPlaca(), obj);
    }

    @Override
    public Registro buscar(String placa) {
        return mapa.get(placa.toUpperCase());
    }

    @Override
    public List<Registro> listar() {
        return new ArrayList<>(mapa.values());
    }

    @Override
    public void eliminar(String placa) {
        mapa.remove(placa.toUpperCase());
    }
}
