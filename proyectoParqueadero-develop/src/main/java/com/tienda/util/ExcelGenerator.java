package com.tienda.util;

import com.tienda.enums.EstadoRegistro;
import com.tienda.modelo.Registro;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelGenerator {

    public static void exportarReporteExcel(List<Registro> registros, String rutaDestino) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        // 1. Clasificar y calcular información
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        String todayStr = dayFormat.format(new Date());

        List<Registro> diario = new ArrayList<>();
        double gananciasHoy = 0;
        double gananciasHist = 0;
        int ingresosHoy = 0;
        int salidasHoy = 0;
        int vehiculosActivos = 0;

        for (Registro r : registros) {
            String fechaEntradaStr = dayFormat.format(new Date(r.getHoraEntrada()));
            String fechaSalidaStr = r.getHoraSalida() > 0 ? dayFormat.format(new Date(r.getHoraSalida())) : "";
            
            if (r.getEstado() == EstadoRegistro.FINALIZADO) {
                gananciasHist += r.getTotal();
            } else if (r.getEstado() == EstadoRegistro.ACTIVO) {
                vehiculosActivos++;
            }

            boolean isHoy = fechaEntradaStr.equals(todayStr) || fechaSalidaStr.equals(todayStr);
            if (isHoy) {
                diario.add(r);
                if (fechaEntradaStr.equals(todayStr)) {
                    ingresosHoy++;
                }
                if (fechaSalidaStr.equals(todayStr)) {
                    salidasHoy++;
                    if (r.getEstado() == EstadoRegistro.FINALIZADO || r.getEstado() == EstadoRegistro.ACTIVO) {
                        gananciasHoy += r.getTotal();
                    }
                }
            }
        }

        // Estilos
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);

        // 2. Crear Hoja 1: Resumen General
        crearHojaResumen(workbook, titleStyle, currencyStyle, vehiculosActivos, ingresosHoy, salidasHoy, gananciasHoy, gananciasHist);

        // 3. Crear Hoja 2: Diario
        crearHojaListado(workbook, "Diario", diario, headerStyle, currencyStyle);

        // 4. Crear Hoja 3: Histórico
        crearHojaListado(workbook, "Histórico", registros, headerStyle, currencyStyle);

        // Escribir archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaDestino)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    private static void crearHojaResumen(Workbook workbook, CellStyle titleStyle, CellStyle currencyStyle, 
                                         int vehiculosActivos, int ingresosHoy, int salidasHoy, 
                                         double gananciasHoy, double gananciasHist) {
        Sheet sheet = workbook.createSheet("Resumen General");
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 6000);

        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Resumen Cierre de Caja");
        titleCell.setCellStyle(titleStyle);

        SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Row dateRow = sheet.createRow(2);
        dateRow.createCell(0).setCellValue("Fecha de Emisión: ");
        dateRow.createCell(1).setCellValue(sdfFull.format(new Date()));

        int r = 4;
        
        // Stats
        Row r1 = sheet.createRow(r++);
        r1.createCell(0).setCellValue("Vehículos Actualmente Parqueados:");
        r1.createCell(1).setCellValue(vehiculosActivos);

        Row r2 = sheet.createRow(r++);
        r2.createCell(0).setCellValue("Total Vehículos que Ingresaron Hoy:");
        r2.createCell(1).setCellValue(ingresosHoy);

        Row r3 = sheet.createRow(r++);
        r3.createCell(0).setCellValue("Total Vehículos que Salieron Hoy:");
        r3.createCell(1).setCellValue(salidasHoy);

        r++; // espacio

        Row r4 = sheet.createRow(r++);
        r4.createCell(0).setCellValue("Ganancias del Día (Hoy):");
        Cell c4 = r4.createCell(1);
        c4.setCellValue(gananciasHoy);
        c4.setCellStyle(currencyStyle);

        Row r5 = sheet.createRow(r++);
        r5.createCell(0).setCellValue("Ganancias Históricas Totales:");
        Cell c5 = r5.createCell(1);
        c5.setCellValue(gananciasHist);
        c5.setCellStyle(currencyStyle);
    }

    private static void crearHojaListado(Workbook workbook, String nombreHoja, List<Registro> lista, CellStyle headerStyle, CellStyle currencyStyle) {
        Sheet sheet = workbook.createSheet(nombreHoja);
        
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Placa", "Tipo Vehículo", "Zona", "Hora Entrada", "Hora Salida", "Tiempo (min)", "Estado", "Total Cobrado"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        int rowNum = 1;

        for (Registro r : lista) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(r.getVehiculo().getPlaca());
            row.createCell(1).setCellValue(r.getVehiculo().getTipo().name());
            row.createCell(2).setCellValue(r.getEspacio().getNumero());

            row.createCell(3).setCellValue(sdf.format(new Date(r.getHoraEntrada())));

            if (r.getHoraSalida() > 0) {
                row.createCell(4).setCellValue(sdf.format(new Date(r.getHoraSalida())));
            } else {
                row.createCell(4).setCellValue("EN CURSO");
            }

            long minutos = (r.getHoraSalida() == 0) ? (System.currentTimeMillis() - r.getHoraEntrada()) / 60000 : (r.getHoraSalida() - r.getHoraEntrada()) / 60000;
            row.createCell(5).setCellValue(minutos);

            row.createCell(6).setCellValue(r.getEstado().name());

            Cell totalCell = row.createCell(7);
            totalCell.setCellValue(r.getTotal());
            totalCell.setCellStyle(currencyStyle);
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        return headerStyle;
    }

    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle currencyStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("$#,##0.00"));
        return currencyStyle;
    }

    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        return style;
    }
}
