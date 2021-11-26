package ConsultasALaDb;

import ModelosApi.*;
import java.sql.*;
import java.util.ArrayList;

public class ConstructorDeObjeto {

    public Perfil construirPerfil(ResultSet resultSet) {
        try {
            String descripcion = "";
            String hobbies = "";
            while (resultSet.next()) {
                descripcion = resultSet.getString("descripcion");
                hobbies = resultSet.getString("hobbies");
            }
            return new Perfil(descripcion, hobbies);
        } catch (SQLException ex) {
            return new Perfil("", "");
        }
    }

    public Usuario crearUsuario(ResultSet resultSet) {
        try {
            Usuario usuario;
            String nombre = "";
            String password = "";
            String rol = "";
            while (resultSet.next()) {
                nombre = resultSet.getString("nombre_de_usuario");
                password = resultSet.getString("password");
                rol = resultSet.getString("rol");
            }
            usuario = new Usuario(nombre, password, rol);
            return usuario;
        } catch (SQLException ex) {
            return null;
        }
    }

    public ArrayList<Tag> crearArrayDeTags(ResultSet resultSet) {
        ArrayList<Tag> arrayDeTags = new ArrayList<>();
        try {
            String nombreTag;
            while (resultSet.next()) {//exploramos el resultset
                nombreTag = resultSet.getString("nombre_tag");//traemos el nombre del tag
                arrayDeTags.add(new Tag(nombreTag));//anadimos el nuevo tag
            }
            return arrayDeTags;//retornamos el array sin importar que este vacio
        } catch (SQLException ex) {
            return arrayDeTags;//retornamos el array sin importar que este vacio
        }
    }
}
