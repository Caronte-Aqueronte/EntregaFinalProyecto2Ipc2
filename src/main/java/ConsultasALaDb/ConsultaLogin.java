package ConsultasALaDb;

import modelos.Usuario;
import modelos.ModeloResponseLogin;
import herramientas.ConstructorDeObjeto;
import herramientas.Encriptador;
import java.sql.*;

public class ConsultaLogin extends Consulta {

    private Encriptador enciptador;

    public ConsultaLogin(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
        this.enciptador = new Encriptador();
    }

    public ModeloResponseLogin hacerLogin(Usuario usuario) {
        String passwordEncriptada = enciptador.encriptarPassword(usuario.getPassword());
        try {
            if (saberSiExisteUsuario(usuario.getUsuario()) == true) {
                PreparedStatement query = CONEXION.prepareStatement(//query que trae todos los usuario que coincidan con la query
                        "SELECT * FROM usuario WHERE nombre_de_usuario = ? AND password = ?");
                query.setString(1, usuario.getUsuario());//dar valores a los ?
                query.setString(2, passwordEncriptada);
                ResultSet resultado = query.executeQuery();
                Usuario usuarioParaResponse = getConstructorObjeto().crearUsuario(resultado);
                if (!usuarioParaResponse.getUsuario().equals("")) {
                    ModeloResponseLogin modeloResponse = new ModeloResponseLogin(usuarioParaResponse, true);
                    return modeloResponse;
                } else {
                    ModeloResponseLogin modeloResponse = new ModeloResponseLogin(usuarioParaResponse, false);
                    return modeloResponse;
                }
            } else {
                return new ModeloResponseLogin(new Usuario("", "", ""), false);
            }

        } catch (SQLException ex) {
            return null;
        }
    }

}
