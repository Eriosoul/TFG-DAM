package Modelo;
// clase de usuarios // objeto
public class MetodoUsuario {

    private int id_user = 0;
    private String nombre;
    private String apellidos;
    private String email;
    private String pass;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccioncasa() {
        return direccioncasa;
    }

    public void setDireccioncasa(String direccioncasa) {
        this.direccioncasa = direccioncasa;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    private String telefono ;
    private String direccioncasa;
    private int tipo = 1;

}
