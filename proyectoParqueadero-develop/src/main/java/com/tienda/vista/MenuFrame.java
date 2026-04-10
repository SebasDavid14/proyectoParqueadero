package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.AppContext;
import com.tienda.enums.EstadoRegistro;
import com.tienda.enums.TipoVehiculo;
import com.tienda.modelo.Admin;
import com.tienda.modelo.Registro;
import com.tienda.modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MenuFrame extends JFrame {

    private JLabel lblOcupados;
    private JLabel lblDisponibles;
    private JLabel lblRecaudo;
    private JProgressBar progAutos;
    private JProgressBar progMotos;
    private JLabel lblAutosText;
    private JLabel lblMotosText;
    private DefaultTableModel tableModel;
    private JPanel recentActivityPanel;
    private JTextField txtSearch;

    private final Usuario user;

    public MenuFrame(Usuario user) {
        this.user = user;
        setTitle("Parking Pro - Dashboard Principal");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximized by default

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createMainContent(), BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Refrescar los datos periódicamente (cada 5 segundos) para simular tiempo real
        Timer timer = new Timer(5000, e -> actualizarDashboard());
        timer.start();
        
        actualizarDashboard();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(250, 250, 250));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(new Color(250, 250, 250));
        logoPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        
        JLabel lblLogo = new JLabel("P Parking Pro");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLogo.setForeground(new Color(12, 27, 110));
        
        JLabel lblSub = new JLabel("SYSTEM ADMIN");
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblSub.setForeground(Color.GRAY);
        
        logoPanel.add(lblLogo);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(lblSub);
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logoPanel);

        // Botones del menú
        sidebar.add(createMenuBtn("DASHBOARD", true, null));
        
        if (user.getPermisos().getOrDefault("INGRESOS", false)) {
            sidebar.add(createMenuBtn("INGRESOS", false, e -> new IngresoFrame().setVisible(true)));
        }
        if (user.getPermisos().getOrDefault("SALIDAS", false)) {
            sidebar.add(createMenuBtn("SALIDAS", false, e -> new SalidaFrame().setVisible(true)));
        }
        if (user.getPermisos().getOrDefault("TARIFAS", false)) {
            sidebar.add(createMenuBtn("TARIFAS", false, e -> new TarifasFrame().setVisible(true)));
        }
        if (user.getPermisos().getOrDefault("REPORTES", false)) {
            sidebar.add(createMenuBtn("REPORTES", false, e -> new ReporteFrame().setVisible(true)));
        }
        
        if (user instanceof Admin) {
            sidebar.add(createMenuBtn("CONFIGURACIÓN", false, e -> new ConfiguracionFrame(user).setVisible(true)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Help y Logout
        sidebar.add(createMenuBtn("HELP", false, null));
        JButton btnLogout = createMenuBtn("LOGOUT", false, e -> {
            com.tienda.AppContext.guardarDatos();
            new LoginFrame().setVisible(true);
            dispose();
        });
        btnLogout.setForeground(new Color(200, 50, 50));
        sidebar.add(btnLogout);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }

    private JButton createMenuBtn(String text, boolean active, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 0; margin: 15,20,15,20; borderWidth: 0; focusWidth: 0;");
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (active) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(12, 27, 110));
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(12, 27, 110)));
        } else {
            btn.setBackground(new Color(250, 250, 250));
            btn.setForeground(Color.DARK_GRAY);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(250, 250, 250)));
        }

        if (listener != null) {
            btn.addActionListener(listener);
        }
        return btn;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 246, 250));

        // Topbar
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(new Color(12, 27, 110));
        topbar.setPreferredSize(new Dimension(0, 60));
        topbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTopTitle = new JLabel("THE PRECISION ARCHITECT");
        lblTopTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTopTitle.setForeground(Color.WHITE);
        topbar.add(lblTopTitle, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightTop.setOpaque(false);
        
        txtSearch = new JTextField(15);
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar placa u oprimir Enter...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 15");

        txtSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim().toUpperCase();
            if (q.isEmpty()) return;
            Registro r = AppContext.service.buscarActivo(q);
            if (r != null) {
                SimpleDateFormat sdfInfo = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                double cobroTemp = AppContext.service.calcularTotal(r);
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
                String info = "PLACA: " + r.getVehiculo().getPlaca() + "\n"
                            + "TIPO: " + r.getVehiculo().getTipo().name() + "\n"
                            + "ZONA: " + r.getEspacio().getNumero() + "\n"
                            + "HORA INGRESO: " + sdfInfo.format(new java.util.Date(r.getHoraEntrada())) + "\n"
                            + "SALDO HASTA AHORA: " + nf.format(cobroTemp);
                JOptionPane.showMessageDialog(this, info, "Consulta de Vehículo Activo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Placa '" + q + "' no encontrada en el parqueadero.", "Sin resultados", JOptionPane.WARNING_MESSAGE);
            }
        });

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarDashboard(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarDashboard(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarDashboard(); }
        });
        
        JButton btnCheckin = new JButton("Check-in");
        btnCheckin.putClientProperty(FlatClientProperties.STYLE, "background: #2E7D32; foreground: #FFFFFF; font: bold; arc: 5; margin: 5,15,5,15");
        btnCheckin.addActionListener(e -> new IngresoFrame().setVisible(true));
        
        JLabel lblAdminInfo = new JLabel("<html><div style='text-align: right;'><span style='color: white; font-weight: bold;'>Admin Usuario</span><br><span style='color: #AAAAAA; font-size: 9px;'>ADMINISTRATOR</span></div></html>");

        rightTop.add(txtSearch);
        rightTop.add(btnCheckin);
        rightTop.add(lblAdminInfo);
        topbar.add(rightTop, BorderLayout.EAST);

        content.add(topbar, BorderLayout.NORTH);

        // Dashboard Body
        JPanel body = new JPanel(new BorderLayout(20, 20));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel headerBody = new JPanel(new BorderLayout());
        headerBody.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard Principal");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(12, 27, 110));
        
        JLabel subtitle = new JLabel("Visión general del estado del parqueadero en tiempo real.");
        subtitle.setForeground(Color.GRAY);
        
        JPanel pTitle = new JPanel(new GridLayout(2,1));
        pTitle.setOpaque(false);
        pTitle.add(title);
        pTitle.add(subtitle);
        
        JLabel lblStatus = new JLabel("● SISTEMA EN LÍNEA");
        lblStatus.setForeground(new Color(46, 125, 50));
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 200), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));

        headerBody.add(pTitle, BorderLayout.WEST);
        headerBody.add(lblStatus, BorderLayout.EAST);
        
        body.add(headerBody, BorderLayout.NORTH);

        // Main Center Grid
        JPanel centerGrid = new JPanel(new BorderLayout(20, 20));
        centerGrid.setOpaque(false);

        // Cards (Top of center grid)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        
        JPanel card1 = createCard("TOTAL CUPOS", "50", "Capacidad máxima autorizada", new Color(12, 27, 110), true);
        
        JPanel p2 = createCard("OCUPADOS", "0", "Ocupación actual", new Color(245, 166, 35), false);
        lblOcupados = (JLabel) p2.getClientProperty("valueLabel");
        
        JPanel p3 = createCard("DISPONIBLES", "0", "Espacios listos para ingreso", new Color(46, 125, 50), false);
        lblDisponibles = (JLabel) p3.getClientProperty("valueLabel");

        JPanel p4 = createCard("RECAUDO HOY", "$0", "Ingresos", new Color(12, 27, 110), false);
        lblRecaudo = (JLabel) p4.getClientProperty("valueLabel");

        cardsPanel.add(card1);
        cardsPanel.add(p2);
        cardsPanel.add(p3);
        cardsPanel.add(p4);
        
        centerGrid.add(cardsPanel, BorderLayout.NORTH);

        // Bottom panels
        JPanel bottomPanels = new JPanel(new BorderLayout(20, 0));
        bottomPanels.setOpaque(false);

        // Left Bottom (Table)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        JLabel lblTableTitle = new JLabel("Vehículos Actualmente Parqueados");
        lblTableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTableTitle.setForeground(new Color(12, 27, 110));
        tableHeader.add(lblTableTitle, BorderLayout.WEST);
        
        JButton btnExport = new JButton("EXPORTAR");
        tableHeader.add(btnExport, BorderLayout.EAST);
        tablePanel.add(tableHeader, BorderLayout.NORTH);

        String[] cols = {"PLACA", "TIPO", "HORA INGRESO", "UBICACIÓN", "ACCIONES"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        scrollTable.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        bottomPanels.add(tablePanel, BorderLayout.CENTER);

        // Right Bottom (Stats)
        JPanel rightStats = new JPanel();
        rightStats.setLayout(new BoxLayout(rightStats, BoxLayout.Y_AXIS));
        rightStats.setOpaque(false);
        rightStats.setPreferredSize(new Dimension(300, 0));

        // Progreso por tipo
        JPanel progresoPanel = new JPanel();
        progresoPanel.setLayout(new BoxLayout(progresoPanel, BoxLayout.Y_AXIS));
        progresoPanel.setBackground(Color.WHITE);
        progresoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        progresoPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        JLabel lblProgTitle = new JLabel("OCUPACIÓN POR TIPO");
        lblProgTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblProgTitle.setForeground(new Color(12, 27, 110));
        progresoPanel.add(lblProgTitle);
        progresoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        progAutos = new JProgressBar();
        progAutos.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        progAutos.setForeground(new Color(12, 27, 110));
        lblAutosText = new JLabel("0/50");
        lblAutosText.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JPanel pAutosT = new JPanel(new BorderLayout());
        pAutosT.setOpaque(false);
        pAutosT.add(new JLabel("Automóviles"), BorderLayout.WEST);
        pAutosT.add(lblAutosText, BorderLayout.EAST);
        
        progresoPanel.add(pAutosT);
        progresoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        progresoPanel.add(progAutos);
        progresoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        progMotos = new JProgressBar();
        progMotos.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        progMotos.setForeground(new Color(245, 166, 35));
        lblMotosText = new JLabel("0/50");
        lblMotosText.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JPanel pMotosT = new JPanel(new BorderLayout());
        pMotosT.setOpaque(false);
        pMotosT.add(new JLabel("Motocicletas"), BorderLayout.WEST);
        pMotosT.add(lblMotosText, BorderLayout.EAST);
        
        progresoPanel.add(pMotosT);
        progresoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        progresoPanel.add(progMotos);

        rightStats.add(progresoPanel);
        rightStats.add(Box.createRigidArea(new Dimension(0, 20)));

        // Actividad reciente
        JPanel actividadPanel = new JPanel(new BorderLayout());
        actividadPanel.setBackground(Color.WHITE);
        actividadPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        actividadPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        JLabel lblActTitle = new JLabel("ACTIVIDAD RECIENTE");
        lblActTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblActTitle.setForeground(new Color(12, 27, 110));
        actividadPanel.add(lblActTitle, BorderLayout.NORTH);

        recentActivityPanel = new JPanel();
        recentActivityPanel.setLayout(new BoxLayout(recentActivityPanel, BoxLayout.Y_AXIS));
        recentActivityPanel.setOpaque(false);
        actividadPanel.add(recentActivityPanel, BorderLayout.CENTER);

        rightStats.add(actividadPanel);

        bottomPanels.add(rightStats, BorderLayout.EAST);
        centerGrid.add(bottomPanels, BorderLayout.CENTER);
        
        body.add(centerGrid, BorderLayout.CENTER);
        content.add(body, BorderLayout.CENTER);

        return content;
    }

    private JPanel createCard(String title, String value, String sub, Color markerColor, boolean markedBg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, markerColor),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblValue.setForeground(new Color(12, 27, 110));

        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(new Font("SansSerif", Font.ITALIC, 10));
        lblSub.setForeground(Color.GRAY);

        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        p.add(lblSub, BorderLayout.SOUTH);

        p.putClientProperty("valueLabel", lblValue);
        return p;
    }

    private void actualizarDashboard() {
        int total = AppContext.service.totalCupos();
        long ocupados = AppContext.service.ocupados();
        long disponibles = AppContext.service.disponibles();
        double recaudo = AppContext.service.totalIngresos();

        if (lblOcupados != null) lblOcupados.setText(String.valueOf(ocupados));
        if (lblDisponibles != null) lblDisponibles.setText(String.valueOf(disponibles));
        if (lblRecaudo != null) {
            NumberFormat formater = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            lblRecaudo.setText(formater.format(recaudo));
        }

        // Progress bars
        long motosOcupadas = AppContext.service.ocupadosTipo(TipoVehiculo.MOTO);
        long carrosOcupadas = AppContext.service.ocupadosTipo(TipoVehiculo.CARRO);

        if (progAutos != null) {
            progAutos.setMaximum(total);
            progAutos.setValue((int) carrosOcupadas);
            lblAutosText.setText(carrosOcupadas + "/" + total);
        }

        if (progMotos != null) {
            progMotos.setMaximum(total);
            progMotos.setValue((int) motosOcupadas);
            lblMotosText.setText(motosOcupadas + "/" + total);
        }

        // Tabla
        if (tableModel != null) {
            tableModel.setRowCount(0);
            List<Registro> registros = AppContext.service.listar();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            
            String filtro = (txtSearch != null && txtSearch.getText() != null) ? txtSearch.getText().trim().toUpperCase() : "";

            for (int i = registros.size() - 1; i >= 0; i--) {
                Registro r = registros.get(i);
                if (r.getEstado() == EstadoRegistro.ACTIVO) {
                    if (!filtro.isEmpty() && !r.getVehiculo().getPlaca().contains(filtro)) {
                        continue;
                    }
                    String placa = r.getVehiculo().getPlaca();
                    String tipo = r.getVehiculo().getTipo() == TipoVehiculo.CARRO ? "Automóvil" : "Motocicleta";
                    String hora = sdf.format(new java.util.Date(r.getHoraEntrada()));
                    String ubicacion = "Zona " + (r.getVehiculo().getTipo() == TipoVehiculo.CARRO ? "A" : "M") + " - " + r.getEspacio().getNumero();
                    tableModel.addRow(new Object[]{placa, tipo, hora, ubicacion, "\u2192 Consultar"});
                }
            }
        }

        // Actividad Reciente
        if (recentActivityPanel != null) {
            recentActivityPanel.removeAll();
            recentActivityPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            List<Registro> registros = AppContext.service.listar();
            SimpleDateFormat sdfAct = new SimpleDateFormat("hh:mm a");
            int count = 0;
            
            for (int i = registros.size() - 1; i >= 0; i--) {
                Registro r = registros.get(i);
                if (count >= 4) break;

                JPanel p = new JPanel(new GridLayout(2, 1));
                p.setOpaque(false);
                p.setBorder(new EmptyBorder(5, 0, 10, 0));
                
                String action = r.getEstado() == EstadoRegistro.ACTIVO ? "Ingreso Placa " : "Salida/Anulado Placa ";
                JLabel l1 = new JLabel("<html><b>" + action + r.getVehiculo().getPlaca() + "</b></html>");
                l1.setFont(new Font("SansSerif", Font.PLAIN, 12));
                
                String timeStr = sdfAct.format(new java.util.Date(r.getEstado() == EstadoRegistro.ACTIVO ? r.getHoraEntrada() : r.getHoraSalida()));
                JLabel l2 = new JLabel("Hora: " + timeStr);
                l2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                l2.setForeground(Color.GRAY);
                
                p.add(l1);
                p.add(l2);
                recentActivityPanel.add(p);
                count++;
            }
            recentActivityPanel.revalidate();
            recentActivityPanel.repaint();
        }
    }
}