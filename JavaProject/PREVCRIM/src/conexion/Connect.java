/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import backend.Operador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void setOperador(String rut,Operador operador) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador where rut='" + rut + "';");
            if (rs.next()) {
                operador.setNombre(rs.getString(3));
                operador.setApellidos(rs.getString(4));
                operador.setInstitucion(rs.getString(5));
                operador.setRango(rs.getString(6));
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

}
