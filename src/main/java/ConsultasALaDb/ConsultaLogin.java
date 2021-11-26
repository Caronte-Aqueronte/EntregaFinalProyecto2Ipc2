package ConsultasALaDb;

import ModelosApi.*;
import java.sql.*;

public class ConsultaLogin extends Consulta {

    public ConsultaLogin(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public ModeloResponseLogin hacerLogin(Usuario usuario) {
        try {
            if (saberSiExisteUsuario(usuario.getUsuario()) == true) {
                PreparedStatement query = CONEXION.prepareStatement(//query que trae todos los usuario que coincidan con la query
                        "SELECT * FROM usuario WHERE nombre_de_usuario = ? AND password = ?");
                query.setString(1, usuario.getUsuario());//dar valores a los ?
                query.setString(2, usuario.getPassword());
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
