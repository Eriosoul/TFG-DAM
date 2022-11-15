package ConexionDB;

import ConexionDB.PerdidaConexi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Conexion {

    static Connection con = null;

    // conexion con nuestra base de datos
    public static Connection getConnection() {
        PerdidaConexi perd = new PerdidaConexi();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            System.out.println("conexion establecida");
        } catch (Exception e) {
            System.out.println("Ha ocurido un error");
            perd.perdidaConex();
            
        }
        return con;
    }
    public PreparedStatement prepareStatement(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
