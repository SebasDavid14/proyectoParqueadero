package com.tienda.vista;

import com.tienda.AppContext;
import com.tienda.enums.TipoVehiculo;
import com.tienda.util.Validador;

import javax.swing.*;
import java.awt.*;

public class IngresoFrame extends JFrame {

    public IngresoFrame() {

        setTitle("Ingreso Vehículo");
        setSize(300,200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3,2));

        JTextField txtPlaca = new JTextField();
        JComboBox<TipoVehiculo> combo = new JComboBox<>(TipoVehiculo.values());
        JButton btn = new JButton("Registrar");

        panel.add(new JLabel("Placa"));
        panel.add(txtPlaca);
        panel.add(new JLabel("Tipo"));
        panel.add(combo);
        panel.add(btn);

        add(panel);

        btn.addActionListener(e -> {
            String placa = txtPlaca.getText().toUpperCase();
            TipoVehiculo tipo = (TipoVehiculo) combo.getSelectedItem();

            if (tipo == TipoVehiculo.CARRO && !Validador.validarPlacaCarro(placa)) {
                JOptionPane.showMessageDialog(this,"Placa carro inválida");
                return;
            }

            if (tipo == TipoVehiculo.MOTO && !Validador.validarPlacaMoto(placa)) {
                JOptionPane.showMessageDialog(this,"Placa moto inválida");
                return;
            }
            AppContext.service.ingreso(placa,tipo);
            JOptionPane.showMessageDialog(this, "Ingreso registrado");
        });
    }
}
