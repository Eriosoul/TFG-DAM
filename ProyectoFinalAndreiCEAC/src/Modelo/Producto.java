package Modelo;
// clase de producto // objeto
import java.sql.Blob;

public class Producto {
    private int id_prod = 0;
    private String pnombreproducto;
    private int marcaproveedor = 0;
    private int stockproducto = 0;
    private int pprecioproducto= 0;
    private byte[] pfoto;
    private String pdescripcionproducto;
    private int ptipocomponente= 0;

    public int getId_prod() {
        return id_prod;
    }

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public String getPnombreproducto() {
        return pnombreproducto;
    }

    public void setPnombreproducto(String pnombreproducto) {
        this.pnombreproducto = pnombreproducto;
    }

    public int getMarcaproveedor() {
        return marcaproveedor;
    }

    public void setMarcaproveedor(int marcaproveedor) {
        this.marcaproveedor = marcaproveedor;
    }

    public int getStockproducto() {
        return stockproducto;
    }

    public void setStockproducto(int stockproducto) {
        this.stockproducto = stockproducto;
    }

    public int getPprecioproducto() {
        return pprecioproducto;
    }

    public void setPprecioproducto(int pprecioproducto) {
        this.pprecioproducto = pprecioproducto;
    }

    public byte[] getPfoto() {
        return pfoto;
    }

    public void setPfoto(byte[] pfoto) {
        this.pfoto = pfoto;
    }

    public byte[] getFoto() {
        return pfoto;
    }

    public void setFoto(byte[] foto) {
        this.pfoto = pfoto;
    }

    public String getPdescripcionproducto() {
        return pdescripcionproducto;
    }

    public void setPdescripcionproducto(String pdescripcionproducto) {
        this.pdescripcionproducto = pdescripcionproducto;
    }

    public int getPtipocomponente() {
        return ptipocomponente;
    }

    public void setPtipocomponente(int ptipocomponente) {
        this.ptipocomponente = ptipocomponente;
    }
    
    
}
