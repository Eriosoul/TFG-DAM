package Modelo;
// metodo para almacenar la informacion de mi proveedor en //objeto
public class MetodoProveedor {
    
    private int id_prov = 0;
    private String nombreprov;

    public int getId_prov() {
        return id_prov;
    }

    public void setId_prov(int id_prov) {
        this.id_prov = id_prov;
    }

    public String getNombreprov() {
        return nombreprov;
    }

    public void setNombreprov(String nombreprov) {
        this.nombreprov = nombreprov;
    }
}
