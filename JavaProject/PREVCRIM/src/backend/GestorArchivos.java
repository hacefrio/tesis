/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import conexion.Connect;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Sebastian
 */
public class GestorArchivos {

    private Connect c = new Connect();

    public static String ElegirRutaExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setApproveButtonText("Guardar");
        fc.setDialogTitle("Guardar");
        int opcion = fc.showOpenDialog(null);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath() + ".xls";
        }
        System.out.println("error");
        return "error";
    }

    public static String ElegirRutaPDF() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setApproveButtonText("Guardar");
        fc.setDialogTitle("Guardar");
        int opcion = fc.showOpenDialog(null);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath() + ".pdf";
        }
        System.out.println("error");
        return "error";
    }

    public void crearReporteExcel(JTable tabla) throws IOException, WriteException {
        // crea archivo excel como nombre la fecha actual
        String ruta = ElegirRutaExcel();
        File excel = new File(ruta);

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        WritableWorkbook woorBook = Workbook.createWorkbook(new File(ruta), conf);
        WritableSheet hoja1 = woorBook.createSheet("PREVCRIM", 0);

        //cabezera 
        WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, true);
        WritableCellFormat titleformat = new WritableCellFormat(titleFont);
        WritableCellFormat titleformat1 = new WritableCellFormat(titleFont);
        titleformat.setBackground(Colour.YELLOW);
        titleformat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
        titleformat1.setBorder(Border.ALL, BorderLineStyle.MEDIUM);

        //hoja 1
        //estadisticas
        hoja1.addCell(new jxl.write.Label(0, 2, "PREVCRIM", titleformat1));
        //tabla
        DefaultTableModel tm = (DefaultTableModel) tabla.getModel();
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            hoja1.addCell(new jxl.write.Label(i, 10, tabla.getColumnName(i)));
        }
        int valor1 = 0, valor2 = 0;
        for (int i = 11; i < tabla.getRowCount() + 11; i++) {
            for (int j = 0; j < tabla.getColumnCount(); j++) {
                System.out.println("i: " + i + " J:" + j + " valor1: " + valor1 + " valor2: " + valor2);
                hoja1.addCell(new jxl.write.Label(j, i, String.valueOf(tm.getValueAt(valor1, valor2))));
                valor2++;
            }
            valor1++;
            valor2 = 0;
        }
        woorBook.setProtected(true);
        woorBook.write();
        woorBook.close();
    }

    public void crearReportePDF(JTable tabla) throws IOException, DocumentException {
        Document documento = new Document();

        FileOutputStream ficheroPdf = new FileOutputStream(ElegirRutaPDF());

        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(1);

        documento.open();
        try {
            Image foto = Image.getInstance("src/img/PREVCRIMLOGO.png");
            foto.scaleToFit(100, 100);
            foto.setAlignment(Chunk.ALIGN_RIGHT);
            documento.add(foto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        documento.add(new Paragraph("Archivo generador automaticamente por sistema PREVCRIM",
                FontFactory.getFont("arial", // fuente
                        14, // tamaño
                        Font.ITALIC, // estilo
                        BaseColor.BLACK)));             // color
        documento.add(new Paragraph("    ",
                FontFactory.getFont("arial", // fuente
                        20, // tamaño
                        Font.ITALIC, // estilo
                        BaseColor.BLACK)));
        PdfPTable tablaPDF = new PdfPTable(tabla.getColumnCount());
        DefaultTableModel tm = (DefaultTableModel) tabla.getModel();
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tablaPDF.addCell(tabla.getColumnName(i));
        }
        for (int i = 0; i < tabla.getRowCount(); i++) {
            for (int j = 0; j < tabla.getColumnCount(); j++) {
                tablaPDF.addCell(String.valueOf(tm.getValueAt(i, j)));
            }
        }

        documento.add(tablaPDF);
        documento.close();
    }
}
