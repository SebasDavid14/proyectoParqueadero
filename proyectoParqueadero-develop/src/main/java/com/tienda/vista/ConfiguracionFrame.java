package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.AppContext;
import com.tienda.modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfiguracionFrame extends JFrame {

    private final Usuario admin;
    private Usuario cajeroTarget;

    public ConfiguracionFrame(Usuario adminUser) {
        this.admin = adminUser;
        setTitle("Configuración de Permisos");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Buscar al primer cajero para editarlo
        for (Usuario u : AppContext.usuarioService.getLista()) {
            if (u.getRol() == com.tienda.enums.Rol.CAJERO) {
                cajeroTarget = u;
                break;
            }
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Permisos de Cajero");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(12, 27, 110));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("Habilita o deshabilita los módulos:");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel checksPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        checksPanel.setOpaque(false);
        checksPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JCheckBox chkIngresos = createCheck("INGRESOS", "Registrar entrada de vehículos");
        JCheckBox chkSalidas = createCheck("SALIDAS", "Registrar salida y cobro de vehículos");
        JCheckBox chkTarifas = createCheck("TARIFAS", "Ver y editar tarifas del sistema");
        JCheckBox chkReportes = createCheck("REPORTES", "Acceder al histórico y exportar a Excel");

        checksPanel.add(chkIngresos);
        checksPanel.add(chkSalidas);
        checksPanel.add(chkTarifas);
        checksPanel.add(chkReportes);
        
        checksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSave = new JButton("Guardar Cambios");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background: #0C1B6E; foreground: #FFFFFF; font: bold; arc: 10; margin: 10,20,10,20;");
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSave.addActionListener(e -> {
            if (cajeroTarget != null) {
                cajeroTarget.setPermiso("INGRESOS", chkIngresos.isSelected());
                cajeroTarget.setPermiso("SALIDAS", chkSalidas.isSelected());
                cajeroTarget.setPermiso("TARIFAS", chkTarifas.isSelected());
                cajeroTarget.setPermiso("REPORTES", chkReportes.isSelected());
                
                AppContext.guardarDatos();
                JOptionPane.showMessageDialog(this, "Permisos actualizados con éxito.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblDesc);
        mainPanel.add(checksPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(btnSave);

        add(mainPanel);
    }

    private JCheckBox createCheck(String key, String desc) {
        JCheckBox chk = new JCheckBox(desc);
        chk.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chk.setForeground(Color.DARK_GRAY);
        chk.setOpaque(false);
        if (cajeroTarget != null) {
            chk.setSelected(cajeroTarget.getPermisos().getOrDefault(key, false));
        }
        return chk;
    }
}
