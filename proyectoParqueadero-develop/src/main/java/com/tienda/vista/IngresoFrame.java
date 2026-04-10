package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.AppContext;
import com.tienda.enums.TipoVehiculo;
import com.tienda.modelo.Registro;
import com.tienda.util.Validador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IngresoFrame extends JFrame {

    private JTextField txtPlaca;
    private JComboBox<TipoVehiculo> comboTipo;
    private JTextArea txtObservaciones;
    private JPanel panelCascos;

    public IngresoFrame() {
        setTitle("Check-in de Vehículo");
        setSize(400, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("Formulario de Ingreso");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(12, 27, 110));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblPlaca = new JLabel("Placa del Vehículo:");
        lblPlaca.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblPlaca);
        formPanel.add(Box.createRigidArea(new Dimension(0,5)));
        
        txtPlaca = new JTextField();
        txtPlaca.putClientProperty(FlatClientProperties.STYLE, "arc: 8; margin: 5,10,5,10");
        txtPlaca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPlaca.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtPlaca);
        formPanel.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel lblTipo = new JLabel("Tipo de Vehículo:");
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblTipo);
        formPanel.add(Box.createRigidArea(new Dimension(0,5)));
        
        comboTipo = new JComboBox<>(TipoVehiculo.values());
        comboTipo.putClientProperty(FlatClientProperties.STYLE, "arc: 8; padding: 5,10,5,10");
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        comboTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(comboTipo);
        formPanel.add(Box.createRigidArea(new Dimension(0,15)));

        // Panel Dinámico para "Cascos" (solo Moto)
        panelCascos = new JPanel();
        panelCascos.setLayout(new BoxLayout(panelCascos, BoxLayout.Y_AXIS));
        panelCascos.setOpaque(false);
        panelCascos.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCascos = new JLabel("Observaciones (Cascos, color, estado...):");
        lblCascos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCascos.add(lblCascos);
        panelCascos.add(Box.createRigidArea(new Dimension(0,5)));
        
        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.putClientProperty(FlatClientProperties.STYLE, "margin: 5,10,5,10");
        
        JScrollPane scrollDesc = new JScrollPane(txtObservaciones);
        scrollDesc.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelCascos.add(scrollDesc);
        
        // Ocultarlo por defecto si es CARRO
        panelCascos.setVisible(comboTipo.getSelectedItem() == TipoVehiculo.MOTO);

        comboTipo.addActionListener(e -> {
            boolean isMoto = comboTipo.getSelectedItem() == TipoVehiculo.MOTO;
            panelCascos.setVisible(isMoto);
            revalidate();
            repaint();
        });

        formPanel.add(panelCascos);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "background: #F5F5F5; foreground: #333333; font: bold; arc: 8");
        btnCancelar.addActionListener(e -> dispose());
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnRegistrar = new JButton("Registrar Ingreso");
        btnRegistrar.putClientProperty(FlatClientProperties.STYLE, "background: #2E7D32; foreground: #FFFFFF; font: bold; arc: 8");
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegistrar.addActionListener(e -> registrarIngreso());

        bottomPanel.add(btnCancelar);
        bottomPanel.add(btnRegistrar);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void registrarIngreso() {
        String placa = txtPlaca.getText().toUpperCase().trim();
        TipoVehiculo tipo = (TipoVehiculo) comboTipo.getSelectedItem();

        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la placa.");
            return;
        }

        if (tipo == TipoVehiculo.CARRO && !Validador.validarPlacaCarro(placa)) {
            JOptionPane.showMessageDialog(this, "Placa de carro inválida (Ej: ABC123)");
            return;
        }

        if (tipo == TipoVehiculo.MOTO && !Validador.validarPlacaMoto(placa)) {
            JOptionPane.showMessageDialog(this, "Placa de moto inválida (Ej: ABC12D)");
            return;
        }

        String observaciones = "";
        if (tipo == TipoVehiculo.MOTO) {
            observaciones = txtObservaciones.getText().trim();
        }

        // Llamar a servicio para validar ingreso
        AppContext.service.ingreso(placa, tipo);
        
        // Buscar el registro recién creado para setearle los cascos (es informativo)
        Registro activo = AppContext.service.buscarActivo(placa);
        if (activo != null && tipo == TipoVehiculo.MOTO) {
            activo.getVehiculo().setObservaciones(observaciones);
        }

        if (activo != null || AppContext.service.buscarActivo(placa) != null) { // por si el servicio funcionó
            JOptionPane.showMessageDialog(this, "Ingreso registrado correctamente.");
            dispose();
        }
    }
}
