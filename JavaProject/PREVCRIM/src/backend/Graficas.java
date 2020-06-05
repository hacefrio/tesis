/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import conexion.Connect;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Sebastian
 */
public class Graficas {

    /*              METODOS RELACIONADOS CON RANKING DE COMUNAS         */
    public void MostrarTopComunaHistorico() {
        try {
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            Connect.cargarTopComunasGrafico(ds);
            JFreeChart jf = ChartFactory.createBarChart("Top comunas con mas delitos", "Comunas",
                    "Delitos", ds, PlotOrientation.VERTICAL, true, true, true);
            ChartFrame f = new ChartFrame("Top comunas con mas delitos ", jf);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void MostrarTopComunaRangoFechas(String desde, String hasta) {
        try {
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            Connect.cargarTopComunasGraficoRangoFechas(ds, desde, hasta);
            JFreeChart jf = ChartFactory.createBarChart("Top comunas con mas delitos desde " + desde + " hasta " + hasta, "Comunas",
                    "Delitos", ds, PlotOrientation.VERTICAL, true, true, true);
            ChartFrame f = new ChartFrame("Top comunas con mas delitos desde " + desde + " hasta " + hasta, jf);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No hay delitos cometidos en ese rango de fechas");
            System.out.println(ex.getMessage());
        }
    }

    /*             FIN DE METODOS RELACIONADOS CON RANKING DE COMUNAS         */
    
    /*              METODOS RELACIONADOS CON RANKING DE SECTORES         */
    public void MostrarTopSectoresHistorico() {
        try {
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            Connect.cargarTopSectoresGrafico(ds);
            JFreeChart jf = ChartFactory.createBarChart("Top sectores con mas delitos", "Sectores",
                    "Delitos", ds, PlotOrientation.VERTICAL, true, true, true);
            ChartFrame f = new ChartFrame("Top sectores con mas delitos ", jf);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void MostrarTopSectoresRangoFechas(String desde, String hasta) {
        try {
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            Connect.cargarTopSectoresGraficoRangoFechas(ds, desde, hasta);
            JFreeChart jf = ChartFactory.createBarChart("Top sectores con mas delitos desde " + desde + " hasta " + hasta, "Sectores",
                    "Delitos", ds, PlotOrientation.VERTICAL, true, true, true);
            ChartFrame f = new ChartFrame("Top sectores con mas delitos desde " + desde + " hasta " + hasta, jf);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No hay delitos cometidos en ese rango de fechas");
            System.out.println(ex.getMessage());
        }
    }
    /*             FIN DE METODOS RELACIONADOS CON RANKING DE SECTORES         */
}
