package com.tienda.vista;

import com.tienda.AppContext;

import javax.swing.*;
import java.awt.*;

public class TarifasFrame extends JFrame {

    public TarifasFrame() {

        setTitle("Gestionar Tarifas");
        setSize(300,200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3,2));

        JTextField txtCarro = new JTextField();
        JTextField txtMoto = new JTextField();
        JButton btnGuardar = new JButton("Guardar");

        panel.add(new JLabel("Tarifa Carro"));
        panel.add(txtCarro);
        panel.add(new JLabel("Tarifa Moto"));
        panel.add(txtMoto);
        panel.add(btnGuardar);

        add(panel);

        btnGuardar.addActionListener(e -> {
            AppContext.tarifa.setTarifaCarro(Double.parseDouble(txtCarro.getText()));
            AppContext.tarifa.setTarifaMoto(Double.parseDouble(txtMoto.getText()));
            JOptionPane.showMessageDialog(this,"Actualizado");
        });
    }
}
