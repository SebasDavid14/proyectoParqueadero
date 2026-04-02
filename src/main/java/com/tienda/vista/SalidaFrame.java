package com.tienda.vista;

import com.tienda.AppContext;
import com.tienda.modelo.Registro;

import javax.swing.*;
import java.awt.*;

public class SalidaFrame extends JFrame {

    public SalidaFrame() {

        setTitle("Salida");
        setSize(300,150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2,2));

        JTextField txtPlaca = new JTextField();
        JButton btn = new JButton("COBRAR");

        panel.add(new JLabel("Placa"));
        panel.add(txtPlaca);
        panel.add(btn);

        add(panel);

        btn.addActionListener(e -> {
            String placa = txtPlaca.getText();

            Registro r = AppContext.service.buscarActivo(placa);

            if (r == null) {
                JOptionPane.showMessageDialog(this, "No existe activo");
                return;
            }

            double total = AppContext.service.calcularTotal(r);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Placa: " + placa +
                            "\nTotal: " + total +
                            "\n¿Confirmar pago?"
            );

            if (confirm == JOptionPane.YES_OPTION) {
                AppContext.service.finalizarSalida(r, total);
                new FacturaFrame(r).setVisible(true);
            }
        });
    }
}
