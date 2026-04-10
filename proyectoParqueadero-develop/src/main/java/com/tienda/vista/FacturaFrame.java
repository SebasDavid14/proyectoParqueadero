package com.tienda.vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.tienda.modelo.Registro;
import com.tienda.util.PdfGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FacturaFrame extends JFrame {

    public FacturaFrame(Registro r) {
        setTitle("Recibo de Caja");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JLabel lblHeader = new JLabel("PARKING PRO");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setForeground(new Color(12, 27, 110));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Recibo de Caja Oficial");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        long minutos = (r.getTotal() == 0) ? 0 : (r.getHoraSalida() - r.getHoraEntrada()) / 60000;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        // Detalles
        JPanel detailsPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(10, 0, 10, 0)
        ));
        
        detailsPanel.add(new JLabel("<html><b>Placa:</b> " + r.getVehiculo().getPlaca() + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Tipo:</b> " + r.getVehiculo().getTipo() + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Zona Asignada:</b> " + r.getEspacio().getNumero() + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Entrada:</b> " + sdf.format(new Date(r.getHoraEntrada())) + "</html>"));
        
        String salidaText = r.getHoraSalida() > 0 ? sdf.format(new Date(r.getHoraSalida())) : "N/A";
        detailsPanel.add(new JLabel("<html><b>Salida:</b> " + salidaText + "</html>"));
        detailsPanel.add(new JLabel("<html><b>Tiempo:</b> " + minutos + " minutos</html>"));

        // Total
        JLabel lblTotal = new JLabel("TOTAL: " + formatter.format(r.getTotal()));
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTotal.setForeground(new Color(46, 125, 50));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        JButton btnExport = new JButton("Exportar a PDF");
        btnExport.putClientProperty(FlatClientProperties.STYLE, "background: #0C1B6E; foreground: #FFFFFF; font: bold; arc: 10; margin: 10,0,10,0");
        btnExport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnExport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar Factura " + r.getVehiculo().getPlaca());
            chooser.setSelectedFile(new File("Factura_" + r.getVehiculo().getPlaca() + ".pdf"));
            
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    String ruta = chooser.getSelectedFile().getAbsolutePath();
                    if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";
                    
                    PdfGenerator.generarFacturaPdf(r, ruta);
                    JOptionPane.showMessageDialog(this, "Factura guardada correctamente en:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar el PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(lblHeader);
        panel.add(lblSub);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(detailsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblTotal);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnExport);

        add(panel);
    }
}
