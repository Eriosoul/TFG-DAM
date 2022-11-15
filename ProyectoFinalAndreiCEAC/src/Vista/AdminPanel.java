package Vista;

import ConexionDB.PerdidaConexi;
import Modelo.CifradoHash;
import ConexionDB.Conexion;
import Modelo.MetodoProveedor;
import Modelo.MetodoUsuario;
import Modelo.Producto;
import Modelo.Render;
import Modelo.SQLConsultas;
import Modelo.TablaImagen;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class AdminPanel extends javax.swing.JFrame {
    // variables globales
    SQLConsultas prod = null;
    String tnombre;
    int Idprod, Pprov;
    double precicost, precivent;
    String ruta = null;
    DefaultTableModel model;
    // almacenamos los datos del combo box
    int dato_proveedor = 0;
    int dato_compoente = 0;
    int dato_tipousuario = 0;
    int dato_venta = 0;

    public AdminPanel() {
        initComponents();
        consutlaboxProveeodor();
        consutlaboxComponent();
        consutlaboxTipoUsuario();
        consutlaboxVenta();
        PanelUsuario.setVisible(false);
        PanelProductos.setVisible(false);
        PanelProveedores.setVisible(false);
        txt_idProveedor.setEditable(false);
        txt_id.setEditable(false);
        txt_idVenta.setEditable(false);
    }
    
    public void limpiarUser() {
        txt_adIduser.setText("");
        txt_admnombre.setText("");
        txt_adapellidos.setText("");
        txt_ademail.setText("");
        txt_adpas.setText("");
        txt_adtelefono.setText("");
        txt_addirecion.setText("");
    }

    //metodos de combo box
    public void consutlaboxVenta() {
        Connection con = Conexion.getConnection();

        String insert = "SELECT `id_estado`, `etadoactual` FROM `tipo_estado`";
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insert);
            rs = ps.executeQuery();
            while (rs.next()) {
                ComboVenta.addItem(rs.getString("etadoactual"));
            }
        } catch (Exception e) {
        }
    }

    public void consutlaboxTipoUsuario() {
        //consultamos a nuestra tabla relacionada, asi a la hora de dar de alta el usuario, podemos selecionar si sera cliente o administrador
        Connection con = Conexion.getConnection();
        String sql = "select id_tipouser, nombretipo from tipo_usuario";
        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ComboUser.addItem(rs.getString("nombretipo"));
            }
        } catch (Exception e) {
        }
    }

    public void consutlaboxProveeodor() {
        // consultamos para nuestro box los proveedores, asi cunando insertemos un nuevo producto añadiremos un proveedor desde nuestro combo box
        Connection con = Conexion.getConnection();

        String insert = "select * from proveedores";
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insert);
            rs = ps.executeQuery();
            while (rs.next()) {
                ComboProveedor.addItem(rs.getString("nombreproveedor"));
            }
        } catch (Exception e) {
        }
    }

    public void consutlaboxComponent() {
        // consultamos para nuestro box los componentes, asi cunando insertemos un nuevo producto añadiremos un componente desde nuestro combo box
        Connection con = Conexion.getConnection();

        String insert = "select * from componentes";
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insert);
            rs = ps.executeQuery();
            while (rs.next()) {
                ComboProveedor1.addItem(rs.getString("nombrecomponente"));
            }
        } catch (Exception e) {
        }
    }

    public void cargarTabla(JTable tabla) {
        String campo = jtBuscar.getText();
        // procedemos a cargar nuestra tabla
        String where = "";
        if (!"".equals(campo)) {
            where = "and id_producto ='" + campo + "'";
            int i = 0;
        }
        //realizamos un try y cathc
        try {
            // le indicamos un modelo de referencia a nuestra tabla 
            DefaultTableModel modelo = new DefaultTableModel();
            tablaAdmin.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            tablaAdmin.setModel(modelo);
            tablaAdmin.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,stockproducto, precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) " + where;
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            //ejecutamos
            rs = ps.executeQuery();
            //añadimos los campos que deseamos a nuestra tabla
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Proveedor");
            modelo.addColumn("Stock");
            modelo.addColumn("Precio");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Tipo componente");
            while (rs.next()) {
                //procedemos a lllenar columnas
                Object filas[] = new Object[8];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject("nombreproveedor");
                filas[3] = rs.getObject(4);
                filas[4] = rs.getObject(5);
                Blob blob = rs.getBlob(6);
                if (blob != null) {
                    try {
                        //metodo para guardar imagen 
                        //creamos un array de bytes llamado data que lo igualamos al blob y le decimos que alamacene los datos
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;// creamos un metodo para que nos lea la imagen
                        try {
                            //igualamos nuesto metodo de lectura a los bytes que le pasamos mediante data
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        //mostramos la imagen en el label si todo salio correcto
                        ImageIcon icono = new ImageIcon(img);
                        filas[5] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[5] = "No imagen";
                    }
                } else {
                    filas[5] = "No imagen";
                }
                filas[6] = rs.getObject(7);
                filas[7] = rs.getObject("nombrecomponente");

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cargarTablaUsuarios() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            tablaAdminUsuarios.setModel(modelo);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "select * from user where id_user ";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            //result set para la tabla
            // aqui lo que hacemos es pasarle el resultado de la consulta
            ResultSetMetaData rsMd = rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellidos");
            modelo.addColumn("Email");
            modelo.addColumn("Password");
            modelo.addColumn("Telefono");
            modelo.addColumn("Dirección");
            modelo.addColumn("tipo");

            while (rs.next()) { // devolvemos los datos de una sola fila
                // creamos el for para añadir los contenidos a un tipo Object[]
                Object[] filas = new Object[cantidadColumnas];
                //creamos un for para pasar los datos al tipo object[]
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void cargarTablaProveedores() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaProveedor.setModel(modelo);
            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "select * from proveedores where id_proveedor ";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            //result set para la tabla
            // aqui lo que hacemos es pasarle el resultado de la consulta
            ResultSetMetaData rsMd = rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            modelo.addColumn("Id");
            modelo.addColumn("Nombreproveedor");
            modelo.addColumn("Webproveedor");
            modelo.addColumn("telefono");
            while (rs.next()) { // devolvemos los datos de una sola fila
                // creamos el for para añadir los contenidos a un tipo Object[]
                Object[] filas = new Object[cantidadColumnas];
                //creamos un for para pasar los datos al tipo object[]
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void guardarProducto() {
        //procedemos a guardar un producto
        PreparedStatement ps = null;
        DefaultTableModel modelo = new DefaultTableModel();// creamos una variable de la tabla 
        tablaAdmin.setModel(modelo);// le pasamos el modelo de nuestra tabla al de la variable tabla 

        try {
            // realizamos la conexion a nuestra base de datos
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            ps = con.prepareStatement("insert into productos (nombreproducto,marcaproveedor,stockproducto,precioproducto,foto,descripcionproducto, tipocomponente) values(?,?,?,?,?,?,?)");
            //aqui se intorducira la informacion,
            ps.setString(1, txt_id.getText());
            ps.setString(2, txt_nombre.getText());
            //ps.setString(3, txt_proveed.getText());

            ps.setString(3, ComboProveedor.getItemAt(dato_proveedor));
            ps.setString(4, txt_stock.getText());
            ps.setString(5, txt_precio.getText());
            ps.setString(7, txt_descipcion.getText());
            ps.setString(8, ComboProveedor1.getItemAt(dato_compoente));
            //procedemos a ejecutar la insercion
            ps.execute();
            // le indicamos que el producto se ha guardado
            JOptionPane.showMessageDialog(null, "Producto guardado");
            // pasamos el nuevo contenido a nuesto objeto, que esta igualado a fila, esto hara que la nueva insercion al actualizar la tabla sea visible
            Object[] fila = new Object[7];
            fila[0] = txt_id.getText();
            fila[1] = txt_nombre.getText();
            fila[2] = ComboProveedor.getSelectedItem().toString();
            fila[3] = txt_stock.getText();
            fila[4] = txt_precio.getText();
            fila[5] = txt_descipcion.getText();
            fila[6] = ComboProveedor1.getSelectedItem().toString();
            // se añadio una nueva fila
            modelo.addRow(fila);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error al guardar");
        }
    }

    public void guardarProveedor() {
        // procedemos a insertar nuevos proveedores 
        PreparedStatement ps = null;
        DefaultTableModel modelo = new DefaultTableModel(); // a la tabla por defecto, le asignamos una variable llamada modelo
        jTablaProveedor.setModel(modelo);//  a nuestra tabla de proveedores le pasamos el modelo 

        try {
            // realizamos la conexion con nuestra base de datos
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            // realizamos la consulta de insercion -- introduce en proveedores en los campos(nombre,web,telefono) los valores ([valordenombre],[valordeweb],[valordetelefono]);
            ps = con.prepareStatement("INSERT INTO proveedores (nombreproveedor, webproveedor, telefono) VALUES (?,?,?)");
            //recogemos la informacion introducida
            ps.setString(1, txt_nombreProveedor.getText());
            ps.setString(2, txt_webProveedor.getText());
            ps.setInt(3, Integer.parseInt(txt_telefonoProveedor.getText()));
            //procedemos a ejecutar nuesta insercion
            ps.execute();
            JOptionPane.showMessageDialog(null, "Nuevo proveedor guardado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el nuevo proveedor");
        }

    }

    public boolean eliminarProducto() {
        //declaramos una variable y la igualamos a falso
        boolean isDeleted = false;
        //solo necesitaremos el id de nuestro objeto
        String tnombre = txt_id.getText();
        try {
            //comprobamos la conexion con nuestra base de datos
            Connection con = ConexionDB.Conexion.getConnection();
            //creamos una linea en sql en la cual indicamos literalmente
            //elimina de la tabla eliminarProducto donde el eliminarProducto sea igual al selecionado
            String sql = "delete from productos where id_producto =?";
            //le pasamos la sentencia de sql
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, tnombre);
            //creamos una variable para la selecion de la columna, mientras que tambien pasamos
            //nuesta sentencia de sql, que realice una actualizacion
            int rowDeleted = pst.executeUpdate();
            if (rowDeleted > 0) {// si el numero de la columna es mayor a 0 -- osea si tiene contenido
                //nos devolvera verdadero 
                isDeleted = true;
            } else {
                //en caso de que no sea verdadero nos dara falso
                isDeleted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //devolvermos nuestro contenido, solo puede ser verdadero o falso
        return isDeleted;
    }

    public boolean updateAdminUsers() {
        // creamos un tipo boolean =  verdadero o falso
        Boolean isUpdated = false;
        //para poder actualizar nuestro contenido debemos selecionar el campo de la tabla
        //creamos variables a las que se igualan al txtFiled
        String uemail = txt_ademail.getText();
        String unombre = txt_admnombre.getText();
        String uapellido = txt_adapellidos.getText();
        String upass = new String(txt_adpas.getPassword());
        String upss2 = new String(txt_adpas2.getPassword());
        String utelefono = txt_adtelefono.getText();
        String udireccion = txt_addirecion.getText();

        try {
            if (upass.equals(upss2)) {
                Connection con = ConexionDB.Conexion.getConnection();

                //String nuevaContra = CifradoHash.sha1(upass);
                String nuevaContra = upass;
                // creamos la consulta -- acutaliza nombre de tabla los valores -nombre- -email-... etc where = donde el contenido sea igual al id_de la tabla
                String sql = "update user set nombre= ?,apellidos= ?,pass =?,email =?,telefono= ?,direccioncasa=?,tipo=? where id_user";
                System.out.println(sql);
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, unombre);
                pst.setString(2, uapellido);
                pst.setString(3, uemail);
                pst.setString(4, nuevaContra);
                pst.setString(5, utelefono);
                pst.setString(6, udireccion);
                pst.setString(7, ComboUser.getSelectedItem().toString());
                //llamamos a nuestra variable que se iguala al pst.exe..., y llamamos al executeupdate
                int rowCount = pst.executeUpdate();
                //si nuestra variable es mayor que 0
                if (rowCount > 0) {
                    // nos devuelve un valor verdadero, por lo cual se ha ejecutado bien los datos han sido modificados
                    isUpdated = true;
                    JOptionPane.showMessageDialog(null, "actualizado");
                } else {
                    //si nos devuelve un false, como lo iniciamos en falso pues no se ejecutara nuestra actualizacion
                    isUpdated = false;
                    JOptionPane.showMessageDialog(null, "no actualizado");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isUpdated;
    }

    public boolean updateProductos() {
        Boolean isUpdated = false;
        String id = txt_id.getText();
        String nombre = txt_nombre.getText();
        String stok = txt_stock.getText();
        String precio = txt_precio.getText();
        String descr = txt_descipcion.getText();
        PreparedStatement ps;
        Connection con = Conexion.getConnection();
        try {
            ps = con.prepareStatement("update productos set nombreproducto=?,marcaproveedor=?,stockproducto=?,precioproducto=?,foto=?,descripcionproducto=?,tipocomponente=? where id_producto=" + id);
            ps.setString(1, nombre);
            ps.setInt(2, dato_proveedor);
            ps.setString(3, stok);
            ps.setString(4, precio);
            try {
                File file = new File(ruta);
                FileInputStream fi = new FileInputStream(file);
                byte[] image = new byte[(int) file.length()];
                fi.read(image);
                ps.setBytes(5, image);
            } catch (Exception e) {
                ps.setString(5, "No Imagen");
            }
            ps.setString(6, descr);
            ps.setInt(7, dato_compoente);
            int contarfila = ps.executeUpdate();
            if (contarfila >= 0) {
                isUpdated = true;
                JOptionPane.showMessageDialog(null, "se actualizo");
            } else {
                isUpdated = false;
                JOptionPane.showMessageDialog(null, "no se actualizo");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isUpdated;
    }

    public boolean upadateVenta() {
        Boolean isUpdated = false;
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        String id = txt_idVenta.getText();
        try {
            ps = con.prepareStatement("UPDATE prueba2 SET estado=? WHERE id_prueba1=" + id);
            ps.setInt(1, dato_venta);
            int contarfila = ps.executeUpdate();
            if (contarfila >= 0) {
                isUpdated = true;
                JOptionPane.showMessageDialog(null, "se actualizo");
            } else {
                isUpdated = false;
                JOptionPane.showMessageDialog(null, "no se actualizo");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isUpdated;
    }

    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) tablaAdmin.getModel();
        model.setRowCount(0);
    }
    public void clearTableventa() {
        DefaultTableModel model = (DefaultTableModel) jtableVentas.getModel();
        model.setRowCount(0);
    }

    //
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        BackgroundAdminPanel = new javax.swing.JPanel();
        PanelUsuario = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaAdminUsuarios = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        BCargarUserAdmin = new javax.swing.JButton();
        txt_admnombre = new javax.swing.JTextField();
        txt_adapellidos = new javax.swing.JTextField();
        txt_ademail = new javax.swing.JTextField();
        txt_adtelefono = new javax.swing.JTextField();
        txt_adpas = new javax.swing.JPasswordField();
        txt_adpas2 = new javax.swing.JPasswordField();
        txt_addirecion = new javax.swing.JTextField();
        BModificarUser = new javax.swing.JButton();
        BAdmineliminar = new javax.swing.JButton();
        BAdminaniadir = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_adIduser = new javax.swing.JLabel();
        ComboUser = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        btLimpiar = new javax.swing.JButton();
        PanelProductos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAdmin = new javax.swing.JTable();
        jBguardarContenido = new javax.swing.JButton();
        jbEliminar = new javax.swing.JButton();
        jtBuscar = new javax.swing.JTextField();
        jBbusquedaAdmin = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_stock = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_descipcion = new javax.swing.JTextArea();
        ComboProveedor = new javax.swing.JComboBox<>();
        ComboProveedor1 = new javax.swing.JComboBox<>();
        lblimagen = new javax.swing.JLabel();
        lbl_url = new javax.swing.JLabel();
        txt_aniadirdescripcion = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        PanelProveedores = new javax.swing.JPanel();
        txt_nombreProveedor = new javax.swing.JTextField();
        txt_webProveedor = new javax.swing.JTextField();
        txt_telefonoProveedor = new javax.swing.JTextField();
        txt_idProveedor = new javax.swing.JTextField();
        btnGuardarProveedor = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jBEliminarPorveedor = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTablaProveedor = new javax.swing.JTable();
        jtCargarProveedores = new javax.swing.JButton();
        PanelVerVentas = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtableVentas = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        ComboVenta = new javax.swing.JComboBox<>();
        btActualizarEstado = new javax.swing.JButton();
        txt_idVenta = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        AdminUsuarios = new javax.swing.JMenu();
        AdminProductos = new javax.swing.JMenu();
        Proveedores = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Gestion de Administrador"); // NOI18N

        BackgroundAdminPanel.setPreferredSize(new java.awt.Dimension(1480, 705));

        tablaAdminUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "", ""
            }
        ));
        tablaAdminUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAdminUsuariosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaAdminUsuarios);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        BCargarUserAdmin.setText("Cargar contendio de la tabla / Actualizar tabla");
        BCargarUserAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCargarUserAdminActionPerformed(evt);
            }
        });

        BModificarUser.setText("Modificar");
        BModificarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BModificarUserActionPerformed(evt);
            }
        });

        BAdmineliminar.setText("Eliminar");
        BAdmineliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAdmineliminarActionPerformed(evt);
            }
        });

        BAdminaniadir.setText("Añadir");
        BAdminaniadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BAdminaniadirActionPerformed(evt);
            }
        });

        jLabel12.setText("Direccion");

        jLabel14.setText("Confrimar contraseña");

        jLabel11.setText("Contraseña");

        jLabel10.setText("Telefono");

        jLabel9.setText("Correo");

        jLabel8.setText("Apellidos");

        jLabel7.setText("Nombre");

        jLabel13.setText("ID");

        jLabel15.setBackground(new java.awt.Color(51, 255, 255));
        jLabel15.setForeground(new java.awt.Color(0, 255, 255));

        ComboUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboUserItemStateChanged(evt);
            }
        });
        ComboUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboUserActionPerformed(evt);
            }
        });

        jLabel16.setText("Tipo Usuario");

        btLimpiar.setText("Limpiar");
        btLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BCargarUserAdmin))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_admnombre, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(txt_adIduser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(94, 94, 94)
                                .addComponent(txt_adapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(txt_ademail, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(txt_adtelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_addirecion, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboUser, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(txt_adpas, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_adpas2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(BAdminaniadir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BModificarUser)
                .addGap(18, 18, 18)
                .addComponent(BAdmineliminar)
                .addGap(18, 18, 18)
                .addComponent(btLimpiar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BCargarUserAdmin)
                .addGap(53, 53, 53)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_adIduser, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_admnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txt_adapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txt_ademail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_adtelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_adpas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_adpas2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_addirecion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BAdminaniadir)
                    .addComponent(BModificarUser)
                    .addComponent(BAdmineliminar)
                    .addComponent(btLimpiar))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(106, 106, 106))
        );

        javax.swing.GroupLayout PanelUsuarioLayout = new javax.swing.GroupLayout(PanelUsuario);
        PanelUsuario.setLayout(PanelUsuarioLayout);
        PanelUsuarioLayout.setHorizontalGroup(
            PanelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PanelUsuarioLayout.setVerticalGroup(
            PanelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelUsuarioLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        PanelProductos.setPreferredSize(new java.awt.Dimension(1480, 705));

        tablaAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAdminMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaAdmin);

        jBguardarContenido.setText("Guardar contenido");
        jBguardarContenido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBguardarContenidoActionPerformed(evt);
            }
        });

        jbEliminar.setText("Eliminar");
        jbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarActionPerformed(evt);
            }
        });

        jBbusquedaAdmin.setText("Buscar / Cargar contenido");
        jBbusquedaAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBbusquedaAdminActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("ID");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 30, -1));

        txt_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idActionPerformed(evt);
            }
        });
        jPanel1.add(txt_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 30, -1));

        jLabel1.setText("Nombre de contenido");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 146, 20));
        jPanel1.add(txt_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 166, -1));

        jLabel4.setText("Proveedor");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        jLabel5.setText("Precio / €");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 60, -1));
        jPanel1.add(txt_precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 100, -1));

        jLabel6.setText("Cantidad de Stock");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        txt_stock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stockActionPerformed(evt);
            }
        });
        jPanel1.add(txt_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 120, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Elegir Foto");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 80, 30));

        jToggleButton1.setText("Abrir");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 162, -1));

        txt_descipcion.setColumns(20);
        txt_descipcion.setLineWrap(true);
        txt_descipcion.setRows(5);
        txt_descipcion.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txt_descipcion);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 290, 230));

        ComboProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboProveedorItemStateChanged(evt);
            }
        });
        ComboProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboProveedorActionPerformed(evt);
            }
        });
        jPanel1.add(ComboProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 100, -1));

        ComboProveedor1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboProveedor1ItemStateChanged(evt);
            }
        });
        ComboProveedor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboProveedor1ActionPerformed(evt);
            }
        });
        jPanel1.add(ComboProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, -1));
        jPanel1.add(lblimagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, 210, 200));
        jPanel1.add(lbl_url, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 440, 200, 30));
        jPanel1.add(txt_aniadirdescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 290, -1));

        jButton3.setText("Modificar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel18.setText("Buscar por ID");

        jLabel19.setText("A la hora de modificar recuerda selecionar la foto desde su carpeta");

        javax.swing.GroupLayout PanelProductosLayout = new javax.swing.GroupLayout(PanelProductos);
        PanelProductos.setLayout(PanelProductosLayout);
        PanelProductosLayout.setHorizontalGroup(
            PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProductosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
            .addGroup(PanelProductosLayout.createSequentialGroup()
                .addGroup(PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelProductosLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jBbusquedaAdmin)
                        .addGap(472, 472, 472)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelProductosLayout.createSequentialGroup()
                        .addGap(870, 870, 870)
                        .addComponent(jBguardarContenido, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton3)
                        .addGap(77, 77, 77)
                        .addComponent(jbEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PanelProductosLayout.setVerticalGroup(
            PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProductosLayout.createSequentialGroup()
                .addGroup(PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelProductosLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBbusquedaAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelProductosLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelProductosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(PanelProductosLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                        .addGap(10, 10, 10)))
                .addGroup(PanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBguardarContenido)
                    .addComponent(jButton3)
                    .addComponent(jbEliminar))
                .addGap(42, 42, 42))
        );

        btnGuardarProveedor.setText("Guardar nuevo proveedor");
        btnGuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProveedorActionPerformed(evt);
            }
        });

        jButton2.setText("ModificarProveedor");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jBEliminarPorveedor.setText("Eliminar");
        jBEliminarPorveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEliminarPorveedorActionPerformed(evt);
            }
        });

        jTablaProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTablaProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaProveedorMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTablaProveedor);

        jtCargarProveedores.setText("Cargar contenido");
        jtCargarProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtCargarProveedoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelProveedoresLayout = new javax.swing.GroupLayout(PanelProveedores);
        PanelProveedores.setLayout(PanelProveedoresLayout);
        PanelProveedoresLayout.setHorizontalGroup(
            PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProveedoresLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelProveedoresLayout.createSequentialGroup()
                        .addComponent(btnGuardarProveedor)
                        .addGap(98, 98, 98)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBEliminarPorveedor)
                        .addGap(656, 656, 656))
                    .addGroup(PanelProveedoresLayout.createSequentialGroup()
                        .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelProveedoresLayout.createSequentialGroup()
                                .addComponent(txt_nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txt_webProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txt_telefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_idProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(226, 226, 226)
                                .addComponent(jtCargarProveedores))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(278, Short.MAX_VALUE))))
        );
        PanelProveedoresLayout.setVerticalGroup(
            PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProveedoresLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_webProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_telefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_idProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtCargarProveedores))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBEliminarPorveedor)
                    .addComponent(jButton2)
                    .addComponent(btnGuardarProveedor))
                .addGap(85, 85, 85))
        );

        jButton1.setText("Cargar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jtableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtableVentasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jtableVentas);

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel17.setText("Nuestras ventas");

        ComboVenta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboVentaItemStateChanged(evt);
            }
        });
        ComboVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboVentaActionPerformed(evt);
            }
        });

        btActualizarEstado.setText("Actualizar Estado");
        btActualizarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActualizarEstadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelVerVentasLayout = new javax.swing.GroupLayout(PanelVerVentas);
        PanelVerVentas.setLayout(PanelVerVentasLayout);
        PanelVerVentasLayout.setHorizontalGroup(
            PanelVerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelVerVentasLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(PanelVerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(PanelVerVentasLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_idVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(ComboVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btActualizarEstado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        PanelVerVentasLayout.setVerticalGroup(
            PanelVerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelVerVentasLayout.createSequentialGroup()
                .addGroup(PanelVerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelVerVentasLayout.createSequentialGroup()
                        .addGroup(PanelVerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelVerVentasLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jButton1)
                                .addGap(45, 45, 45))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelVerVentasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(PanelVerVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ComboVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btActualizarEstado)
                                    .addComponent(txt_idVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelVerVentasLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout BackgroundAdminPanelLayout = new javax.swing.GroupLayout(BackgroundAdminPanel);
        BackgroundAdminPanel.setLayout(BackgroundAdminPanelLayout);
        BackgroundAdminPanelLayout.setHorizontalGroup(
            BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BackgroundAdminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 1470, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BackgroundAdminPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PanelUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BackgroundAdminPanelLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(PanelProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE)))
            .addGroup(BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BackgroundAdminPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PanelVerVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        BackgroundAdminPanelLayout.setVerticalGroup(
            BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BackgroundAdminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BackgroundAdminPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PanelUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BackgroundAdminPanelLayout.createSequentialGroup()
                    .addGap(44, 44, 44)
                    .addComponent(PanelProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(17, Short.MAX_VALUE)))
            .addGroup(BackgroundAdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BackgroundAdminPanelLayout.createSequentialGroup()
                    .addComponent(PanelVerVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jMenu1.setText("Opciones de Administracion ");

        AdminUsuarios.setText("Usuarios");
        AdminUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AdminUsuariosMouseClicked(evt);
            }
        });
        AdminUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminUsuariosActionPerformed(evt);
            }
        });
        jMenu1.add(AdminUsuarios);

        AdminProductos.setText("Productos");
        AdminProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AdminProductosMouseClicked(evt);
            }
        });
        AdminProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminProductosActionPerformed(evt);
            }
        });
        jMenu1.add(AdminProductos);

        Proveedores.setText("Proveedores");
        Proveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProveedoresMouseClicked(evt);
            }
        });
        jMenu1.add(Proveedores);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Panel Principal");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenu3.setText("HomeAdmin");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackgroundAdminPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1482, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BackgroundAdminPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser j = new JFileChooser();

        FileNameExtensionFilter fil = new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "png", "gif");
        j.setFileFilter(fil);

        int s = j.showOpenDialog(this);
        if (s == JFileChooser.APPROVE_OPTION) {
            ruta = j.getSelectedFile().getAbsolutePath();
            lblimagen.setIcon(new ImageIcon(ruta));
            lbl_url.setText(ruta);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jBguardarContenidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBguardarContenidoActionPerformed
        // en este caso tabajamos con progracion orientada a objetos ya que llamamos parametros y funciones de otro paquete, llamado modelo
        Modelo.SQLConsultas modSql = new SQLConsultas();
        Modelo.Producto modProd = new Producto();
        String url = lbl_url.getText();
        // pasamos la informacion y hacemos lo que hicimos hasta ahora 
        //llamamos a una variable del metodo y le pasamos la informaion introducida
        modProd.setPnombreproducto(txt_nombre.getText());
        modProd.setMarcaproveedor(dato_proveedor);
        modProd.setStockproducto(Integer.parseInt(txt_stock.getText()));
        modProd.setPprecioproducto(Integer.parseInt(txt_precio.getText()));
        modProd.setPdescripcionproducto(txt_descipcion.getText());
        modProd.setPtipocomponente(dato_compoente);
        if (url.trim().length() != 0) {
            if (modSql.registrarProducto(modProd, ruta)) {
                // mediante un if le pasamos la informacion del modSql, y llamamos al metodo que realizamos en este caso se llama registrarProducto y los datos de modProd
                JOptionPane.showMessageDialog(null, "Un,nuevo producto ha añadido a nuestra lista");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "no");
            }
        }
    }//GEN-LAST:event_jBguardarContenidoActionPerformed

    public void limpiar() {
        txt_nombre.setText("");
        txt_descipcion.setText("");
        txt_stock.setText("");
        txt_precio.setText("");
        lbl_url.setText("");
        txt_id.setText("");
        lblimagen.setIcon(new ImageIcon(""));

    }

    public void guardar_imagen(String ruta, String nombre) {
        Connection con = Conexion.getConnection();
        Idprod = Integer.parseInt(txt_id.getText());
        tnombre = txt_nombre.getText();
        precicost = Integer.parseInt(txt_precio.getText());
        //precivent = Integer.parseInt(txt_precioVenta.getText());
        String insert = "insert into producto (id_producto, descripcion, marca, preciocosto, precioventa, foto) values (?,?,?,?,?,?)";
        FileInputStream fi = null;
        PreparedStatement ps = null;

        try {
            // para leer la ruta de donde se seleciona y la pasaremos al string del void
            File file = new File(ruta);
            fi = new FileInputStream(file);

            // llamaos a nuestro insert -- conexion a base de datos
            ps = con.prepareStatement(insert);
            ps.setInt(1, Idprod);
            ps.setString(2, tnombre);
            ps.setInt(3, Pprov);
            ps.setDouble(4, precicost);
            ps.setDouble(5, precivent);
            ps.setBinaryStream(6, fi);

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al guardar");

        }
    }

    private void AdminUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AdminUsuariosMouseClicked
        // TODO add your handling code here:
        cargarTablaUsuarios();
        PanelUsuario.setVisible(true);
        PanelProductos.setVisible(false);
        PanelProveedores.setVisible(false);
        PanelVerVentas.setVisible(false);
    }//GEN-LAST:event_AdminUsuariosMouseClicked

    private void jbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarActionPerformed
        // TODO add your handling code here:
        if (eliminarProducto() == true) {
            JOptionPane.showMessageDialog(this, "contenido eliminado");
            clearTable(); // añadimos el metodo limpiar la tabla -- 
            limpiar();
            cargarTabla(tablaAdmin);
        } else {
            JOptionPane.showMessageDialog(this, "contenido no eliminado");
        }
    }//GEN-LAST:event_jbEliminarActionPerformed

    private void tablaAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAdminMouseClicked
        PreparedStatement ps = null;

        ResultSet rs = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            // variable para saber que fila esta selecionada
            int Fila = tablaAdmin.getSelectedRow();
            //variable para igualar nuestra fila selecionada
            String id_producto = tablaAdmin.getValueAt(Fila, 0).toString();
            // consulta -- seleciona -- -- -- -- de la tabla productos, proveedores donde el id producto
            ps = con.prepareStatement("select id_producto, nombreproducto, nombreproveedor, stockproducto,precioproducto,foto,descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and id_producto=?");

            ps.setString(1, id_producto);
            rs = ps.executeQuery();
            // recorre los datos
            while (rs.next()) {
                //pasamos los datos de nuestra tabla a nuestros text o combo box para que el usuario a la hora de actualizar ese campo no quede vacio
                txt_id.setText(rs.getString("id_producto"));
                txt_nombre.setText(rs.getString("nombreproducto"));
                txt_precio.setText(rs.getString("precioproducto"));
                txt_stock.setText(rs.getString("stockproducto"));
                txt_descipcion.setText(rs.getString("descripcionproducto"));
                ComboProveedor.setSelectedItem(rs.getString("nombreproveedor"));
                ComboProveedor1.setSelectedItem(rs.getString("nombrecomponente"));
                Image i = null;
                Blob blob = rs.getBlob("foto");
                i = javax.imageio.ImageIO.read(blob.getBinaryStream());
                ImageIcon image = new ImageIcon(i);
                lblimagen.setIcon(image);
                lbl_url.setText(rs.getString("foto"));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }//GEN-LAST:event_tablaAdminMouseClicked


    private void txt_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idActionPerformed

    private void txt_stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_stockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stockActionPerformed

    private void jBbusquedaAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBbusquedaAdminActionPerformed
        // TODO add your handling code here:
        //llamamos al metodo cargar tabla al hace rclic
        cargarTabla(tablaAdmin);
    }//GEN-LAST:event_jBbusquedaAdminActionPerformed

    private void ComboProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboProveedorActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_ComboProveedorActionPerformed

    private void BAdmineliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAdmineliminarActionPerformed
        // TODO add your handling code here:
        // llamamos e igualamos nuestro metodo conexion
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps;
        // en caso de que nuesto personal no selecione algun usuario de la tabla y para preveer que se borre toda nuestra tabla(me paso jajaja)
        if (txt_adIduser.getText().equals("")) {
            // llamamos a nuesto metodo en caso de que se haya perdido la conexion
            ConexionDB.PerdidaConexi eliminardato = new PerdidaConexi();
            // llamamos a nuesto joption panel para enseñar un error de que no se puede eliminar si no selecionas a una persona
            eliminardato.optionEliminar();
        } else {
            try {
                // realizamos ala consulta -- elimina de la tabla usuarios donde el id del usuario
                ps = con.prepareStatement("delete from user where id_user=?");
                // por si acaso pasamos la informacion a un tinteger ya que estamos recibiendo un texto .getText()
                ps.setInt(1, Integer.parseInt(txt_adIduser.getText()));
                // realozamos la actualizacion
                int res = ps.executeUpdate();
                // si nuestra variable res es mayor a 0 se elimino a la persona, si no error
                if (res > 0) {
                    JOptionPane.showMessageDialog(null, "Persona eliminada");
                } else {
                    JOptionPane.showConfirmDialog(null, "error al elminar a la persona");
                }
            } catch (Exception e) {
            }
        }

    }//GEN-LAST:event_BAdmineliminarActionPerformed

    private void BCargarUserAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCargarUserAdminActionPerformed
        // TODO add your handling code here:
        cargarTablaUsuarios();
    }//GEN-LAST:event_BCargarUserAdminActionPerformed

    private void AdminProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AdminProductosMouseClicked
        // TODO add your handling code here:
        cargarTabla(tablaAdmin);
        PanelProductos.setVisible(true);
        PanelUsuario.setVisible(false);
        PanelProveedores.setVisible(false);
        PanelVerVentas.setVisible(false);
    }//GEN-LAST:event_AdminProductosMouseClicked

    private void tablaAdminUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAdminUsuariosMouseClicked
        // TODO add your handling code here:
        // realizamos la misma funcion de hacer clic en la tabla para mostrar los usuarios - al igual que hicimos con los productos
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            // variable para saber que fila esta selecionada
            int Fila = tablaAdminUsuarios.getSelectedRow();
            String id_user = tablaAdminUsuarios.getValueAt(Fila, 0).toString();
            ps = con.prepareStatement("select id_user, nombre, apellidos, email, pass, telefono, direccioncasa,nombretipo from user, tipo_usuario where user.tipo=tipo_usuario.id_tipouser and id_user = ?");
            ps.setString(1, id_user);
            rs = ps.executeQuery();
            while (rs.next()) {
                txt_adIduser.setText(rs.getString("id_user"));
                txt_admnombre.setText(rs.getString("nombre"));
                txt_adapellidos.setText(rs.getString("apellidos"));
                txt_ademail.setText(rs.getString("email"));
                txt_adpas.setText(rs.getString("pass"));
                txt_adtelefono.setText(rs.getString("telefono"));
                txt_addirecion.setText(rs.getString("direccioncasa"));
                ComboUser.setSelectedItem(rs.getString("nombretipo"));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }//GEN-LAST:event_tablaAdminUsuariosMouseClicked

    private void BAdminaniadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BAdminaniadirActionPerformed
        /*volvemos a realizar, una insercion mediante objeto , llamando a los metodos de nuestro paquete modelo*/

        Modelo.SQLConsultas modSql = new SQLConsultas();
        Modelo.MetodoUsuario modUser = new MetodoUsuario();

        String pass = new String(txt_adpas.getPassword());
        String passConfirmar = new String(txt_adpas2.getPassword());
        if (pass.equals(passConfirmar)) {
            if (modSql.existeUser(txt_ademail.getText()) == 0) {
                String nuevaContra = pass;
                modUser.setNombre(txt_admnombre.getText());
                modUser.setApellidos(txt_adapellidos.getText());
                modUser.setEmail(txt_ademail.getText());
                modUser.setPass(nuevaContra);
                modUser.setTelefono(txt_adtelefono.getText());
                modUser.setDireccioncasa(txt_addirecion.getText());
                modUser.setTipo(dato_tipousuario);
                //modUser.setTipo(ComboUser.getSelectedItem().toString());
                if (modSql.registrarUser(modUser)) {
                    JOptionPane.showMessageDialog(null, "Usuario creado");
                    //limpiarForm();
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha podido guardar");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ese correo ya esta en uso");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
        }

    }//GEN-LAST:event_BAdminaniadirActionPerformed

    private void BModificarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BModificarUserActionPerformed
        // TODO add your handling code here:
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps;
        String campo = txt_adIduser.getText();

        String where = "";
        if (!"".equals(campo)) {
            where = "where id_user ='" + campo + "'";
            int i = 0;
        }// si nuestro campo esta vacio nos dara un error
        if (txt_adIduser.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "selecione un producto de la tabla");
        } else {
            // si las contraseñas no coinciden no se podran actualizar lso datos
            String pass = new String(txt_adpas.getPassword());
            String passConfirmar = new String(txt_adpas2.getPassword());
            if (pass.equals(passConfirmar)) {
                String nuevaContra = (pass);
                try {
                    //realiamos la ejecucion del update
                    ps = con.prepareStatement("UPDATE user SET nombre=?,apellidos=?, email=?, pass=?, telefono=?,direccioncasa=?, tipo=? " + where);
                    // indicamos los datos
                    ps.setString(1, txt_admnombre.getText());
                    ps.setString(2, txt_adapellidos.getText());
                    ps.setString(3, txt_ademail.getText());
                    ps.setString(4, nuevaContra);
                    ps.setString(5, txt_adtelefono.getText());
                    ps.setString(6, txt_addirecion.getText());
                    ps.setInt(7, dato_tipousuario);
                    int rest = ps.executeUpdate();
                    if (rest > 0) {
                        JOptionPane.showMessageDialog(null, "actualizado");
                    } else {
                        JOptionPane.showMessageDialog(null, "NO actualizado");
                    }
                } catch (Exception e) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
            }
        }
    }//GEN-LAST:event_BModificarUserActionPerformed

    private void ComboProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboProveedorItemStateChanged
        // TODO add your handling code here:
        //realizamos el llenar datos al combo box
        String itemSeleecionado = (String) ComboProveedor.getSelectedItem();
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        // selecionamos el idproveedor de nuestr atabla proveedores donde hayamos selecionado con nuestro combo box
        String sql = "select id_proveedor from proveedores where nombreproveedor='" + itemSeleecionado + "';";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                // siempre campo de al tabla que quiera recoger
                // creamos una variable o metodo global para guardar la informacion 
                dato_proveedor = rs.getInt("id_proveedor");
            }
        } catch (Exception e) {
        }

        //JOptionPane.showMessageDialog(null, itemSeleecionado);
    }//GEN-LAST:event_ComboProveedorItemStateChanged

    private void ComboProveedor1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboProveedor1ItemStateChanged
        // TODO add your handling code here:
        String Selecion = (String) ComboProveedor1.getSelectedItem();
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select id_componente from componentes where nombrecomponente='" + Selecion + "';";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                // siempre campo de al tabla que quiera recohger
                dato_compoente = rs.getInt("id_componente");
            }
        } catch (Exception e) {
        }

        //JOptionPane.showMessageDialog(null, Selecion);
    }//GEN-LAST:event_ComboProveedor1ItemStateChanged

    private void ComboProveedor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboProveedor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboProveedor1ActionPerformed

    private void ComboUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboUserItemStateChanged
        // TODO add your handling code here:
        String seleciontusuario = (String) ComboUser.getSelectedItem();
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select id_tipouser from tipo_usuario where nombretipo='" + seleciontusuario + "';";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                dato_tipousuario = rs.getInt("id_tipouser");
            }
        } catch (Exception e) {
        }
        //JOptionPane.showMessageDialog(null, seleciontusuario);
    }//GEN-LAST:event_ComboUserItemStateChanged

    private void ComboUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboUserActionPerformed

    private void btLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarUser();
    }//GEN-LAST:event_btLimpiarActionPerformed

    private void ProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProveedoresMouseClicked
        // TODO add your handling code here:
        cargarTablaProveedores();
        PanelProveedores.setVisible(true);
        PanelProductos.setVisible(false);
        PanelUsuario.setVisible(false);
        PanelVerVentas.setVisible(false);

    }//GEN-LAST:event_ProveedoresMouseClicked

    private void btnGuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProveedorActionPerformed
        // TODO add your handling code here:
        guardarProveedor();
    }//GEN-LAST:event_btnGuardarProveedorActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        // TODO add your handling code here:
        PanelVerVentas.setVisible(true);
        PanelProveedores.setVisible(false);
        PanelProductos.setVisible(false);
        PanelUsuario.setVisible(false);
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jtCargarProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtCargarProveedoresActionPerformed
        // TODO add your handling code here:
        cargarTablaProveedores();
    }//GEN-LAST:event_jtCargarProveedoresActionPerformed

    private void jBEliminarPorveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEliminarPorveedorActionPerformed
        // TODO add your handling code here:
        // llamamos e igualamos nuestro metodo conexion
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps;
        // en caso de que nuesto personal no selecione algun usuario de la tabla y para preveer que se borre toda nuestra tabla(me paso jajaja)
        if (txt_idProveedor.getText().equals("")) {
            // llamamos a nuesto metodo en caso de que se haya perdido la conexion
            ConexionDB.PerdidaConexi eliminardato = new PerdidaConexi();
            // llamamos a nuesto joption panel para enseñar un error de que no se puede eliminar si no selecionas a una persona
            eliminardato.optionEliminar();
        } else {
            try {
                // realizamos ala consulta -- elimina de la tabla usuarios donde el id del usuario
                ps = con.prepareStatement("delete from proveedores where id_proveedor=?");
                // por si acaso pasamos la informacion a un tinteger ya que estamos recibiendo un texto .getText()
                ps.setInt(1, Integer.parseInt(txt_idProveedor.getText()));
                // realozamos la actualizacion
                int res = ps.executeUpdate();
                // si nuestra variable res es mayor a 0 se elimino a la persona, si no error
                if (res > 0) {
                    JOptionPane.showMessageDialog(null, "Persona eliminada");
                } else {
                    JOptionPane.showConfirmDialog(null, "error al elminar a la persona");
                }
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jBEliminarPorveedorActionPerformed

    private void jTablaProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaProveedorMouseClicked
        // TODO add your handling code here:
        PreparedStatement ps = null;

        ResultSet rs = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            // variable para saber que fila esta selecionada
            int Fila = jTablaProveedor.getSelectedRow();
            //variable para igualar nuestra fila selecionada
            String id_proveedor = jTablaProveedor.getValueAt(Fila, 0).toString();
            // consulta -- seleciona -- -- -- -- de la tabla productos, proveedores donde el id producto
            ps = con.prepareStatement("select * from proveedores where id_proveedor=?");

            ps.setString(1, id_proveedor);
            rs = ps.executeQuery();

            while (rs.next()) {
                txt_idProveedor.setText(rs.getString("id_proveedor"));
                txt_nombreProveedor.setText(rs.getString("nombreproveedor"));
                txt_webProveedor.setText(rs.getString("webproveedor"));
                txt_telefonoProveedor.setText(rs.getString("telefono"));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }//GEN-LAST:event_jTablaProveedorMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps;
        String campo = txt_idProveedor.getText();

        String where = "";
        if (!"".equals(campo)) {
            where = "where id_proveedor ='" + campo + "'";
            int i = 0;
        }
        if (txt_idProveedor.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "selecione un producto de la tabla");
        } else {
            try {//realizamos una actualizacion a nuestros datos
                ps = con.prepareStatement("UPDATE proveedores SET nombreproveedor=?,webproveedor=?, telefono=? where id_proveedor=" + campo);
                System.out.println(ps);
                //Pasamos los datos de los textos para que proceda a actualizar
                ps.setString(1, txt_nombreProveedor.getText());
                ps.setString(2, txt_webProveedor.getText());
                ps.setInt(3, Integer.parseInt(txt_telefonoProveedor.getText()));
                int rest = ps.executeUpdate();
                if (rest > 0) {
                    JOptionPane.showMessageDialog(null, "actualizado");
                } else {
                    JOptionPane.showMessageDialog(null, "NO actualizado");
                }
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // boton para cargar la tabla
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            jtableVentas.setModel(modelo);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //realizamos la consulta a los datos que deseamos ver.
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "select C.id, C.idUsuario, C.total, O.id_prueba2, O.id_prueba1, O.nombre, O.cantidad, E.id_user, E.nombre, E.apellidos, E.email, E.telefono, E.direccioncasa, F.etadoactual "
                    + "from prueba1 C, prueba2 O, user E, tipo_estado F  where (C.id = O.id_prueba1 AND C.idUsuario = E.id_user) and O.estado=F.id_estado";

            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            // tras realizar la consulta procedemos a añadir columnas a la fila 
            modelo.addColumn("Codigo tiquet Venta");
            modelo.addColumn("Nombre Producto");
            modelo.addColumn("Para usuario");
            modelo.addColumn("Apellidos");
            modelo.addColumn("E.direccioncasa");
            modelo.addColumn("E.telefono");
            modelo.addColumn("Cantidad solicitada");
            modelo.addColumn("Total");
            modelo.addColumn("Estado");
            while (rs.next()) {
                //Procedemos a llamar lo que nos interesa para rellenar nuestra tabla de datos
                Object filas[] = new Object[9];
                filas[0] = rs.getObject("C.id");
                filas[1] = rs.getObject("O.nombre");
                filas[2] = rs.getObject("E.nombre");
                filas[3] = rs.getObject("E.apellidos");
                filas[4] = rs.getObject("E.direccioncasa");
                filas[5] = rs.getObject("E.telefono");
                filas[6] = rs.getObject("O.cantidad");
                filas[7] = rs.getObject("C.total");
                filas[8] = rs.getObject("F.etadoactual");
                modelo.addRow(filas);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        // metodo si nos devuelve verdadero se actualiza los datos
        if (updateProductos() == true) {
            JOptionPane.showMessageDialog(this, "contenido Actualizado");
            clearTable(); // añadimos el metodo limpiar la tabla -- 
            limpiar();
            cargarTabla(tablaAdmin);
        } else {
            JOptionPane.showMessageDialog(this, "contenido no Actualizado");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void AdminUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminUsuariosActionPerformed
        // TODO add your handling code here:
        cargarTablaUsuarios();
    }//GEN-LAST:event_AdminUsuariosActionPerformed

    private void AdminProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminProductosActionPerformed
        // TODO add your handling code here:
        cargarTabla(tablaAdmin);
    }//GEN-LAST:event_AdminProductosActionPerformed

    private void jtableVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtableVentasMouseClicked
        // TODO add your handling code here:
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            // variable para saber que fila esta selecionada
            int Fila = jtableVentas.getSelectedRow();
            String id = jtableVentas.getValueAt(Fila, 0).toString();
            ps = con.prepareStatement("select C.id, C.idUsuario, C.total, O.id_prueba2, O.id_prueba1, O.nombre, O.cantidad, E.id_user, E.nombre, E.apellidos, E.email, E.telefono, E.direccioncasa, F.etadoactual "
                    + "from prueba1 C, prueba2 O, user E, tipo_estado F where (C.id = O.id_prueba1 AND C.idUsuario = E.id_user) and O.estado=F.id_estado and id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                txt_idVenta.setText(rs.getString("C.id"));
                ComboVenta.setSelectedItem(rs.getString("F.etadoactual"));
                //ComboBVentas.setSelectedItem(rs.getString("F.etadoactual"));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }//GEN-LAST:event_jtableVentasMouseClicked

    private void ComboVentaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboVentaItemStateChanged
        // TODO add your handling code here:
        String seleciontusuario = (String) ComboVenta.getSelectedItem();
        Connection con = ConexionDB.Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;

        String sql = "select id_estado,etadoactual from tipo_estado where etadoactual='" + seleciontusuario + "';";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                dato_venta = rs.getInt("id_estado");
            }
        } catch (Exception e) {
        }
        //JOptionPane.showMessageDialog(null, "el dato del estado es " +dato_venta);
    }//GEN-LAST:event_ComboVentaItemStateChanged

    private void ComboVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboVentaActionPerformed

    private void btActualizarEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActualizarEstadoActionPerformed
        // TODO add your handling code here:
        if (upadateVenta()== true) {
            JOptionPane.showMessageDialog(this, "contenido Actualizado");
            clearTableventa();   
        } else {
            JOptionPane.showMessageDialog(this, "contenido no Actualizado");
        }
    }//GEN-LAST:event_btActualizarEstadoActionPerformed

    /**
     * @param args the command line arguments
     * Aqui es donde nuestro administrador podra ejercer sus funciones
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu AdminProductos;
    private javax.swing.JMenu AdminUsuarios;
    private javax.swing.JButton BAdminaniadir;
    private javax.swing.JButton BAdmineliminar;
    private javax.swing.JButton BCargarUserAdmin;
    private javax.swing.JButton BModificarUser;
    private javax.swing.JPanel BackgroundAdminPanel;
    private javax.swing.JComboBox<String> ComboProveedor;
    private javax.swing.JComboBox<String> ComboProveedor1;
    private javax.swing.JComboBox<String> ComboUser;
    private javax.swing.JComboBox<String> ComboVenta;
    private javax.swing.JPanel PanelProductos;
    private javax.swing.JPanel PanelProveedores;
    private javax.swing.JPanel PanelUsuario;
    private javax.swing.JPanel PanelVerVentas;
    private javax.swing.JMenu Proveedores;
    private javax.swing.JButton btActualizarEstado;
    private javax.swing.JButton btLimpiar;
    private javax.swing.JButton btnGuardarProveedor;
    private javax.swing.JButton jBEliminarPorveedor;
    private javax.swing.JButton jBbusquedaAdmin;
    private javax.swing.JButton jBguardarContenido;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTablaProveedor;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton jbEliminar;
    private javax.swing.JTextField jtBuscar;
    private javax.swing.JButton jtCargarProveedores;
    private javax.swing.JTable jtableVentas;
    private javax.swing.JLabel lbl_url;
    private javax.swing.JLabel lblimagen;
    private javax.swing.JTable tablaAdmin;
    private javax.swing.JTable tablaAdminUsuarios;
    private javax.swing.JLabel txt_adIduser;
    private javax.swing.JTextField txt_adapellidos;
    private javax.swing.JTextField txt_addirecion;
    private javax.swing.JTextField txt_ademail;
    private javax.swing.JTextField txt_admnombre;
    private javax.swing.JPasswordField txt_adpas;
    private javax.swing.JPasswordField txt_adpas2;
    private javax.swing.JTextField txt_adtelefono;
    private javax.swing.JTextField txt_aniadirdescripcion;
    private javax.swing.JTextArea txt_descipcion;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_idProveedor;
    private javax.swing.JTextField txt_idVenta;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_nombreProveedor;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_stock;
    private javax.swing.JTextField txt_telefonoProveedor;
    private javax.swing.JTextField txt_webProveedor;
    // End of variables declaration//GEN-END:variables
}
