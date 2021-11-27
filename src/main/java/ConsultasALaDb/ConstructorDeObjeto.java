package ConsultasALaDb;

import ModelosApi.*;
import java.sql.*;
import java.util.ArrayList;

public class ConstructorDeObjeto {

    /**
     * Este metodo crea un objeto Perfil apartir de explorar el ResultSet que
     * recibe como parametro
     *
     * @param resultSet
     * @return
     */
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

    /**
     * Este metodo crea un objeto Usuario apartir de explorar el ResultSet que
     * recibe como parametro
     *
     * @param resultSet
     * @return
     */
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

    /**
     * Este metodo crea un array de objetos Tag apartir de la exploracion del
     * ResultSet qeu recibe como parametro
     *
     * @param resultSet
     * @return
     */
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

    /**
     * Este metodo construye un array list de varios objetos Categoria
     * construidos a partir del ResultSet que recibe como parametroF
     *
     * @param resultSet
     * @return
     */
    public ArrayList<Categoria> crearArrayDeCategorias(ResultSet resultSet) {
        ArrayList<Categoria> categorias = new ArrayList<>();//crear un array nuevo de categorias si encaso hay error se devolvera vacia
        try {
            String nombreCategoria;//nombre de la categoria que se registrara en el array
            while (resultSet.next()) {
                nombreCategoria = resultSet.getString("nombre_de_categoria");//traermos el nombre la categoria
                categorias.add(new Categoria(nombreCategoria));//anadimos la nueva categoria
            }//acabado el while devolvemos el array
            return categorias;
        } catch (SQLException ex) {
            return categorias;//si hay error entonces devolvemos el array independiente de su estado
        }
    }
}
