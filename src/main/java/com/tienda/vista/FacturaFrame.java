package com.tienda.vista;

import com.tienda.modelo.Registro;

import javax.swing.*;
import java.awt.*;

public class FacturaFrame extends JFrame {

    public FacturaFrame(Registro r) {

        setTitle("Factura");
        setSize(300,200);
        setLocationRelativeTo(null);

        long minutos = (r.getTotal() == 0) ? 0 :
                (System.currentTimeMillis() - r.getHoraEntrada()) / 60000;

        JTextArea area = new JTextArea();

        area.setEditable(false);

        area.setText(
                "=== PARQUEADERO ===\n" +
                        "Placa: " + r.getVehiculo().getPlaca() +
                        "\nTipo: " + r.getVehiculo().getTipo() +
                        "\nEntrada: " + r.getHoraEntrada() +
                        "\nSalida: " + r.getHoraSalida() +
                        "\nTiempo(min): " + minutos +
                        "\nTotal: $" + r.getTotal()
        );

        add(area);
    }
}
