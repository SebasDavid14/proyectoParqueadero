package com.tienda.servicio;

import com.tienda.enums.*;
import com.tienda.modelo.*;
import com.tienda.repositorio.RegistroRepositorio;

import java.util.ArrayList;
import java.util.List;

public class ParqueaderoService {

    private final RegistroRepositorio repo;
    private final TarifaService tarifaService;
    private final List<Espacio> espacios = new ArrayList<>();

    public ParqueaderoService(RegistroRepositorio repo, TarifaService tarifaService) {
        this.repo = repo;
        this.tarifaService = tarifaService;

        // crear 50 espacios
        for (int i = 1; i <= 50; i++) {
            espacios.add(new Espacio(i));
        }
    }

    public void ingreso(String placa, TipoVehiculo tipo){
        if(repo.buscar(placa)!=null){
            System.out.println("Ya existe");
            return;
        }

        Espacio e = espacios.stream().filter(x->!x.isOcupado()).findFirst().orElse(null);
        if(e==null){
            System.out.println("Sin cupos");
            return;
        }

        e.setOcupado(true);
        repo.crear(new Registro(new Vehiculo(placa,tipo),e));
    }

    public List<Registro> listar(){
        return repo.listar();
    }

    public double totalIngresos(){
        return repo.listar().stream()
                .filter(r->r.getEstado()==EstadoRegistro.FINALIZADO)
                .mapToDouble(Registro::getTotal)
                .sum();
    }

    public int totalCupos() {
        return espacios.size();
    }

    public long ocupados() {
        return espacios.stream().filter(Espacio::isOcupado).count();
    }

    public long ocupadosTipo(TipoVehiculo tipo) {
        return repo.listar().stream()
                .filter(r -> r.getEstado() == EstadoRegistro.ACTIVO && r.getVehiculo().getTipo() == tipo)
                .count();
    }

    public long disponibles() {
        return totalCupos() - ocupados();
    }

    public double porcentaje() {
        return (ocupados() * 100.0) / totalCupos();
    }

    public Registro buscarActivo(String placa) {
        Registro r = repo.buscar(placa);

        if (r != null && r.getEstado() == EstadoRegistro.ACTIVO) {
            return r;
        }
        return null;
    }

    public double calcularTotal(Registro r) {
        long tiempo = System.currentTimeMillis() - r.getHoraEntrada();
        return tarifaService.calcular(tiempo, r.getVehiculo().getTipo());
    }

    public void finalizarSalida(Registro r, double total) {
        r.finalizar(total);
        r.getEspacio().setOcupado(false);
    }

    public void configurarCapacidad(int n) {

        if (n < ocupados()) {
            return;
        }

        espacios.clear();

        for (int i = 1; i <= n; i++) {
            espacios.add(new Espacio(i));
        }
    }

    public long totalEntradas() {
        return repo.listar().size();
    }

    public long totalSalidas() {
        return repo.listar().stream()
                .filter(r -> r.getEstado() == EstadoRegistro.FINALIZADO)
                .count();
    }
}
