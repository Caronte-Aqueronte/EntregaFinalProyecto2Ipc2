
package ModelosApi;

public class ModeloResponseLogin {
    private Usuario usuario;
    private boolean banderaExisteUsuario;

    public ModeloResponseLogin(Usuario usuario, boolean banderaExisteUsuario) {
        this.usuario = usuario;
        this.banderaExisteUsuario = banderaExisteUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isBanderaExisteUsuario() {
        return banderaExisteUsuario;
    }

    public void setBanderaExisteUsuario(boolean banderaExisteUsuario) {
        this.banderaExisteUsuario = banderaExisteUsuario;
    }

    
    
}
