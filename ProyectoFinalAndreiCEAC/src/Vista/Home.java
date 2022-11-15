/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.destalle_venta;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.sql.Blob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author tboss
 */
public class Home extends javax.swing.JFrame {

    int logiduser = 0;
    int idUsuarioUpanel = 0;

    // pasamos infromacion por parametro
    public Home(int logId) {
        initComponents();
        this.setResizable(true);
        this.setLocationRelativeTo(this);

        // que inicie la aplicacion y que no aparezca nada
        JPanelHome.setVisible(true);
        JContenidoSobremesa.setVisible(false);
        JPanelCarrito.setVisible(false);
        jPPagar.setVisible(false);
        logiduser = logId;
        idUsuarioUpanel = logId;
        //JOptionPane.showMessageDialog(null, "el id del usuario es : " + logId);
        indicarUsuario();

    }

    //
    public Home() {
        initComponents();
        this.setResizable(true);
        this.setLocationRelativeTo(this);
        // que inicie la aplicacion y que no aparezca nada
        JPanelHome.setVisible(true);
        JContenidoSobremesa.setVisible(false);
        JPanelCarrito.setVisible(false);
        jPPagar.setVisible(false);
        txt_codigo.setEditable(false);
        txt_nombre.setEditable(false);
        txt_precio.setEditable(false);
        txt_verDescripcion.setEditable(false);
    }
    
    int fila = 0;
    double total = 0.00;
    DefaultTableModel m;
    Connection con = ConexionDB.Conexion.getConnection();
    PreparedStatement ps;
    ResultSet rs;
    int dato_usuario = 0;
    String dato_email;
    int id_Comprador = 0;

