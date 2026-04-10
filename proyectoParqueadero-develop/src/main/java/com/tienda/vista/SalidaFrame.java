package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.AppContext;
import com.tienda.modelo.Registro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class SalidaFrame extends JFrame {

    private JTextField txtPlaca;

    public SalidaFrame() {
        setTitle("Check-out de Vehículo");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("Formulario de Salida (Cobro)");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(12, 27, 110));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Content
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        formPanel.add(new JLabel("Placa del Vehículo:"));
        txtPlaca = new JTextField();
        txtPlaca.putClientProperty(FlatClientProperties.STYLE, "arc: 8; margin: 5,10,5,10");
        formPanel.add(txtPlaca);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "background: #F5F5F5; foreground: #333333; font: bold; arc: 8");
        btnCancelar.addActionListener(e -> dispose());
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnCobrar = new JButton("Cobrar / Verificación");
        btnCobrar.putClientProperty(FlatClientProperties.STYLE, "background: #E65100; foreground: #FFFFFF; font: bold; arc: 8");
        btnCobrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCobrar.addActionListener(e -> procesarSalida());

        bottomPanel.add(btnCancelar);
        bottomPanel.add(btnCobrar);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void procesarSalida() {
        String placa = txtPlaca.getText().toUpperCase().trim();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una placa.");
            return;
        }

        Registro r = AppContext.service.buscarActivo(placa);
        if (r == null) {
            JOptionPane.showMessageDialog(this, "El vehículo no se encuentra activo / Ingreso no registrado.");
            return;
        }

        double total = AppContext.service.calcularTotal(r);
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        // Checklist Informativo
        JCheckBox chkEspejos = new JCheckBox("Espejos (Sin daños comprobados)");
        JCheckBox chkPintura = new JCheckBox("Pintura (Sin rayones nuevos)");
        JCheckBox chkVidrios = new JCheckBox("Vidrios (Sin quiebres / fisuras)");

        JPanel panelChecks = new JPanel(new GridLayout(3, 1));
        panelChecks.setOpaque(false);
        panelChecks.add(chkEspejos);
        panelChecks.add(chkPintura);
        panelChecks.add(chkVidrios);

        String observaciones = r.getVehiculo().getObservaciones();
        boolean tieneObs = r.getVehiculo().getTipo() == com.tienda.enums.TipoVehiculo.MOTO 
                && observaciones != null && !observaciones.isEmpty();

        Object[] msg;
        if (tieneObs) {
            JTextArea txtAreaObs = new JTextArea(4, 25);
            txtAreaObs.setText(observaciones);
            txtAreaObs.setEditable(false);
            txtAreaObs.setLineWrap(true);
            txtAreaObs.setWrapStyleWord(true);
            txtAreaObs.setFont(new Font("SansSerif", Font.PLAIN, 12));
            txtAreaObs.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            JScrollPane scrollObs = new JScrollPane(txtAreaObs);

            msg = new Object[]{
                "Verificación de estado exterior (Informativo para la entrega):",
                panelChecks,
                " ",
                "Placa: " + placa,
                "¡ATENCIÓN! Observaciones retenidas del ingreso:",
                scrollObs,
                " ",
                "Total a pagar: " + formatoMoneda.format(total),
                "¿Confirmar salida segura y facturar?"
            };
        } else {
            msg = new Object[]{
                "Verificación de estado exterior (Informativo para la entrega):",
                panelChecks,
                " ",
                "Placa: " + placa,
                "Total a pagar: " + formatoMoneda.format(total),
                "¿Confirmar salida segura y facturar?"
            };
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                msg,
                "Confirmación de Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            AppContext.service.finalizarSalida(r, total);
            new FacturaFrame(r).setVisible(true);
            dispose();
        }
    }
}
