package com.tienda.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.tienda.modelo.Registro;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PdfGenerator {

    public static void generarFacturaPdf(Registro r, String rutaDestino) throws Exception {
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, new FileOutputStream(rutaDestino));
        
        document.open();
        
        // Fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(12, 27, 110));
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new BaseColor(46, 125, 50));

        // Header
        Paragraph title = new Paragraph("PARKING PRO", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        Paragraph subTitle = new Paragraph("Recibo de Caja", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY));
        subTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subTitle);
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph("------------------------------------------------------"));
        document.add(new Paragraph(" "));
        
        // Content
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        long minutos = (r.getTotal() == 0) ? 0 : (r.getHoraSalida() - r.getHoraEntrada()) / 60000;
        
        document.add(new Paragraph("Placa: " + r.getVehiculo().getPlaca(), headerFont));
        document.add(new Paragraph("Tipo de Vehículo: " + r.getVehiculo().getTipo().name(), normalFont));
        document.add(new Paragraph("Espacio: " + r.getEspacio().getNumero(), normalFont));
        
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("Hora Entrada: " + sdf.format(new Date(r.getHoraEntrada())), normalFont));
        if (r.getHoraSalida() > 0) {
            document.add(new Paragraph("Hora Salida: " + sdf.format(new Date(r.getHoraSalida())), normalFont));
        }
        document.add(new Paragraph("Tiempo consumido: " + minutos + " minutos", normalFont));
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph("------------------------------------------------------"));
        document.add(new Paragraph(" "));
        
        // Total
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        Paragraph total = new Paragraph("TOTAL PAGADO: " + formatter.format(r.getTotal()), totalFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Visite administracion para más información.\n¡Gracias por su visita!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }
}
