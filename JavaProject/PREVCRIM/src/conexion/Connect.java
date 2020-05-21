/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import backend.Operador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

/**
 *
 * @author Sebastian
 */
public class Connect {

    private static String url = "jdbc:mysql://localhost:3306/tesis";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "root";
    private static String password = "";
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
                System.out.println("nice");
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println(ex.getMessage());
                System.out.println("Failed to create the database connection.");

            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return con;
    }

    public boolean logear(String rut, String clave) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador where rut='" + rut + "' and clave='" + clave + "';");
            if (rs.next()) {
                salida = rs.getString(1);
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
            if (salida != "") {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void loadInstituciones1(JComboBox entrada, Operador operador) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador \n"
                    + "join institucion \n"
                    + "on institucion.codigo = operador.institucion \n"
                    + "join sector \n"
                    + "on institucion.sector = sector.codigo \n"
                    + " where sector.codigo='" + operador.getZona() + "';");
            if (rs.next()) {
                entrada.addItem(rs.getString(6));
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadInstituciones2(JComboBox entrada) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from institucion ;");
            while (rs.next()) {
                entrada.addItem(rs.getString(1));
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setOperador(String rut, Operador operador) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador \n"
                    + "join institucion \n"
                    + "on institucion.codigo = operador.institucion \n"
                    + "join sector \n"
                    + "on institucion.sector = sector.codigo \n"
                    + " where rut='" + rut + "';");
            if (rs.next()) {
                operador.setNombre(rs.getString(3));
                operador.setApellidos(rs.getString(4));
                operador.setInstitucion(rs.getString(6));
                operador.setRango(rs.getString(5));
                operador.setZona(rs.getString(9));
                System.out.println(operador.getNombre() + "|" + operador.getApellidos()
                        + "|" + operador.getInstitucion() + "|" + operador.getRango() + "|" + operador.getZona());
                conn.close();
                rs.close();
                s.close();
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String comprobarUsuariosRutPermisos(String rut, Operador operador) {
        String salida = "nada";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Connection conn2 = SQL.getConnection();
            Statement s = conn.createStatement();
            Statement s2 = conn2.createStatement();
            ResultSet rs2 = s2.executeQuery("SELECT * FROM `operador` "
                    + "where operador.rut= '" + rut + "';");

            ResultSet rs = s.executeQuery("SELECT * FROM `operador`"
                    + "join institucion\n"
                    + "on institucion.codigo= operador.institucion\n"
                    + "JOIN sector \n"
                    + "on institucion.sector=sector.codigo \n"
                    + "where operador.rut ='" + rut + "' and  sector.codigo=" + operador.getZona() + ";");

            if (rs.next()) {

                if (operador.getRango().equals("JefeDeZona")) {
                    if (rs.getString(5).equals("Operador")) {
                        System.out.println("si puede editar a un operador de su zona");
                        salida = "si";

                    } else {
                        System.out.println("no tiene permiso para esto");
                        salida = "noPermisos";
                    }
                }
                rs2.next();
            } else {
                if (rs2.next()) {
                    System.out.println("si existe, pero en otra zona");
                    salida = "otraZona";
                } else {
                    System.out.println("Usuario inexistente");
                    salida = "noExiste";
                    System.out.println("no existe 1");
                }

            }

            if (operador.getRango().equals("AdministradorGeneral") && rs2.getString(1).isEmpty() == false) {
                salida = "si";
            } else if (operador.getRango().equals("AdministradorGeneral")) {
                System.out.println("no existe 2");
                salida = "noExiste";
            }
            rs2.close();
            conn.close();
            rs.close();
            s2.close();
            s.close();
            conn2.close();
        } catch (SQLException e) {
            System.out.println("error " + e.getMessage());
            return salida;
        }
        System.out.println("Salida del metodo comprobarUsuariosRutPermisos" + salida);
        return salida;
    }

    public void setOperador2(String rut, Operador operador) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador where rut='" + rut + "';");
            if (rs.next()) {
                operador.setContra(rs.getString(2));
                operador.setNombre(rs.getString(3));
                operador.setApellidos(rs.getString(4));
                operador.setInstitucion(rs.getString(6));
                operador.setRango(rs.getString(5));
                conn.close();
                rs.close();
                s.close();
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void crearOperador(String rut, String clave, String nombre, String apellido, String rango, String institucion) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into operador values( '" + rut + "','" + clave + "','" + nombre + "','" + apellido + "','" + rango + "'," + institucion + ");";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "usuario creado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void eliminarOperador(String rut) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM operador WHERE rut = '" + rut + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "usuario eliminado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void editarOperador(String rut, String clave, String nombre, String apellido, String rango, String institucion) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "UPDATE operador SET nombre = '" + nombre + "' , clave= '" + clave + "' , apellidos = '" + apellido + "' , rango = '" + rango + "' , institucion = " + institucion + " WHERE `rut` = '" + rut + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "usuario editado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean comprobarInstitucion(String codigo) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from institucion where codigo=" + codigo + " ;");
            if (rs.next()) {
                salida = rs.getString(1);
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
            if (salida != "") {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void crearInstitucion(String codigo, String nombre, String sector) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into institucion values(" + codigo + ",'" + nombre + "', sector=" + sector + " );";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Institucion creada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setInstitucionesDatos(String codigo, JTextField nombre, JComboBox lista) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from institucion where codigo=" + codigo + ";");
            if (rs.next()) {
                nombre.setText(rs.getString(2));
                lista.addItem(rs.getString(3));
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarInstitucion(String codigo) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM institucion WHERE codigo = " + codigo + ";";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Institucion eliminada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void editarInstitucion(String codigo, String nombre, String sector) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "UPDATE institucion SET nombre = '" + nombre + "' , sector=" + sector + "  WHERE `codigo` = " + codigo + ";";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Institucion editada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean comprobarSector(String codigo) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from sector where codigo=" + codigo + " ;");
            if (rs.next()) {
                salida = rs.getString(1);
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
            if (salida != "") {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void setSectores(String codigo, JTextField nombre, JTextField descripcion) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from sector where codigo='" + codigo + "';");
            if (rs.next()) {
                nombre.setText(rs.getString(2));
                descripcion.setText(rs.getString(3));
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean comprobarComuna(String codigo) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from comuna where comuna.codigo=" + codigo + " ;");
            if (rs.next()) {
                salida = rs.getString(1);
                System.out.println(rs.getString(1));
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
            if (salida != "") {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public boolean comprobarComuna(String comuna, String sector) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from comuna \n"
                    + "join sector \n"
                    + "on sector.codigo= comuna.sector \n"
                    + "where comuna.codigo=" + comuna + " and sector.codigo=" + sector + ";");
            if (rs.next()) {
                salida = rs.getString(1);
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
            if (salida != "") {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void loadSectores(JComboBox entrada) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from sector ;");
            while (rs.next()) {
                entrada.addItem(rs.getString(1));
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void crearComuna(String codigo, String nombre, String sector) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into comuna values(" + codigo + ",'" + nombre + "', sector=" + sector + " );";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Comuna creada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarComuna(String codigo, String nombre, String sector) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "UPDATE comuna SET nombre = '" + nombre + "' , sector=" + sector + "  WHERE `codigo` = " + codigo + ";";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Comuna editada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setComunaDatos(String codigo, JTextField nombre, JComboBox lista) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from comuna where codigo=" + codigo + ";");
            if (rs.next()) {
                nombre.setText(rs.getString(2));
                lista.addItem(rs.getString(3));
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int contarDelincuentes() {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select count(*) from delincuente  ;");
            if (rs.next()) {
                salida = rs.getString(1);
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
            return Integer.parseInt(salida);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }

    public void eliminarComuna(String codigo) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM comuna WHERE codigo = '" + codigo + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Comuna Eliminada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public void DelintuentesToExcel(WritableSheet hoja1) throws WriteException {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delincuente ORDER BY apellidos DESC;");
            int i = 11;
            while (rs.next()) {
                hoja1.addCell(new jxl.write.Label(0, i, rs.getString(1)));
                hoja1.addCell(new jxl.write.Label(1, i, rs.getString(2)));
                hoja1.addCell(new jxl.write.Label(2, i, rs.getString(3)));
                hoja1.addCell(new jxl.write.Label(3, i, rs.getString(4)));
                hoja1.addCell(new jxl.write.Label(4, i, rs.getString(5)));
                hoja1.addCell(new jxl.write.Label(5, i, rs.getString(6)));
                hoja1.addCell(new jxl.write.Label(6, i, rs.getString(7)));
                hoja1.addCell(new jxl.write.Label(7, i, rs.getString(8)));
                hoja1.addCell(new jxl.write.Label(8, i, rs.getString(9)));
                hoja1.addCell(new jxl.write.Label(9, i, rs.getString(10)));
                hoja1.addCell(new jxl.write.Label(10, i, rs.getString(11)));
                hoja1.addCell(new jxl.write.Label(11, i, rs.getString(12)));
                i++;
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
