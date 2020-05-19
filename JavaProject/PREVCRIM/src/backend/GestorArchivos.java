/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import conexion.Connect;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private Connect c= new Connect();
    

    public static String ElegirRuta() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setApproveButtonText("Guardar");
        fc.setDialogTitle("Guardar");
        int opcion = fc.showOpenDialog(null);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath()+".xls";
        }
        System.out.println("error");
        return "error";
    }

    public  void CrearReporterOdenadosAlfabeticamente() throws IOException, WriteException {
        // crea archivo excel como nombre la fecha actual
        String ruta=ElegirRuta();
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
        hoja1.addCell(new jxl.write.Label(0, 0, "WWW.GITHUB.COM/HACEFRIO/TESIS", titleformat1));

        //estadisticas
        hoja1.addCell(new jxl.write.Label(0, 2, "PREVCRIM", titleformat1));
        hoja1.addCell(new jxl.write.Label(0, 4, "Total delincuentes:", titleformat1));
        hoja1.addCell(new jxl.write.Label(1, 4, Integer.toString(c.contarDelincuentes()), titleformat1));
      //tabla

        hoja1.addCell(new jxl.write.Label(0, 10, "RUT"));
        hoja1.addCell(new jxl.write.Label(1, 10, "APELLIDOS"));
        hoja1.addCell(new jxl.write.Label(2, 10, "NOMBRES"));
        hoja1.addCell(new jxl.write.Label(3, 10, "APODO"));
        hoja1.addCell(new jxl.write.Label(4, 10, "DOMICILIO"));
        hoja1.addCell(new jxl.write.Label(5, 10, "ULTIMO LUGAR VISTO"));
        hoja1.addCell(new jxl.write.Label(6, 10, "TELEFONO HOGAR"));
        hoja1.addCell(new jxl.write.Label(7, 10, "TELEFONO CELULAR"));
        hoja1.addCell(new jxl.write.Label(8, 10, "EMAIL"));
        hoja1.addCell(new jxl.write.Label(9, 10, "FECHA NACIMIENTO"));
        hoja1.addCell(new jxl.write.Label(10, 10, "ESTADO"));
        hoja1.addCell(new jxl.write.Label(11, 10, "COMUNA"));
        c.DelintuentesToExcel(hoja1);
        
        woorBook.setProtected(true);
        woorBook.write();
        woorBook.close();

    }

}