    public void indicarUsuario() {
        String correo = null;
        try {
            ps = con.prepareStatement("select email from user where id_user=" + logiduser);
            rs = ps.executeQuery();
            if (rs.next()) {
                correo = rs.getString("email");
            }
            lbMostrarNombre.setText(correo);
        } catch (Exception e) {
        }
    }
    // metodos para mostrar nuestros porductos
    public void verProductosSobremesa() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaproductoHome.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            jTablaproductoHome.setModel(modelo);
            jTablaproductoHome.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,  precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and E.id_componente=1;";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            modelo.addColumn("Codigo del Producto");
            modelo.addColumn("Nombre");
            modelo.addColumn("Marca");
            modelo.addColumn("Precio / €");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            while (rs.next()) {

                Object filas[] = new Object[6];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject(3);
                filas[3] = rs.getObject(4);
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    try {
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        ImageIcon icono = new ImageIcon(img);
                        filas[4] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[4] = "No imagen";
                    }
                } else {
                    filas[4] = "No imagen";
                }
                filas[5] = rs.getObject(6);

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void verProductosPortatil() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaproductoHome.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            jTablaproductoHome.setModel(modelo);
            jTablaproductoHome.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,  precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and E.id_componente=2;";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            modelo.addColumn("Codigo del Producto");
            modelo.addColumn("Nombre");
            modelo.addColumn("Marca");
            modelo.addColumn("Precio / €");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            while (rs.next()) {

                Object filas[] = new Object[6];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject(3);
                filas[3] = rs.getObject(4);
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    try {
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        ImageIcon icono = new ImageIcon(img);
                        filas[4] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[4] = "No imagen";
                    }
                } else {
                    filas[4] = "No imagen";
                }
                filas[5] = rs.getObject(6);

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void verProductosRam() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaproductoHome.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            jTablaproductoHome.setModel(modelo);
            jTablaproductoHome.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,  precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and E.id_componente=3;";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            modelo.addColumn("Codigo del Producto");
            modelo.addColumn("Nombre");
            modelo.addColumn("Marca");
            modelo.addColumn("Precio / €");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            while (rs.next()) {

                Object filas[] = new Object[6];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject(3);
                filas[3] = rs.getObject(4);
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    try {
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        ImageIcon icono = new ImageIcon(img);
                        filas[4] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[4] = "No imagen";
                    }
                } else {
                    filas[4] = "No imagen";
                }
                filas[5] = rs.getObject(6);

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void verProductosDiscos() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaproductoHome.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            jTablaproductoHome.setModel(modelo);
            jTablaproductoHome.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,  precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and E.id_componente=4;";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            modelo.addColumn("Codigo del Producto");
            modelo.addColumn("Nombre");
            modelo.addColumn("Marca");
            modelo.addColumn("Precio / €");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            while (rs.next()) {

                Object filas[] = new Object[6];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject(3);
                filas[3] = rs.getObject(4);
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    try {
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        ImageIcon icono = new ImageIcon(img);
                        filas[4] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[4] = "No imagen";
                    }
                } else {
                    filas[4] = "No imagen";
                }
                filas[5] = rs.getObject(6);

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void verProductosPlacaBase() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaproductoHome.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            jTablaproductoHome.setModel(modelo);
            jTablaproductoHome.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,  precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and E.id_componente=5;";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            modelo.addColumn("Codigo del Producto");
            modelo.addColumn("Nombre");
            modelo.addColumn("Marca");
            modelo.addColumn("Precio / €");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            while (rs.next()) {

                Object filas[] = new Object[6];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject(3);
                filas[3] = rs.getObject(4);
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    try {
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        ImageIcon icono = new ImageIcon(img);
                        filas[4] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[4] = "No imagen";
                    }
                } else {
                    filas[4] = "No imagen";
                }
                filas[5] = rs.getObject(6);

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void verProductosGraficas() {
        try {
            //
            DefaultTableModel modelo = new DefaultTableModel();
            jTablaproductoHome.setDefaultRenderer(Object.class, new Modelo.TablaImagen());
            jTablaproductoHome.setModel(modelo);
            jTablaproductoHome.setRowHeight(120);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            //preparamos la conslta
            PreparedStatement ps = null;
            ResultSet rs = null;

            String sql = "SELECT id_producto, nombreproducto, nombreproveedor,  precioproducto, foto, descripcionproducto, nombrecomponente "
                    + "FROM productos C, proveedores O, componentes E WHERE (C.marcaproveedor = O.id_proveedor AND C.tipocomponente = E.id_componente) and E.id_componente=6;";
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            modelo.addColumn("Codigo del Producto");
            modelo.addColumn("Nombre");
            modelo.addColumn("Marca");
            modelo.addColumn("Precio / €");
            modelo.addColumn("Foto");
            modelo.addColumn("Descripcion");
            while (rs.next()) {

                Object filas[] = new Object[6];
                filas[0] = rs.getObject(1);
                filas[1] = rs.getObject(2);
                filas[2] = rs.getObject(3);
                filas[3] = rs.getObject(4);
                Blob blob = rs.getBlob(5);
                if (blob != null) {
                    try {
                        byte[] data = blob.getBytes(1, (int) blob.length());
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(data));
                        } catch (Exception e) {
                        }
                        ImageIcon icono = new ImageIcon(img);
                        filas[4] = new JLabel(icono);
                    } catch (Exception ex) {
                        filas[4] = "No imagen";
                    }
                } else {
                    filas[4] = "No imagen";
                }
                filas[5] = rs.getObject(6);

                modelo.addRow(filas);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void InsertarDatosEnCompra() {
        PreparedStatement segundaConsulta = null;
        try {
            int idprueba = 0;
            segundaConsulta = con.prepareStatement("insert into prueba1(idUsuario,total, fecha) values(?,?, NOW())", Statement.RETURN_GENERATED_KEYS);
            // donde coincide nuestro usuario le pasamos el id de la consulta anterior
            segundaConsulta.setInt(1, idUsuarioUpanel);
            segundaConsulta.setDouble(2, total);
            segundaConsulta.executeUpdate();
            // en caso de que se relice, nos dara un mensaje por pantalla
            ResultSet rst = segundaConsulta.getGeneratedKeys();
            rst.next();
            idprueba = rst.getInt(1);

            if (idprueba == 0) {
                JOptionPane.showMessageDialog(null, "Error, al guardar su venta");
                return;
            }
            JOptionPane.showMessageDialog(null, "Venta completada");

            agregarProductos(idprueba);
            limpiar();
            limpiaTabla();
            JPanelCarrito.setVisible(true);
            jPPagar.setVisible(false);
            total = 0.00;
            txt_to.setText("0.00");
        } catch (Exception e) {
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        BackgroundPanel = new javax.swing.JPanel();
        ContenidoBackgrowundPanel = new javax.swing.JPanel();
        JPanelHome = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        JPanelCarrito = new javax.swing.JPanel();
        lb = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCarrito = new javax.swing.JTable();
        txt_to = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        JContenidoSobremesa = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        PVistaProducto = new javax.swing.JPanel();
        txt_codigo = new javax.swing.JTextField();
        txt_nombre = new javax.swing.JTextField();
        txt_cantidad = new javax.swing.JTextField();
        txt_precio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_verDescripcion = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablaproductoHome = new javax.swing.JTable();
        jPPagar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnPagar1 = new javax.swing.JButton();
        txtmailpagar = new javax.swing.JTextField();
        txtIdUserPagar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        txtnumeroTarjeta = new javax.swing.JTextField();
        PanelSuperior = new javax.swing.JPanel();
        PanelCarrito = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblContadorCarrito = new javax.swing.JLabel();
        lbVercorreo = new javax.swing.JLabel();
        BackgrounMenuPanel = new javax.swing.JPanel();
        jmostrarmail = new javax.swing.JPanel();
        lbMostrarNombre = new javax.swing.JLabel();
        MenuIzquierda = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbHome = new javax.swing.JLabel();
        lbSobremesa = new javax.swing.JLabel();
        lbPortatil = new javax.swing.JLabel();
        lbRam = new javax.swing.JLabel();
        lbDiscoduro = new javax.swing.JLabel();
        lbPlacabase = new javax.swing.JLabel();
        lbGraficas = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 700));

        BackgroundPanel.setBackground(new java.awt.Color(34, 33, 74));
        BackgroundPanel.setMinimumSize(new java.awt.Dimension(700, 700));
        BackgroundPanel.setPreferredSize(new java.awt.Dimension(1480, 700));

        ContenidoBackgrowundPanel.setPreferredSize(new java.awt.Dimension(1140, 661));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(34, 33, 74));
        jLabel12.setText("Bienvenid@ a ORSTED");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(34, 33, 74));
        jLabel13.setText("Ves algo que te guste compralo");

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(34, 33, 74));
        jLabel14.setText("Muy caro mira otras ofertas");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/pc_home2.PNG"))); // NOI18N

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/pc_home.PNG"))); // NOI18N

        javax.swing.GroupLayout JPanelHomeLayout = new javax.swing.GroupLayout(JPanelHome);
        JPanelHome.setLayout(JPanelHomeLayout);
        JPanelHomeLayout.setHorizontalGroup(
            JPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHomeLayout.createSequentialGroup()
                .addGap(363, 363, 363)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(417, 417, 417))
            .addGroup(JPanelHomeLayout.createSequentialGroup()
                .addGap(290, 290, 290)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(490, 490, 490))
            .addGroup(JPanelHomeLayout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(144, 144, 144)
                .addGroup(JPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHomeLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(50, 50, 50))
                    .addGroup(JPanelHomeLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(240, 240, 240))
        );
        JPanelHomeLayout.setVerticalGroup(
            JPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHomeLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addGap(45, 45, 45)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(JPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelHomeLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3)))
                .addGap(57, 57, 57))
        );

        JPanelCarrito.setPreferredSize(new java.awt.Dimension(1140, 661));

        jTableCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Cantidad", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableCarrito);

        txt_to.setFont(new java.awt.Font("Courier New", 0, 24)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel8.setText("Total € :");

        btnEliminar.setText("Eliminar Producto de la cesta");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jButton1.setText("Realizar Compra");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPanelCarritoLayout = new javax.swing.GroupLayout(JPanelCarrito);
        JPanelCarrito.setLayout(JPanelCarritoLayout);
        JPanelCarritoLayout.setHorizontalGroup(
            JPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelCarritoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(JPanelCarritoLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(352, 352, 352)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_to, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
            .addGroup(JPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelCarritoLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1121, Short.MAX_VALUE)
                    .addGap(15, 15, 15)))
        );
        JPanelCarritoLayout.setVerticalGroup(
            JPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelCarritoLayout.createSequentialGroup()
                .addContainerGap(638, Short.MAX_VALUE)
                .addGroup(JPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(jButton1)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_to, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(JPanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JPanelCarritoLayout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addGap(125, 125, 125)))
        );

        PVistaProducto.setBackground(new java.awt.Color(255, 255, 255));

        txt_cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cantidadActionPerformed(evt);
            }
        });
        txt_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyPressed(evt);
            }
        });

        txt_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_precioActionPerformed(evt);
            }
        });

        jLabel4.setText("Codigo");

        jLabel5.setText("Nombre");

        jLabel6.setText("Precio  / €");

        jLabel7.setText("Introduzca su cantidad y presione enter");

        txt_verDescripcion.setColumns(20);
        txt_verDescripcion.setLineWrap(true);
        txt_verDescripcion.setRows(5);
        txt_verDescripcion.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txt_verDescripcion);

        jLabel21.setText("Descripción del producto");

        javax.swing.GroupLayout PVistaProductoLayout = new javax.swing.GroupLayout(PVistaProducto);
        PVistaProducto.setLayout(PVistaProductoLayout);
        PVistaProductoLayout.setHorizontalGroup(
            PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PVistaProductoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PVistaProductoLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155))
                    .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(PVistaProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PVistaProductoLayout.createSequentialGroup()
                        .addComponent(txt_precio)
                        .addGap(74, 74, 74))
                    .addGroup(PVistaProductoLayout.createSequentialGroup()
                        .addGroup(PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PVistaProductoLayout.createSequentialGroup()
                        .addGroup(PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PVistaProductoLayout.createSequentialGroup()
                        .addGroup(PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_cantidad))
                        .addGap(71, 71, 71))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        PVistaProductoLayout.setVerticalGroup(
            PVistaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PVistaProductoLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        jTablaproductoHome.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        jTablaproductoHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaproductoHomeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTablaproductoHome);
        if (jTablaproductoHome.getColumnModel().getColumnCount() > 0) {
            jTablaproductoHome.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(18, 18, 18)
                .addComponent(PVistaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(PVistaProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(64, 64, 64))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1)
                .addGap(44, 44, 44))
        );

        javax.swing.GroupLayout JContenidoSobremesaLayout = new javax.swing.GroupLayout(JContenidoSobremesa);
        JContenidoSobremesa.setLayout(JContenidoSobremesaLayout);
        JContenidoSobremesaLayout.setHorizontalGroup(
            JContenidoSobremesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JContenidoSobremesaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JContenidoSobremesaLayout.setVerticalGroup(
            JContenidoSobremesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JContenidoSobremesaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPagar1.setText("Volver");
        btnPagar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagar1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel9.setText("Intorduzca su email:");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel10.setText("Introduzca su número de tarjeta");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/Tarjetas_pago.png"))); // NOI18N

        jButton2.setText("Realizar compra y finalizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtnumeroTarjeta.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        txtnumeroTarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnumeroTarjetaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnumeroTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtmailpagar, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(17, 17, 17))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(btnPagar1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(274, 274, 274))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtIdUserPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(txtIdUserPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtmailpagar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnumeroTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPagar1)
                    .addComponent(jButton2))
                .addGap(147, 147, 147))
        );

        javax.swing.GroupLayout jPPagarLayout = new javax.swing.GroupLayout(jPPagar);
        jPPagar.setLayout(jPPagarLayout);
        jPPagarLayout.setHorizontalGroup(
            jPPagarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPagarLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        jPPagarLayout.setVerticalGroup(
            jPPagarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPagarLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ContenidoBackgrowundPanelLayout = new javax.swing.GroupLayout(ContenidoBackgrowundPanel);
        ContenidoBackgrowundPanel.setLayout(ContenidoBackgrowundPanelLayout);
        ContenidoBackgrowundPanelLayout.setHorizontalGroup(
            ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContenidoBackgrowundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JContenidoSobremesa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContenidoBackgrowundPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(JPanelHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(15, 15, 15)))
            .addGroup(ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContenidoBackgrowundPanelLayout.createSequentialGroup()
                    .addComponent(JPanelCarrito, javax.swing.GroupLayout.DEFAULT_SIZE, 1215, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContenidoBackgrowundPanelLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(jPPagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(9, 9, 9)))
        );
        ContenidoBackgrowundPanelLayout.setVerticalGroup(
            ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoBackgrowundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JContenidoSobremesa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContenidoBackgrowundPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(JPanelHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContenidoBackgrowundPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(JPanelCarrito, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(ContenidoBackgrowundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContenidoBackgrowundPanelLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jPPagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(56, 56, 56)))
        );

        PanelSuperior.setBackground(new java.awt.Color(34, 33, 94));
        PanelSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelCarrito.setBackground(new java.awt.Color(34, 33, 94));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/icons8-carrito-de-compras-40.png"))); // NOI18N
        jLabel11.setText("Carrito");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setText("(");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel3.setText(")");

        lblContadorCarrito.setBackground(new java.awt.Color(255, 153, 0));
        lblContadorCarrito.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblContadorCarrito.setForeground(new java.awt.Color(255, 153, 51));
        lblContadorCarrito.setText("0");

        javax.swing.GroupLayout PanelCarritoLayout = new javax.swing.GroupLayout(PanelCarrito);
        PanelCarrito.setLayout(PanelCarritoLayout);
        PanelCarritoLayout.setHorizontalGroup(
            PanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCarritoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(lblContadorCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelCarritoLayout.setVerticalGroup(
            PanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCarritoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addGroup(PanelCarritoLayout.createSequentialGroup()
                        .addGroup(PanelCarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblContadorCarrito)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(7, 7, 7)))
                .addContainerGap())
        );

        PanelSuperior.add(PanelCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        lbVercorreo.setBackground(new java.awt.Color(255, 255, 255));
        lbVercorreo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        PanelSuperior.add(lbVercorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 130, 40));

        BackgrounMenuPanel.setBackground(new java.awt.Color(34, 33, 74));

        jmostrarmail.setBackground(new java.awt.Color(34, 33, 74));

        lbMostrarNombre.setBackground(new java.awt.Color(255, 255, 255));
        lbMostrarNombre.setFont(new java.awt.Font("Microsoft New Tai Lue", 0, 18)); // NOI18N
        lbMostrarNombre.setForeground(new java.awt.Color(255, 255, 255));
        lbMostrarNombre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/icons8-user-32.png"))); // NOI18N
        lbMostrarNombre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbMostrarNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbMostrarNombreMouseClicked(evt);
            }
        });

        MenuIzquierda.setBackground(new java.awt.Color(34, 33, 74));
        MenuIzquierda.setPreferredSize(new java.awt.Dimension(243, 300));
        MenuIzquierda.setLayout(new java.awt.GridBagLayout());

        jLabel1.setBackground(new java.awt.Color(255, 63, 65));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Todas las categorias");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 6, 0, 0);
        MenuIzquierda.add(jLabel1, gridBagConstraints);

        lbHome.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 48)); // NOI18N
        lbHome.setForeground(new java.awt.Color(255, 255, 255));
        lbHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_home.png"))); // NOI18N
        lbHome.setText("   Home");
        lbHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbHomeMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(45, 6, 0, 0);
        MenuIzquierda.add(lbHome, gridBagConstraints);

        lbSobremesa.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 20)); // NOI18N
        lbSobremesa.setForeground(new java.awt.Color(255, 255, 255));
        lbSobremesa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_pc.png"))); // NOI18N
        lbSobremesa.setText("     Sobremesa");
        lbSobremesa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbSobremesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbSobremesaMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 54;
        gridBagConstraints.ipady = -11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 0, 0);
        MenuIzquierda.add(lbSobremesa, gridBagConstraints);

        lbPortatil.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 20)); // NOI18N
        lbPortatil.setForeground(new java.awt.Color(255, 255, 255));
        lbPortatil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_portatil.png"))); // NOI18N
        lbPortatil.setText("       Portatiles");
        lbPortatil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbPortatil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbPortatilMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 71;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        MenuIzquierda.add(lbPortatil, gridBagConstraints);

        lbRam.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 20)); // NOI18N
        lbRam.setForeground(new java.awt.Color(255, 255, 255));
        lbRam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_ram.png"))); // NOI18N
        lbRam.setText("       Memorias RAM");
        lbRam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbRam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbRamMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 64;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        MenuIzquierda.add(lbRam, gridBagConstraints);

        lbDiscoduro.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 20)); // NOI18N
        lbDiscoduro.setForeground(new java.awt.Color(255, 255, 255));
        lbDiscoduro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_data.png"))); // NOI18N
        lbDiscoduro.setText("       Discors duros");
        lbDiscoduro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbDiscoduro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbDiscoduroMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        MenuIzquierda.add(lbDiscoduro, gridBagConstraints);

        lbPlacabase.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 20)); // NOI18N
        lbPlacabase.setForeground(new java.awt.Color(255, 255, 255));
        lbPlacabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_board.png"))); // NOI18N
        lbPlacabase.setText("       Placa Base");
        lbPlacabase.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbPlacabase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbPlacabaseMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 62;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        MenuIzquierda.add(lbPlacabase, gridBagConstraints);

        lbGraficas.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 20)); // NOI18N
        lbGraficas.setForeground(new java.awt.Color(255, 255, 255));
        lbGraficas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGAPP/img_card.png"))); // NOI18N
        lbGraficas.setText("       Graficas");
        lbGraficas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbGraficas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbGraficasMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 82;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        MenuIzquierda.add(lbGraficas, gridBagConstraints);

        javax.swing.GroupLayout jmostrarmailLayout = new javax.swing.GroupLayout(jmostrarmail);
        jmostrarmail.setLayout(jmostrarmailLayout);
        jmostrarmailLayout.setHorizontalGroup(
            jmostrarmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jmostrarmailLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbMostrarNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jmostrarmailLayout.createSequentialGroup()
                .addComponent(MenuIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jmostrarmailLayout.setVerticalGroup(
            jmostrarmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jmostrarmailLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbMostrarNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MenuIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout BackgrounMenuPanelLayout = new javax.swing.GroupLayout(BackgrounMenuPanel);
        BackgrounMenuPanel.setLayout(BackgrounMenuPanelLayout);
        BackgrounMenuPanelLayout.setHorizontalGroup(
            BackgrounMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgrounMenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jmostrarmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BackgrounMenuPanelLayout.setVerticalGroup(
            BackgrounMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BackgrounMenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jmostrarmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout BackgroundPanelLayout = new javax.swing.GroupLayout(BackgroundPanel);
        BackgroundPanel.setLayout(BackgroundPanelLayout);
        BackgroundPanelLayout.setHorizontalGroup(
            BackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BackgrounMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(BackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ContenidoBackgrowundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1163, Short.MAX_VALUE)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, 1163, Short.MAX_VALUE)))
        );
        BackgroundPanelLayout.setVerticalGroup(
            BackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackgrounMenuPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(BackgroundPanelLayout.createSequentialGroup()
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ContenidoBackgrowundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1473, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbSobremesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbSobremesaMouseClicked
        // TODO add your handling code here:
        JContenidoSobremesa.setVisible(true);
        JPanelHome.setVisible(false);
        JPanelCarrito.setVisible(false);

        verProductosSobremesa();
        limpiar();

    }//GEN-LAST:event_lbSobremesaMouseClicked

    private void lbPortatilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbPortatilMouseClicked
        // TODO add your handling code here:
        JContenidoSobremesa.setVisible(true);
        JPanelHome.setVisible(false);
        JPanelCarrito.setVisible(false);
        verProductosPortatil();
        limpiar();

    }//GEN-LAST:event_lbPortatilMouseClicked

    private void lbHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbHomeMouseClicked
        // TODO add your handling code here:
        JPanelHome.setVisible(true);
        JContenidoSobremesa.setVisible(false);
        JPanelCarrito.setVisible(false);
        limpiar();
    }//GEN-LAST:event_lbHomeMouseClicked

    private void lbRamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbRamMouseClicked
        // TODO add your handling code here:
        verProductosRam();
        JContenidoSobremesa.setVisible(true);
        JPanelHome.setVisible(false);
        JPanelCarrito.setVisible(false);
        limpiar();
    }//GEN-LAST:event_lbRamMouseClicked

    private void lbDiscoduroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbDiscoduroMouseClicked
        // TODO add your handling code here:
        verProductosDiscos();
        JContenidoSobremesa.setVisible(true);
        JPanelHome.setVisible(false);
        JPanelCarrito.setVisible(false);
        limpiar();
    }//GEN-LAST:event_lbDiscoduroMouseClicked

    private void lbPlacabaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbPlacabaseMouseClicked
        // TODO add your handling code here:
        verProductosPlacaBase();
        JContenidoSobremesa.setVisible(true);
        JPanelHome.setVisible(false);
        JPanelCarrito.setVisible(false);
        limpiar();
    }//GEN-LAST:event_lbPlacabaseMouseClicked

    private void lbGraficasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbGraficasMouseClicked
        // TODO add your handling code here:
        verProductosGraficas();
        JContenidoSobremesa.setVisible(true);
        JPanelHome.setVisible(false);
        JPanelCarrito.setVisible(false);
        limpiar();
    }//GEN-LAST:event_lbGraficasMouseClicked

    private void lbMostrarNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbMostrarNombreMouseClicked
        // TODO add your handling code here:
        UserPanel u = new UserPanel(idUsuarioUpanel);
        u.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_lbMostrarNombreMouseClicked

    private void txt_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_precioActionPerformed

    private void jTablaproductoHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaproductoHomeMouseClicked
        // TODO add your handling code here:
        PreparedStatement ps = null;
        ResultSet rs = null;
        int Fila = jTablaproductoHome.getSelectedRow();
        String id_user = jTablaproductoHome.getValueAt(Fila, 0).toString();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/log", "root", "");
            // variable para saber que fila esta selecionada

            ps = con.prepareStatement("select id_producto,nombreproducto,precioproducto, descripcionproducto  from productos where id_producto = ?");
            ps.setString(1, id_user);
            rs = ps.executeQuery();
            while (rs.next()) {
                txt_codigo.setText(rs.getString("id_producto"));
                txt_nombre.setText(rs.getString("nombreproducto"));
                txt_precio.setText(rs.getString("precioproducto"));
                txt_verDescripcion.setText(rs.getString("descripcionproducto"));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }//GEN-LAST:event_jTablaproductoHomeMouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        JPanelCarrito.setVisible(true);
        JContenidoSobremesa.setVisible(false);
        JPanelHome.setVisible(false);
        jPPagar.setVisible(false);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void txt_cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cantidadActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_cantidadActionPerformed

    private void txt_cantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyPressed
        // TODO add your handling code here:
        int cant = 0;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(txt_cantidad.getText())) {
                try {
                    double precio = Double.parseDouble(txt_precio.getText());
                    int cantidad = Integer.parseInt(txt_cantidad.getText());
                    int extstencia = 0;
                    ps = con.prepareStatement("select stockproducto from productos where id_producto=?");
                    ps.setString(1, txt_codigo.getText());
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        extstencia = rs.getInt("stockproducto");

                    } else {

                        return;
                    }
                    // al introducir la cantidad deseada se comprobara con nuestra base de datos, y en caso de que no dispongamos nos marcara un error
                    if (extstencia >= cantidad) {
                        // en caso de que introduzca un 0 o un numero negativo le indicaremos de que esos valores no son correctos
                        if (cantidad <= cant) {
                            JOptionPane.showMessageDialog(null, "No puede introducir esos valores");
                        } else {

                            DefaultTableModel temp = (DefaultTableModel) jTableCarrito.getModel();
                            temp.addRow(new Object[1]);

                            int columna = 0;
                            jTableCarrito.setValueAt(txt_codigo.getText(), fila, columna);
                            columna++;
                            jTableCarrito.setValueAt(txt_nombre.getText(), fila, columna);
                            columna++;
                            jTableCarrito.setValueAt(txt_cantidad.getText(), fila, columna);
                            columna++;
                            double totalprecio = precio * cantidad;
                            jTableCarrito.setValueAt(totalprecio, fila, columna);
                            fila++;
                            lblContadorCarrito.setText(fila + " ");
                            limpiar();
                            actualizaPago();
                            // txt_codigo.requestFocusInWindow();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No disponemos de tantos productos");
                    }
                } catch (Exception e) {
                }
            }
        }
    }//GEN-LAST:event_txt_cantidadKeyPressed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel temp = (DefaultTableModel) jTableCarrito.getModel();

        if (jTableCarrito.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar el producto");
        } else {
            temp.removeRow(jTableCarrito.getSelectedRow());
            fila--;
            lblContadorCarrito.setText(fila + " ");
            actualizaPago();
        }

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (fila <= 0) {
            JOptionPane.showMessageDialog(null, "Su cesta esta vacia");
        } else {
            jPPagar.setVisible(true);
            JPanelCarrito.setVisible(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnPagar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagar1ActionPerformed
        // TODO add your handling code here:
        JPanelCarrito.setVisible(true);
        jPPagar.setVisible(false);

    }//GEN-LAST:event_btnPagar1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtmailpagar.getText())) {
            if (!"".equals(txtnumeroTarjeta.getText())) {
                InsertarDatosEnCompra();

            } else {
                JOptionPane.showMessageDialog(null, "No introdujo su numero de tarjeta");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No ha introducido su corre, por favor rellene los datos");
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtnumeroTarjetaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumeroTarjetaKeyTyped
        // TODO add your handling code here:
        int key = evt.getKeyChar();
        // creamos una restricion solo entero los numeros estan del 0 al 9 del 48 al 57 representan al tabla ascii -- indimaos que su logitud sea de 16 
        boolean numbero = key >= 48 && key <= 57;
        if (!numbero) {
            evt.consume();
        }
        if (txtnumeroTarjeta.getText().length() == 16) {
            evt.consume();
        }
    }//GEN-LAST:event_txtnumeroTarjetaKeyTyped

    private void limpiar() {
        //Metodo para limpiar la tras la insercion de cada producto
        txt_codigo.setText("");
        txt_nombre.setText("");
        txt_precio.setText("");
        txt_cantidad.setText("");
        txt_verDescripcion.setText("");
    }

    private void actualizaPago() {
        total = 0.00;
        // metodo para indicar el total que llevamos alamacenados
        int numeroFilas = jTableCarrito.getRowCount();
        for (int a = 0; a < numeroFilas; a++) {
            total = total + Double.parseDouble(String.valueOf(jTableCarrito.getModel().getValueAt(a, 3)));
        }
        txt_to.setText(String.format("%.2f", total));
    }

    private void agregarProductos(int idprueba) {// pasamos el id de nuestra talbla medainte parametros
        // llamao a mi objeto y le digo que el estado sera 1
        Modelo.destalle_venta det = new destalle_venta();
        double precio;
        
        int id, cantidad;
        String nombre, codigo;
        int filasT = jTableCarrito.getRowCount();
        // contamos nuestras filas de la tabla 
        for (int b = 0; b < filasT; b++) {
            try {
                codigo = (String) jTableCarrito.getValueAt(b, 0);
                ps = con.prepareStatement("select id_producto, nombreproducto,precioproducto from productos where id_producto=?");

                ps.setString(1, codigo);
                rs = ps.executeQuery();
                //id_producto, nombreproducto,  precioproducto
                while (rs.next()) {
                    id = rs.getInt("id_producto");
                    nombre = rs.getString("nombreproducto");
                    precio = rs.getDouble("precioproducto");
                    //realizamos la insercion 
                    cantidad = Integer.parseInt(jTableCarrito.getValueAt(b, 2).toString());
                    //PreparedStatement psI = con.prepareStatement("Insert into detalle_venta_producto (id_venta,id_producto,nombre,precio,cantidad) values (?,?,?,?,?)");
                    PreparedStatement psI = con.prepareStatement("Insert into prueba2 (id_prueba1,id_producto,nombre,precio,cantidad,estado) values (?,?,?,?,?,?)");
                    psI.setInt(1, idprueba);
                    psI.setInt(2, id);
                    psI.setString(3, nombre);
                    psI.setDouble(4, precio);
                    psI.setInt(5, cantidad);
                    psI.setInt(6, det.getEstado());
                    psI.executeUpdate();
                    System.out.println(psI);
                    //actualizamos nuestro stock segun el id del producto
                    PreparedStatement psU = con.prepareStatement("Update productos set stockproducto =(stockproducto-?)  where id_producto=?");
                    psU.setInt(1, cantidad);
                    psU.setInt(2, id);
                    psU.executeUpdate();
                    System.out.println(psU);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        }
    }

    private void limpiaTabla() {
        DefaultTableModel temp = (DefaultTableModel) jTableCarrito.getModel();
        int filas = jTableCarrito.getRowCount();
        lblContadorCarrito.setText("0");
        for (int a = 0; filas > a; a++) {
            temp.removeRow(0);
        }
    }

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BackgrounMenuPanel;
    private javax.swing.JPanel BackgroundPanel;
    private javax.swing.JPanel ContenidoBackgrowundPanel;
    private javax.swing.JPanel JContenidoSobremesa;
    private javax.swing.JPanel JPanelCarrito;
    private javax.swing.JPanel JPanelHome;
    private javax.swing.JPanel MenuIzquierda;
    private javax.swing.JPanel PVistaProducto;
    private javax.swing.JPanel PanelCarrito;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnPagar1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPPagar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTablaproductoHome;
    private javax.swing.JTable jTableCarrito;
    private javax.swing.JPanel jmostrarmail;
    private javax.swing.JLabel lb;
    private javax.swing.JLabel lbDiscoduro;
    private javax.swing.JLabel lbGraficas;
    private javax.swing.JLabel lbHome;
    public javax.swing.JLabel lbMostrarNombre;
    private javax.swing.JLabel lbPlacabase;
    private javax.swing.JLabel lbPortatil;
    private javax.swing.JLabel lbRam;
    private javax.swing.JLabel lbSobremesa;
    public javax.swing.JLabel lbVercorreo;
    public javax.swing.JLabel lblContadorCarrito;
    private javax.swing.JTextField txtIdUserPagar;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_codigo;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_to;
    private javax.swing.JTextArea txt_verDescripcion;
    private javax.swing.JTextField txtmailpagar;
    private javax.swing.JTextField txtnumeroTarjeta;
    // End of variables declaration//GEN-END:variables
}
