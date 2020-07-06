/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import backend.GestorArchivos;
import backend.Operador;
import com.itextpdf.text.DocumentException;
import conexion.Connect;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.WriteException;

/**
 *
 * @author Sebastian
 */
public class Tabla extends javax.swing.JFrame {

    Connect c = new Connect();
    private String entrada;
    private String desde;
    private String hasta;
    private String comuna;
    private GestorArchivos GA = new GestorArchivos();
    private Operador operador;

    /**
     * Creates new form Tabla
     */
    public Tabla(String entrada, String desde, String hasta, Operador operador) {
        initComponents();
        this.entrada = entrada;
        this.desde = desde;
        this.hasta = hasta;
        this.operador = operador;
        this.setVisible(true);
        cargarTabla();
    }

    public Tabla(String entrada, String desde, String hasta, Operador operador, String comuna) {
        initComponents();
        this.entrada = entrada;
        this.desde = desde;
        this.hasta = hasta;
        this.operador = operador;
        this.comuna = comuna;
        this.setVisible(true);
        cargarTabla();
    }

    public void cargarTabla() {
        if (entrada.equals("RankingComunas")) {
            RankingComunas();
        } else if (entrada.equals("RankingComunasFecha")) {
            RankingComunasFecha();
        } else if (entrada.equals("RankingSectores")) {
            RankingSectores();
        } else if (entrada.equals("RankingSectoresFecha")) {
            RankingSectoresFecha();
        } else if (entrada.equals("MostrarUsuariosJefeDeZona")) {
            MostrarUsuariosJefeDeZona();
        } else if (entrada.equals("MostrarUsuariosAdministradorGeneral")) {
            MostrarUsuariosAdministradorGeneral();
        } else if (entrada.equals("MostrarInstitucionesJefeDeZona")) {
            MostrarInstitucionesJefeDeZona();
        } else if (entrada.equals("MostrarInstitucionesAdministradorGeneral")) {
            MostrarInstitucionesAdministradorGeneral();
        } else if (entrada.equals("MostrarSectoresJefeDeZona")) {
            MostrarSectoresJefeDeZona();
        } else if (entrada.equals("MostrarSectoresAdministradorGeneral")) {
            MostrarSectoresAdministradorGeneral();
        } else if (entrada.equals("MostrarComunasJefeDeZona")) {
            MostrarComunasJefeDeZona();
        } else if (entrada.equals("MostrarComunasAdministradorGeneral")) {
            MostrarComunasAdministradorGeneral();
        } else if (entrada.equals("MostrarDelincuentes")) {
            MostrarDelincuentes();
        } else if (entrada.equals("MostrarDelitos")) {
            MostrarDelitos();
        } else if (entrada.equals("MostrarDelitosJefeDeZona")) {
            MostrarDelitos();
        } else if (entrada.equals("MostrarDelincuentesOrdenAlfabetico")) {
            MostrarDelincuentesOrgenAlfabetico();
        } else if (entrada.equals("MostrarDelincuentesPorDelitoCometido")) {
            MostrarDelincuentesPorDelitoCometido();
        } else if (entrada.equals("MostrarDelincuentesPorComunaResidencia")) {
            MostrarDelincuentesPorComunaResidencia();
        } else if (entrada.equals("MostrarDelincuentesPorUltimoLugarVisto")) {
            MostrarDelincuentesPorUltimoLugarVisto();
        } else if (entrada.equals("MostrarDelitosComuna")) {
            MostrarDelitosComuna();
        } else if (entrada.equals("MostrarDelitosComunaFechas")) {
            MostrarDelitosComunaFechas();
        } else if (entrada.equals("MostrarDelitosSector")) {
            MostrarDelitosSector();
        } else if (entrada.equals("MostrarDelitosSectorFechas")) {
            MostrarDelitosSectorFechas();
        } else if (entrada.equals("MostrarParentesco")) {
            MostrarParentesco();
        } else if (entrada.equals("MostrarControles")) {
            MostrarControles();
        }
    }

    public void RankingComunas() {
        c.cargarTopComunasTabla(Tabla, this.filtro.getText());
    }

    public void RankingComunasFecha() {
        c.cargarTopComunasTablaRangoFechas(Tabla, this.filtro.getText(), desde, hasta);
    }

