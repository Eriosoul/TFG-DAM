package Modelo;

import ConexionDB.Conexion;
import Vista.Login;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class SQLConsultas extends Conexion {

    public boolean registrarUser(Modelo.MetodoUsuario usr) {
        // creamos un tipo bollean para registrar nuestro usuario
        PreparedStatement ps = null;
        try {
            // realizamos la conexion 
            Connection con = getConnection();
            // realizamos la consulta -- insertar datos en la tabla de los usuarios donde nombre apellidos etc..
            String sql = "insert into user(nombre,apellidos,email,pass,telefono,direccioncasa,tipo) values(?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            // pasamos datos a la consulta llamamos la infomracion de nuestro metodo de usuarios
            ps.setString(1, usr.getNombre());
            ps.setString(2, usr.getApellidos());
            ps.setString(3, usr.getEmail());
            ps.setString(4, usr.getPass());
            ps.setString(5, usr.getTelefono());
            ps.setString(6, usr.getDireccioncasa());
            ps.setInt(7, usr.getTipo());
            // ejecutamos la sentencia
            ps.execute();
            // si nos devuelve verdero 
            return true;
        } catch (Exception e) {
            // nos devuelve falso
            return false;
        }
    }

    public int existeUser(String email) {
        // pasamos el email
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnection();
        // realizamos la consulta -- seleciona cuenta los id de los usuarios de la tabla usuarios donde el campo email sea igual al email introducido
        String sql = "select count(id_user) from user where email = ?";
        // si el contenido exite nos devolvera el contenido y nos indicara de que ese correo ya esta en uso.
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
    

    public boolean updateUserAcount(Modelo.MetodoUsuario usr) {
        // actualizacion de los datos del usuario
        Boolean isUpdated = false;
        Connection con = getConnection();
        
        try {
            // realizamos la consulta
            String sql = "update user set nombre= ?,apellidos= ?,email =?,pass =?,telefono= ?,direccioncasa=?, tipo=? where email= ?";
            System.out.println(sql);
            // le pasamos la consulta al prepareStatement 
            // cada pst se debera poner de la imisma forma que nuestra insercion, a eso le pasamos nuestro contenido del metodo usuario, 
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, usr.getNombre());
            pst.setString(2, usr.getApellidos());
            pst.setString(3, usr.getEmail());
            pst.setString(4, usr.getPass());
            pst.setString(5, usr.getTelefono());
            pst.setString(6, usr.getDireccioncasa());
            pst.setInt(7, usr.getTipo());
            int rowCount = pst.executeUpdate();
            // si nuesttra variable rowCount es > 0 significa que hay datos introducidos ,
            if (rowCount > 0) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }
    
    
    public boolean registrarProducto(Modelo.Producto prod, String ruta) {
        // procedemos a realizar la insercion de los prodcutos al igual que hicimos con el usuario
        PreparedStatement ps = null;
        FileInputStream fi = null;
        try {
            File file = new File(ruta);
            fi = new FileInputStream(file);
            Connection con = getConnection();
            String sql = "insert into productos(nombreproducto,marcaproveedor,stockproducto,precioproducto,foto,descripcionproducto,tipocomponente) values(?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);         

            ps.setString(1, prod.getPnombreproducto());
            ps.setInt(2, prod.getMarcaproveedor());
            ps.setInt(3, prod.getStockproducto());
            ps.setInt(4, prod.getPprecioproducto());
            ps.setBinaryStream(5, fi);
            ps.setString(6, prod.getPdescripcionproducto());
            ps.setInt(7, prod.getPtipocomponente());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Error al guardar");
            return false;
        }
    }
    
    public boolean updateProducto(Modelo.Producto prod) {
        // procedemos a realizar la insercion de los prodcutos al igual que hicimos con el usuario
        Boolean isUpdated = false;
        PreparedStatement ps = null;
        FileInputStream fi = null;
        
        try {
            Connection con = getConnection();
            String sql = "update productos set nombreproducto=?,marcaproveedor=?,stockproducto=?,precioproducto=?,descripcionproducto=?,tipocomponente=? where id_producto=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, prod.getPnombreproducto());
            ps.setInt(2, prod.getMarcaproveedor());
            ps.setInt(3, prod.getStockproducto());
            ps.setInt(4, prod.getPprecioproducto());
            //ps.setBinaryStream(5, fi);
            ps.setString(5, prod.getPdescripcionproducto());
            ps.setInt(6, prod.getPtipocomponente());
           
            int rowCount = ps.executeUpdate();
            // si nuesttra variable rowCount es > 0 significa que hay datos introducidos ,
            if (rowCount > 0) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar");
            return false;
        }
        return false;
    }
    
    public void usuarioComprador(Modelo.MetodoUsuario usr){
        Login log = new Login();
        log.lbl_log_email.getText();
        PreparedStatement ps =null;
        ResultSet rs =null;
        try {
            Connection con = getConnection();
            String sql = "Select * from user where email=?";
            ps = con.prepareStatement(sql);
            
            rs = ps.executeQuery();
            if(rs.next()){
                rs.getInt("id_user");
            }                      
        } catch (Exception e) {
        }
    }
    
   public boolean comprobarLog(MetodoUsuario us){
       PreparedStatement ps = null;
       ResultSet rs = null;
       Connection con = Conexion.getConnection();
       String sql = "Select id_user, email, pass from user where email=?";
       try {
           ps = con.prepareStatement(sql);
           ps.setString(1, us.getEmail());
           rs= ps.executeQuery();
           if (rs.next()) {
               if (us.getPass().equals(rs.getString(3))) {
                   us.setId_user(rs.getInt(1));
                   return true;
               }else{
                   return false;
               }
           }
           return false;
       } catch (Exception e) {
           return false;
       }
   }

    public boolean registrarProducto(Producto modProd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
