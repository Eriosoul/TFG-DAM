package ConexionDB;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PerdidaConexi {

    public void perdidaConex() {
        ImageIcon icon = new ImageIcon("src/IMG/Error_Database.png");
        int salida = JOptionPane.showConfirmDialog(null, "UPS... Hemos perdido la conexion, compruebe su internet!!\n¿Desea salir de nuesta aplicacion?", "Connection not sucesfull", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        if (salida == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public void optionEliminar(){
        ImageIcon icon = new ImageIcon("src/IMG/alto_riesgo_1.png");
        JOptionPane.showMessageDialog(null," Debe selecionar un campo de la tabla para poder eliminarla \n de lo contrario borraria todos los datos ", "¡ Advertencia !", JOptionPane.PLAIN_MESSAGE, icon);
    }  
}
