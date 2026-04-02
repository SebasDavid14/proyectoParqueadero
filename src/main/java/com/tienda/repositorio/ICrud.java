package com.tienda.repositorio;

import java.util.List;

public interface ICrud<T>{

    void crear(T obj);
    T buscar(String id);
    List<T> listar();
    void eliminar(String id);
}
