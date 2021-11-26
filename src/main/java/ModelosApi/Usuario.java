package ModelosApi;

public class Usuario {

    private String usuario;
    private String password;
    private String rol;
    
    public Usuario(String usuario, String password, String rol) {
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public Usuario(String usuario) {
        this.usuario = usuario;
    }

    public Usuario() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    


}
