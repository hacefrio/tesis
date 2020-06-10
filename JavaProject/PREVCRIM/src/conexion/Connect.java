/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import backend.Operador;
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import org.jfree.data.category.DefaultCategoryDataset;

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

    public void crearSector(String codigo, String nombre, String descripcion) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into sector values(" + codigo + ",'" + nombre + "', '" + descripcion + "' );";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Sector creado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarSector(String codigo) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM sector WHERE codigo = " + codigo + ";";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Sector eliminado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void editarSector(String codigo, String nombre, String descripcion) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "UPDATE sector SET nombre = '" + nombre + "' , descripcion='" + descripcion + "'  WHERE `codigo` = " + codigo + ";";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Comuna editada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void crearInstitucion(String codigo, String nombre, String sector) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into institucion values(" + codigo + ",'" + nombre + "', " + sector + " );";
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
        entrada.removeAllItems();
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
            String sql = "insert into comuna values(" + codigo + ",'" + nombre + "', " + sector + " );";
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

    public void eliminarComuna(String codigo) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM comuna WHERE codigo = " + codigo + ";";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Comuna Eliminada exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    public boolean comprobarDelincuente(String rut) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delincuente \n"
                    + "where delincuente.rut='" + rut + "';");
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

    public void crearDelincuente(String rut, String apellidos, String nombres, String apodo, String domicilio, String ultimoLugarVisto, String telefonoCasa, String telefonoCelular, String email, String fechaDeNacimiento, String estado, String comuna) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into delincuente values( '" + rut + "','" + apellidos + "','" + nombres + "','" + apodo + "','" + domicilio + "'," + ultimoLugarVisto + ",'" + telefonoCasa + "','" + telefonoCelular + "','" + email + "','" + fechaDeNacimiento + "','" + estado + "'," + comuna + ");";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Delincuente creado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String setDelincuentesDatos(String rut, JTextField apellidos, JTextField nombres, JTextField apodo, JTextField domicilio, JTextField ultimoLugarVisto, JTextField telefonoCasa, JTextField telefonoCelular, JTextField email, JDateChooser fechaDeNacimiento, JTextField comuna) throws ParseException {
        String estado = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delincuente where rut='" + rut + "';");
            if (rs.next()) {
                apellidos.setText(rs.getString(2));
                nombres.setText(rs.getString(3));
                apodo.setText(rs.getString(4));
                domicilio.setText(rs.getString(5));
                ultimoLugarVisto.setText(rs.getString(6));
                telefonoCasa.setText(rs.getString(7));
                telefonoCelular.setText(rs.getString(8));
                email.setText(rs.getString(9));
                String fecha = rs.getString(10);
                java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                fechaDeNacimiento.setDate(date2);
                comuna.setText(rs.getString(12));
                estado = rs.getString(11);
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return estado;
    }

    public void editarDelincuente(String rut, String apellidos, String nombres, String apodo, String domicilio, String ultimoLugarVisto, String telefonoCasa, String telefonoCelular, String email, String fechaDeNacimiento, String estado, String comuna) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "UPDATE delincuente SET apellidos = '" + apellidos + "' , nombres='" + nombres + "' , apodo = '" + apodo + "' , domicilio = '" + domicilio + "' , ultimoLugarVisto = " + ultimoLugarVisto + " , telefonoCasa = '" + telefonoCasa + "', telefonoCelular = '" + telefonoCelular + "', email = '" + email + "', fechaNacimiento = '" + fechaDeNacimiento + "', estado = '" + estado + "', comuna = " + comuna + " WHERE `rut` = '" + rut + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Delincuente editado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarDelincuente(String rut) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM delincuente WHERE rut = '" + rut + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "delincuente eliminado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void crearDelito(String codigo, String descripcion, String direccion, String fecha, String delincuente, String comuna) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "insert into delito values( '" + codigo + "','" + descripcion + "','" + direccion + "','" + fecha + "','" + delincuente + "'," + comuna + ");";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Delito creado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarDelito(String codigo, String descripcion, String direccion, String fecha, String delincuente, String comuna) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "UPDATE delito SET descripcion='" + descripcion + "' , direccion = '" + direccion + "',fecha = '" + fecha + "' delincuente = '" + delincuente + "', comuna =" + comuna + "  WHERE `codigo` = '" + codigo + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Delito editado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarDelito(String codigo) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            String sql = "DELETE FROM delito WHERE rut = '" + codigo + "';";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            conn.close();
            pstm.close();
            JOptionPane.showMessageDialog(null, "Delito eliminado exitosamente ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setDelitosDatos(String codigo, JTextField descripcion, JTextField direccion, JDateChooser dia, JTextField delincuente, JTextField comuna) throws ParseException {

        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito where codigo='" + codigo + "';");
            if (rs.next()) {
                descripcion.setText(rs.getString(2));
                direccion.setText(rs.getString(3));
                String fecha = rs.getString(4);
                java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                dia.setDate(date2);
                delincuente.setText(rs.getString(5));
                comuna.setText(rs.getString(6));
            }
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean comprobarDelito(String codigo) {
        String salida = "";
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito where codigo='" + codigo + "' ;");
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

    /*              METODOS RELACIONADOS CON RANKING DE COMUNAS         */
    public static void cargarTopComunasGrafico(DefaultCategoryDataset ds) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreComuna "
                    + "FROM (SELECT comuna.nombre as nombreComuna "
                    + "      FROM delito "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      GROUP BY delito.comuna ) AS Total;");

            while (rs.next()) {
                ds.addValue(rs.getInt(1), rs.getString(2), "");
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTopComunasTabla(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreComuna "
                    + "FROM (SELECT comuna.nombre as nombreComuna "
                    + "      FROM delito "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      GROUP BY delito.comuna ) AS Total;");

            modelo.addColumn("Total");
            modelo.addColumn("Cumuna");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void cargarTopComunasGraficoRangoFechas(DefaultCategoryDataset ds, String desde, String hasta) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreComuna "
                    + "FROM (SELECT comuna.nombre as nombreComuna "
                    + "      FROM delito  "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      where delito.fecha BETWEEN '" + desde + "' AND '" + hasta + "' "
                    + "      GROUP BY delito.comuna) AS Total;");

            while (rs.next()) {
                ds.addValue(rs.getInt(1), rs.getString(2), "");
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTopComunasTablaRangoFechas(JTable tabla, String filtro, String desde, String hasta) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreComuna "
                    + "FROM (SELECT comuna.nombre as nombreComuna "
                    + "      FROM delito  "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      where delito.fecha BETWEEN '" + desde + "' AND '" + hasta + "' "
                    + "      GROUP BY delito.comuna) AS Total;");

            modelo.addColumn("Total");
            modelo.addColumn("Cumuna");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);

                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*              FIN DE METODOS RELACIONADOS CON RANKING DE COMUNAS         */

 /*              METODOS RELACIONADOS CON RANKING DE SECTORES         */
    public static void cargarTopSectoresGrafico(DefaultCategoryDataset ds) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreSector "
                    + "FROM (SELECT sector.nombre as nombreSector "
                    + "      FROM delito "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      inner join sector  "
                    + "      on sector.codigo =comuna.sector "
                    + "      GROUP BY sector.nombre) AS Total;");

            while (rs.next()) {
                ds.addValue(rs.getInt(1), rs.getString(2), "");
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void cargarTopSectoresGraficoRangoFechas(DefaultCategoryDataset ds, String desde, String hasta) {
        try {
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreSector "
                    + "FROM (SELECT sector.nombre as nombreSector "
                    + "      FROM delito "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      inner join sector  "
                    + "      on sector.codigo =comuna.sector "
                    + "      where delito.fecha BETWEEN '" + desde + "' AND '" + hasta + "' "
                    + "      GROUP BY sector.nombre) AS Total;");

            while (rs.next()) {
                ds.addValue(rs.getInt(1), rs.getString(2), "");
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTopSectoresTabla(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreSector "
                    + "FROM (SELECT sector.nombre as nombreSector "
                    + "      FROM delito "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      inner join sector  "
                    + "      on sector.codigo =comuna.sector "
                    + "      GROUP BY sector.nombre) AS Total;");

            modelo.addColumn("Total");
            modelo.addColumn("Sector");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);

                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
                tabla.setModel(modelo);
            }
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTopSectoresTablaRangoFechas(JTable tabla, String filtro, String desde, String hasta) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) as total, nombreSector "
                    + "FROM (SELECT sector.nombre as nombreSector "
                    + "      FROM delito "
                    + "      inner join comuna "
                    + "      on comuna.codigo = delito.comuna "
                    + "      inner join sector  "
                    + "      on sector.codigo =comuna.sector "
                    + "      where delito.fecha BETWEEN '" + desde + "' AND '" + hasta + "' "
                    + "      GROUP BY sector.nombre) AS Total;");

            modelo.addColumn("Total");
            modelo.addColumn("Sector");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*              FIN DE METODOS RELACIONADOS CON RANKING DE SECTORES         */
 /*                    METODOS RELACIONADOS CON CRUD USUARIOS                  */
    public void cargarTablaUsuariosJF(JTable tabla, String filtro, String sector) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador "
                    + "inner join institucion  "
                    + "on institucion.codigo= operador.institucion "
                    + "inner join sector "
                    + "on sector.codigo= institucion.sector "
                    + "where sector.codigo=" + sector + ";");

            modelo.addColumn("rut");
            modelo.addColumn("nombre");
            modelo.addColumn("Apellidos");
            modelo.addColumn("rango");
            modelo.addColumn("institucion");
            modelo.addColumn("Sector");
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(3);
                fila[2] = rs.getString(4);
                fila[3] = rs.getString(5);
                fila[4] = rs.getString(8);
                fila[5] = rs.getString(11);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaUsuariosAdministradorGeneral(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from operador "
                    + "inner join institucion  "
                    + "on institucion.codigo= operador.institucion "
                    + "inner join sector "
                    + "on sector.codigo= institucion.sector ;");

            modelo.addColumn("rut");
            modelo.addColumn("nombre");
            modelo.addColumn("Apellidos");
            modelo.addColumn("rango");
            modelo.addColumn("institucion");
            modelo.addColumn("Sector");
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(3);
                fila[2] = rs.getString(4);
                fila[3] = rs.getString(5);
                fila[4] = rs.getString(8);
                fila[5] = rs.getString(11);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*      FIN METODOS RELACIONADOS CON CRUD USUARIOS*/
 /*                    METODOS RELACIONADOS CON CRUD INSTITUCIONES                  */
    public void cargarTablaInstitucionesJF(JTable tabla, String filtro, String sector) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from institucion "
                    + "inner join sector "
                    + "on institucion.sector=sector.codigo "
                    + "where institucion.sector=" + sector + ";");

            modelo.addColumn("codigo");
            modelo.addColumn("nombre");
            modelo.addColumn("sector");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(5);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaInstitucionesAdministradorGeneral(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from institucion "
                    + "inner join sector "
                    + "on institucion.sector=sector.codigo ;");

            modelo.addColumn("codigo");
            modelo.addColumn("nombre");
            modelo.addColumn("sector");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(5);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*      FIN METODOS RELACIONADOS CON CRUD INSTITUCIONES*/
 /*                    METODOS RELACIONADOS CON CRUD SECTORES                  */
    public void cargarTablaSectoresJF(JTable tabla, String filtro, String sector) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from sector "
                    + "where sector.codigo=" + sector + ";");

            modelo.addColumn("codigo");
            modelo.addColumn("nombre");
            modelo.addColumn("descripcion");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaSectoresAdministradorGeneral(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from sector;");

            modelo.addColumn("codigo");
            modelo.addColumn("nombre");
            modelo.addColumn("descripcion");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*      FIN METODOS RELACIONADOS CON CRUD SECTORES*/
 /*                    METODOS RELACIONADOS CON CRUD COMUNA                  */
    public void cargarTablaComunasJF(JTable tabla, String filtro, String sector) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from comuna"
                    + "inner join sector"
                    + "on sector.codigo= comuna.sector "
                    + "where sector.codigo=" + sector + ";");

            modelo.addColumn("codigo");
            modelo.addColumn("nombre");
            modelo.addColumn("sector");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(5);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaComunasAdministradorGeneral(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from comuna"
                    + "inner join sector"
                    + "on sector.codigo= comuna.sector ");

            modelo.addColumn("codigo");
            modelo.addColumn("nombre");
            modelo.addColumn("sector");
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(5);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*      FIN METODOS RELACIONADOS CON CRUD COMUNA*/
 /*       METODOS RELACIONADOS CON CRUD DELINCUENTES*/
    public void cargarTablaDelincuentes(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select* from delincuente inner join comuna on comuna.codigo=delincuente.comuna ");

            modelo.addColumn("rut");
            modelo.addColumn("apellidos");
            modelo.addColumn("nombres");
            modelo.addColumn("apodo");
            modelo.addColumn("domicilio");
            modelo.addColumn("ultimo lugar visto");
            modelo.addColumn("telefono hogar");
            modelo.addColumn("telefono celular");
            modelo.addColumn("correo");
            modelo.addColumn("fecha de nacimiento");
            modelo.addColumn("estado");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[12];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(6);
                fila[6] = rs.getString(7);
                fila[7] = rs.getString(8);
                fila[8] = rs.getString(9);
                fila[9] = rs.getString(10);
                fila[10] = rs.getString(11);
                fila[11] = rs.getString(14);

                if (fila[10].toString().equals("L")) {
                    fila[10] = "Libre";
                } else if (fila[10].toString().equals("P")) {
                    fila[10] = "Preso";
                } else {
                    fila[10] = "Orden de arresto";
                }
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro) || fila[6].toString().contains(filtro) || fila[7].toString().contains(filtro) || fila[8].toString().contains(filtro) || fila[9].toString().contains(filtro) || fila[10].toString().contains(filtro) || fila[11].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*      FIN METODOS RELACIONADOS CON CRUD DELINCUENTES*/
 /*       METODOS RELACIONADOS CON CRUD DELITOS*/
    public void cargarTablaDelitos(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito "
                    + "join comuna "
                    + "on comuna.codigo=delito.comuna ");

            modelo.addColumn("codigo");
            modelo.addColumn("descripcion");
            modelo.addColumn("direccion");
            modelo.addColumn("fecha");
            modelo.addColumn("delincuente");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(8);

                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*      FIN METODOS RELACIONADOS CON CRUD DELITOS*/
    public void cargarTablaDelincuentesOrdenAlfabetico(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select* from delincuente inner join comuna on comuna.codigo=delincuente.comuna order by apellidos desc");

            modelo.addColumn("rut");
            modelo.addColumn("apellidos");
            modelo.addColumn("nombres");
            modelo.addColumn("apodo");
            modelo.addColumn("domicilio");
            modelo.addColumn("ultimo lugar visto");
            modelo.addColumn("telefono hogar");
            modelo.addColumn("telefono celular");
            modelo.addColumn("correo");
            modelo.addColumn("fecha de nacimiento");
            modelo.addColumn("estado");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[12];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(6);
                fila[6] = rs.getString(7);
                fila[7] = rs.getString(8);
                fila[8] = rs.getString(9);
                fila[9] = rs.getString(10);
                fila[10] = rs.getString(11);
                fila[11] = rs.getString(14);

                if (fila[10].toString().equals("L")) {
                    fila[10] = "Libre";
                } else if (fila[10].toString().equals("P")) {
                    fila[10] = "Preso";
                } else {
                    fila[10] = "Orden de arresto";
                }
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro) || fila[6].toString().contains(filtro) || fila[7].toString().contains(filtro) || fila[8].toString().contains(filtro) || fila[9].toString().contains(filtro) || fila[10].toString().contains(filtro) || fila[11].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelincuentesPorDelitoCometido(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito "
                    + "inner join comuna "
                    + "on delito.comuna=comuna.codigo "
                    + "inner join delincuente "
                    + "on delito.delincuente=delincuente.rut "
                    + "order by delito.comuna desc ");

            modelo.addColumn("codigo");
            modelo.addColumn("descripcion");
            modelo.addColumn("direccion");
            modelo.addColumn("fecha");
            modelo.addColumn("rut");
            modelo.addColumn("nombre");
            modelo.addColumn("apellidos");
            modelo.addColumn("apodo");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[12];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(12);
                fila[6] = rs.getString(11);
                fila[7] = rs.getString(13);
                fila[8] = rs.getString(8);

                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro) || fila[6].toString().contains(filtro) || fila[7].toString().contains(filtro) || fila[8].toString().contains(filtro) || fila[9].toString().contains(filtro) || fila[10].toString().contains(filtro) || fila[11].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelincuentesPorComunaResidencia(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delincuente "
                    + "inner join comuna "
                    + "on delincuente.comuna=comuna.codigo "
                    + "GROUP BY comuna.nombre  desc");

            modelo.addColumn("rut");
            modelo.addColumn("apellidos");
            modelo.addColumn("nombre");
            modelo.addColumn("domicilio");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(5);
                fila[4] = rs.getString(14);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelincuentesPorUltimoLugarVisto(JTable tabla, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delincuente "
                    + "inner join comuna "
                    + "on delincuente.ultimoLugarVisto=comuna.codigo "
                    + "GROUP BY comuna.nombre  desc");

            modelo.addColumn("rut");
            modelo.addColumn("apellidos");
            modelo.addColumn("nombre");
            modelo.addColumn("domicilio");
            modelo.addColumn("Ultima comuna visto");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(5);
                fila[4] = rs.getString(14);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelitosComuna(JTable tabla, String entrada, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito  "
                    + "inner join comuna "
                    + "on comuna.codigo=delito.comuna  "
                    + "inner join delincuente  "
                    + "on delincuente.rut=delito.delincuente  "
                    + "where comuna.nombre='" + entrada + "';");

            modelo.addColumn("codigo");
            modelo.addColumn("descripcion");
            modelo.addColumn("direccion");
            modelo.addColumn("fecha");
            modelo.addColumn("rut delincuente");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(8);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelitosComuna(JTable tabla, String entrada, String filtro, String desde, String hasta) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito  "
                    + "inner join comuna "
                    + "on comuna.codigo=delito.comuna  "
                    + "inner join delincuente  "
                    + "on delincuente.rut=delito.delincuente  "
                    + "where comuna.nombre='" + entrada + "' and delito.fecha BETWEEN '" + desde + "' AND '" + hasta + "';");

            modelo.addColumn("codigo");
            modelo.addColumn("descripcion");
            modelo.addColumn("direccion");
            modelo.addColumn("fecha");
            modelo.addColumn("rut delincuente");
            modelo.addColumn("comuna");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(8);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelitosSector(JTable tabla, String entrada, String filtro) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito  "
                    + "inner join comuna "
                    + "on comuna.codigo=delito.comuna  "
                    + "inner join delincuente  "
                    + "on delincuente.rut=delito.delincuente  "
                    + "inner join sector "
                    + "on sector.codigo= comuna.sector "
                    + "where sector.nombre='" + entrada + "';");

            modelo.addColumn("codigo");
            modelo.addColumn("descripcion");
            modelo.addColumn("direccion");
            modelo.addColumn("fecha");
            modelo.addColumn("rut delincuente");
            modelo.addColumn("sector");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(10);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaDelitosSector(JTable tabla, String entrada, String filtro, String desde, String hasta) {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            Connect SQL = new Connect();
            Connection conn = SQL.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select * from delito  "
                    + "inner join comuna "
                    + "on comuna.codigo=delito.comuna  "
                    + "inner join delincuente  "
                    + "on delincuente.rut=delito.delincuente  "
                    + "inner join sector "
                    + "on sector.codigo=comuna.sector "
                    + "where sector.nombre='" + entrada + "' and delito.fecha BETWEEN '" + desde + "' AND '" + hasta + "';");

            modelo.addColumn("codigo");
            modelo.addColumn("descripcion");
            modelo.addColumn("direccion");
            modelo.addColumn("fecha");
            modelo.addColumn("rut delincuente");
            modelo.addColumn("sector");

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(10);
                if (filtro.isEmpty()) {
                    modelo.addRow(fila);
                } else if (fila[0].toString().contains(filtro) || fila[1].toString().contains(filtro) || fila[2].toString().contains(filtro) || fila[3].toString().contains(filtro) || fila[4].toString().contains(filtro) || fila[5].toString().contains(filtro)) {
                    modelo.addRow(fila);
                }
            }
            tabla.setModel(modelo);
            conn.close();
            conn.close();
            rs.close();
            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
