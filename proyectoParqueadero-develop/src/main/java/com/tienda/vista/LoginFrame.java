package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.AppContext;
import com.tienda.modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    
    public LoginFrame() {
        setTitle("Parking Pro - Login");
        setSize(900, 550); // Larger size for the split design
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
        // ---- Left Panel (Blue branding pane) ----
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(12, 27, 110)); // Deep blue color #0C1B6E
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        // Logo label
        JLabel lblLogo = new JLabel("P Parking Pro");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Main Headline
        JLabel lblHeadline = new JLabel("<html>Gestión de precisión<br>para espacios<br>modernos.</html>");
        lblHeadline.setForeground(Color.WHITE);
        lblHeadline.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblHeadline.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Subtext
        JLabel lblSubtext = new JLabel("<html><div style='width: 250px; line-height: 1.5;'>Optimice el flujo de vehículos y la rentabilidad de su estacionamiento con nuestra plataforma de grado arquitectónico.</div></html>");
        lblSubtext.setForeground(new Color(160, 170, 220));
        lblSubtext.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtext.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Stats Panel (Bottom part)
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(400, 80));
        
        JPanel stat1 = createStatBox("99.9%", "UPTIME DEL SISTEMA");
        JPanel stat2 = createStatBox("Real-Time", "MONITOREO DE SALIDAS");
        statsPanel.add(stat1);
        statsPanel.add(stat2);
        
        // Assemble left panel
        leftPanel.add(lblLogo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(lblHeadline);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(lblSubtext);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(statsPanel);
        
        // ---- Right Panel (Login form) ----
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(60, 60, 40, 60));
        
        JLabel lblWelcome = new JLabel("Bienvenido");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(12, 27, 110));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblInstruction = new JLabel("Ingrese sus credenciales para acceder al panel de");
        JLabel lblInstruction2 = new JLabel("administración.");
        lblInstruction.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInstruction2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInstruction.setForeground(Color.DARK_GRAY);
        lblInstruction2.setForeground(Color.DARK_GRAY);
        lblInstruction.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblInstruction2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // User field
        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblUser.setForeground(Color.GRAY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtUser = new JTextField();
        txtUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "usuario@parkingpro.com");
        txtUser.putClientProperty(FlatClientProperties.STYLE, "arc: 5; margin: 5,10,5,10");
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password field
        JPanel passHeader = new JPanel(new BorderLayout());
        passHeader.setOpaque(false);
        passHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        passHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        
        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblPass.setForeground(Color.GRAY);
        
        JLabel lblForgot = new JLabel("<html><a href='#'>Forgot Password?</a></html>");
        lblForgot.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        passHeader.add(lblPass, BorderLayout.WEST);
        passHeader.add(lblForgot, BorderLayout.EAST);
        
        JPasswordField txtPass = new JPasswordField();
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        txtPass.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true; arc: 5; margin: 5,10,5,10");
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Checkbox
        JCheckBox chkRemember = new JCheckBox("Recordar sesión en este equipo");
        chkRemember.setOpaque(false);
        chkRemember.setFont(new Font("SansSerif", Font.PLAIN, 12));
        chkRemember.setForeground(Color.DARK_GRAY);
        chkRemember.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkRemember.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Button
        JButton btnLogin = new JButton("Ingresar \u2192");
        btnLogin.putClientProperty(FlatClientProperties.STYLE, "background: #0C1B6E; foreground: #FFFFFF; font: bold; arc: 10; margin: 10,20,10,20; focusColor: #0C1B6E");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        footerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        
        JLabel lblVersion = new JLabel("V4.2.0 STABLE BUILD");
        lblVersion.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblVersion.setForeground(Color.GRAY);
        
        JLabel lblLinks = new JLabel("SOPORTE TÉCNICO   PRIVACIDAD");
        lblLinks.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblLinks.setForeground(Color.GRAY);
        
        footerPanel.add(lblVersion, BorderLayout.WEST);
        footerPanel.add(lblLinks, BorderLayout.EAST);
        
        // Assemble right panel
        rightPanel.add(lblWelcome);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(lblInstruction);
        rightPanel.add(lblInstruction2);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        
        rightPanel.add(lblUser);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(txtUser);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        rightPanel.add(passHeader);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(txtPass);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        rightPanel.add(chkRemember);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(new JSeparator());
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(footerPanel);
        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
        
        // Events
        btnLogin.addActionListener(e -> {
            Usuario u = AppContext.auth.login(
                    txtUser.getText(),
                    new String(txtPass.getPassword())
            );

            if(u == null){
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                new MenuFrame(u).setVisible(true);
                dispose();
            }
        });
    }
    
    private JPanel createStatBox(String value, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(28, 43, 119)); // Slightly lighter blue for the boxes
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(160, 170, 220));
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lblValue);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblTitle);
        
        return panel;
    }
}
