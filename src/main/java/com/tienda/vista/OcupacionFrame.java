package com.tienda.vista;

import com.tienda.AppContext;

import javax.swing.*;

public class OcupacionFrame extends JFrame {

    public OcupacionFrame() {

        setTitle("Ocupación");
        setSize(300,200);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText(
                "Total cupos: " + AppContext.service.totalCupos() +
                        "\nOcupados: " + AppContext.service.ocupados() +
                        "\nDisponibles: " + AppContext.service.disponibles() +
                        "\n% Ocupación: " + AppContext.service.porcentaje()
        );

        add(area);
    }
}
