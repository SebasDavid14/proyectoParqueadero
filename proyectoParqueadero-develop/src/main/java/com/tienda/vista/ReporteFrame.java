package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.AppContext;
import com.tienda.util.ExcelGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

public class ReporteFrame extends JFrame {

    public ReporteFrame() {
        setTitle("Módulo de Reportes");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(245, 246, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Centro de Reportes");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(12, 27, 110));
        JLabel subtitle = new JLabel("Genera consolidados de actividad y recaudos en formato Excel.");
        subtitle.setForeground(Color.GRAY);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Cards Grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);
        
        long totalEntradas = AppContext.service.listar().size();
        long totalSalidas = AppContext.service.totalSalidas();
        long activos = AppContext.service.ocupados();
        double recaudoTotal = AppContext.service.totalIngresos();
        NumberFormat curFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        gridPanel.add(createCard("TOTAL REGISTROS", String.valueOf(totalEntradas), "Vehículos que han ingresado históricamente.", new Color(12, 27, 110)));
        gridPanel.add(createCard("TOTAL SALIDAS", String.valueOf(totalSalidas), "Vehículos con servicio finalizado.", new Color(46, 125, 50)));
        gridPanel.add(createCard("VEHÍCULOS ACTIVOS", String.valueOf(activos), "Actualmente en el parqueadero.", new Color(245, 166, 35)));
        gridPanel.add(createCard("RECAUDO TOTAL", curFormat.format(recaudoTotal), "Suma de todos los cobros finalizados.", new Color(12, 27, 110)));

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        
        JButton btnExport = new JButton(" Exportar a Excel");
        btnExport.putClientProperty(FlatClientProperties.STYLE, "background: #1D6F42; foreground: #FFFFFF; font: bold; arc: 10; margin: 10,25,10,25");
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnExport.addActionListener(e -> exportarExcel());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.putClientProperty(FlatClientProperties.STYLE, "background: #E0E0E0; foreground: #333333; font: bold; arc: 10; margin: 10,25,10,25");
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        
        footerPanel.add(btnCerrar);
        footerPanel.add(btnExport);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void exportarExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar Reporte Excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        
        int userSelection = chooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }
            
            try {
                ExcelGenerator.exportarReporteExcel(AppContext.service.listar(), path);
                JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente a:\n" + path, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al exportar a Excel:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createCard(String title, String value, String sub, Color markerColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, markerColor),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblValue.setForeground(new Color(40, 40, 40));

        JLabel lblSub = new JLabel("<html><div style='width:200px;'>" + sub + "</div></html>");
        lblSub.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblSub.setForeground(Color.GRAY);

        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        p.add(lblSub, BorderLayout.SOUTH);

        return p;
    }
}
