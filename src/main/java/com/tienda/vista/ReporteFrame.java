package com.tienda.vista;

import com.tienda.AppContext;

import javax.swing.*;

public class ReporteFrame extends JFrame {

    public ReporteFrame() {

        setTitle("Reporte");
        setSize(300,200);
        setLocationRelativeTo(null);

        double total = AppContext.service.totalIngresos();

        add(new JLabel("Total: "+total));

        JTextArea area = new JTextArea();
        area.setText(
                "Entradas: " + AppContext.service.totalEntradas() +
                        "\nSalidas: " + AppContext.service.totalSalidas() +
                        "\nTotal: $" + AppContext.service.totalIngresos()
        );
    }
}
