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
                    + "on sector.institucion = institucion.codigo \n"
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
                    + "on sector.institucion = institucion.codigo \n"
                    + " where rut='" + rut + "';");
            if (rs.next()) {
                operador.setNombre(rs.getString(3));
                operador.setApellidos(rs.getString(4));
                operador.setInstitucion(rs.getString(6));
                operador.setRango(rs.getString(5));
                operador.setZona(rs.getString(9));
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
            Statement s = conn.createStatement();
            Statement s2 = conn.createStatement();
            ResultSet rs2 = s2.executeQuery("SELECT * FROM `operador` "
                    + "where operador.rut= '" + rut + "'");

            ResultSet rs = s.executeQuery("SELECT * FROM `operador`"
                    + "join institucion\n"
                    + "on institucion.codigo= operador.institucion\n"
                    + "JOIN sector \n"
                    + "on sector.institucion=institucion.codigo \n"
                    + "where operador.rut ='" + rut + "' and  sector.codigo=" + operador.getZona() + ";");

            if (operador.getRango().equals("AdministradorGeneral") && rs2.next()) {
                return "si";
            }else if(operador.getRango().equals("AdministradorGeneral")) {
                return "noExiste";
            }
            if (rs.next()) {
                if (operador.getRango().equals("JefeDeZona")) {
                    if (rs.getString(5).equals("Operador")) {
                        System.out.println("si puede editar a un operador de su zona");
                        return "si";

                    } else {
                        System.out.println("no tiene permiso para esto");
                        return "noPermisos";
                    }
                }
            } else {

                if (rs2.next()) {
                    System.out.println("si existe, pero en otra zona");
                    return "otraZona";
                } else {
                    System.out.println("Usuario inexistente");
                    return "noExiste";
                }

            }
            rs2.close();
            conn.close();
            rs.close();
            s2.close();
            s.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("error ");
            return salida;
        }
        System.out.println("no entró en un if");
        return salida;
    }

    public void setOperador2(String rut, Operador operador) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador where rut='"+rut+"';");
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

    public void crearOperador(String rut,String clave,String nombre, String apellido, String rango, String institucion) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into operador values( '"+rut+"','"+clave+"','"+nombre+"','"+apellido+"','"+rango+"',"+institucion+");";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "usuario creado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    }
}
