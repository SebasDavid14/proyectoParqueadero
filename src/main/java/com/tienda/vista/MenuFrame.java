package com.tienda.vista;

import com.tienda.modelo.Admin;
import com.tienda.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {
    public MenuFrame(Usuario user) {
        setTitle("Menú");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1));

        JButton ingreso = new JButton("Registrar Entrada");
        JButton salida = new JButton("Registrar Salida");
        JButton reporte = new JButton("Ver Reporte");
        JButton ocupacion = new JButton("Ocupación");

        panel.add(ingreso);
        panel.add(salida);
        panel.add(reporte);
        panel.add(ocupacion);

        if (user instanceof Admin) {
            JButton tarifas = new JButton("Gestionar Tarifas");
            panel.add(tarifas);
            tarifas.addActionListener(e -> {new TarifasFrame().setVisible(true);});
        }

        ingreso.addActionListener(e -> new IngresoFrame().setVisible(true));
        salida.addActionListener(e -> new SalidaFrame().setVisible(true));
        reporte.addActionListener(e -> new ReporteFrame().setVisible(true));
        ocupacion.addActionListener(e -> new OcupacionFrame().setVisible(true));

        add(panel);
    }
}