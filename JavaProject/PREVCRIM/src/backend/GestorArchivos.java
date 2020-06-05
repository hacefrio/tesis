/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import conexion.Connect;
import java.io.File;
import java.io.IOException;
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

    public static String ElegirRuta() {
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

    public void crearReporteExcel(JTable tabla) throws IOException, WriteException {
        // crea archivo excel como nombre la fecha actual
        String ruta = ElegirRuta();
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
                hoja1.addCell(new jxl.write.Label(j, i, String.valueOf(tm.getValueAt(valor1, valor2))));
                valor2++;
            }
            valor1++;
        }

        woorBook.setProtected(true);
        woorBook.write();
        woorBook.close();

    }

}