    public void RankingSectores() {
        c.cargarTopSectoresTabla(Tabla, this.filtro.getText());
    }

    public void RankingSectoresFecha() {
        c.cargarTopSectoresTablaRangoFechas(Tabla, this.filtro.getText(), desde, hasta);
    }

    public void MostrarUsuariosJefeDeZona() {
        c.cargarTablaUsuariosJF(Tabla, this.filtro.getText(), operador.getZona());
    }

    public void MostrarUsuariosAdministradorGeneral() {
        c.cargarTablaUsuariosAdministradorGeneral(Tabla, this.filtro.getText());
    }

    public void MostrarInstitucionesAdministradorGeneral() {
        c.cargarTablaInstitucionesAdministradorGeneral(Tabla, this.filtro.getText());
    }

    public void MostrarInstitucionesJefeDeZona() {
        c.cargarTablaInstitucionesJF(Tabla, this.filtro.getText(), operador.getZona());
    }

    public void MostrarSectoresAdministradorGeneral() {
        c.cargarTablaSectoresAdministradorGeneral(Tabla, this.filtro.getText());
    }

    public void MostrarSectoresJefeDeZona() {
        c.cargarTablaSectoresJF(Tabla, this.filtro.getText(), operador.getZona());
    }

    public void MostrarComunasAdministradorGeneral() {
        c.cargarTablaComunasAdministradorGeneral(Tabla, this.filtro.getText());
    }

    public void MostrarComunasJefeDeZona() {
        c.cargarTablaComunasJF(Tabla, this.filtro.getText(), operador.getZona());
    }

    public void MostrarDelincuentes() {
        c.cargarTablaDelincuentes(Tabla, this.filtro.getText());
    }

    public void MostrarDelitos() {
        c.cargarTablaDelitos(Tabla, this.filtro.getText());
    }

    public void MostrarDelitosJefeDeZona() {
        c.cargarTablaDelitosJefeDeZona(Tabla, this.filtro.getText(),operador.getZona());
    }

    public void MostrarDelincuentesOrgenAlfabetico() {
        c.cargarTablaDelincuentesOrdenAlfabetico(Tabla, this.filtro.getText());
    }

    public void MostrarDelincuentesPorDelitoCometido() {
        c.cargarTablaDelincuentesPorDelitoCometido(Tabla, this.filtro.getText());
    }

    public void MostrarDelincuentesPorComunaResidencia() {
        c.cargarTablaDelincuentesPorComunaResidencia(Tabla, this.filtro.getText());
    }

    public void MostrarDelincuentesPorUltimoLugarVisto() {
        c.cargarTablaDelincuentesPorUltimoLugarVisto(Tabla, this.filtro.getText());
    }

    public void MostrarDelitosComuna() {
        c.cargarTablaDelitosComuna(Tabla, comuna, this.filtro.getText());
    }

    public void MostrarDelitosComunaFechas() {
        c.cargarTablaDelitosComuna(Tabla, comuna, this.filtro.getText(), desde, hasta);
    }

    public void MostrarDelitosSector() {
        c.cargarTablaDelitosSector(Tabla, comuna, this.filtro.getText());
    }

    public void MostrarDelitosSectorFechas() {
        c.cargarTablaDelitosSector(Tabla, comuna, this.filtro.getText(), desde, hasta);
    }

    public void MostrarParentesco() {
        c.cargarTablaParentesco(Tabla, this.filtro.getText());
    }

    public void MostrarControles() {
        c.cargarTablaControles(Tabla, this.filtro.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        filtro = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setText("Generar Excel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(Tabla);

        jButton5.setText("Buscar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Actualizar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton3.setText("Generar PDF");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 368, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(85, 85, 85)
                                .addComponent(jButton3)
                                .addGap(250, 250, 250))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton6)
                                .addGap(46, 46, 46)
                                .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jButton5)
                                .addGap(370, 370, 370))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        cargarTabla();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // TODO add your handling code here:
            GA.crearReporteExcel(Tabla);
        } catch (IOException ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        try {
            // TODO add your handling code here:
            GA.crearReportePDF(Tabla);
        } catch (IOException ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:.
        this.filtro.setText("");
        cargarTabla();
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JTextField filtro;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
