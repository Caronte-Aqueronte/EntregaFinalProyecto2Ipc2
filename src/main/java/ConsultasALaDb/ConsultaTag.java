/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import ModelosApi.Tag;
import ModelosApi.TagUsuario;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaTag extends Consulta {

    /**
     * Constructor de la clase ConsultaTag que inicializa a su clase padre
     * Consulta
     *
     * @param constructorObjeto
     */
    public ConsultaTag(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    /**
     * Este metodo devuelve todos los tags que hayan disponibles en la base de
     * datos dentro de la tabla tag.
     *
     * @return
     */
    public ArrayList<Tag> traerTodosLosTags() {
        ArrayList<Tag> tags;
        try {
            Statement query = CONEXION.createStatement();
            ResultSet resultado = query.executeQuery(
                    "SELECT * FROM tag;");//query que trae toda la tabla de tags
            tags = getConstructorObjeto().crearArrayDeTags(resultado);//en base al resultset construimos el array de tags
            return tags;//retornamos todos los tags creados
        } catch (SQLException ex) {
            return tags = new ArrayList<>();//siu hay error retornamos un array vacio
        }
    }

    /**
     * Este metodo devuelve todas las tags que tenga un usuario en la tabla
     * tag_usuario dentro de la base de datos
     *
     * @param nombreUsuario
     * @return
     */
    public ArrayList<Tag> traerTodosLosTagsDeUnUsuario(String nombreUsuario) {
        ArrayList<Tag> tags;
        try {
            PreparedStatement query = CONEXION.prepareStatement("SELECT * FROM tag_usuario WHERE nombre_de_usuario = ?;");
            query.setString(1, nombreUsuario);//damos el valor al ?
            ResultSet resultado = query.executeQuery();
            tags = getConstructorObjeto().crearArrayDeTags(resultado);//en base al resultset construimos el array de tags
            return tags;//retornamos todos los tags creados
        } catch (SQLException ex) {
            return tags = new ArrayList<>();//siu hay error retornamos un array vacio
        }
    }

    /**
     * Este metodo verifica que la tag este asignada al usuario, y luego la
     * elimina devolviendo un mensaje de confirmacion
     *
     * @param tagAEliminar
     * @return
     */
    public String eliminarTagDeUsuario(TagUsuario tagAEliminar) {
        try {
            //verificamos que la tag sea del usuario
            if (this.saberSiUsuarioTieneTag(tagAEliminar)) {
                CONEXION.setAutoCommit(false);//quitamos el autocommit
                PreparedStatement query = CONEXION.prepareStatement(
                        "DELETE FROM tag_usuario WHERE nombre_tag = ? AND nombre_de_usuario = ?;");
                query.setString(1, tagAEliminar.getNombreTag());
                query.setString(2, tagAEliminar.getNombreDeUsuario());
                if (query.executeUpdate() > 0) {//si se cumple el if entonces se elimino el tag y retornamos el mensjae de confirmacion
                    CONEXION.commit();//se hace el commit
                    return "Se elimino el tag \"" + tagAEliminar.getNombreTag() + "\" de tu usuario con exito.";//retornar confirmacion
                } else {//si no se afecto ninguna fila entonces retornamos confirmacion
                    return "No se elimino el tag \"" + tagAEliminar.getNombreTag() + "\" de tu usuario.";
                }
            } else {//si no tiene el tag entonces no hacemos nada mas que mandar la confirmacion
                return "No se elimino el tag \"" + tagAEliminar.getNombreTag() + "\" de tu usuario debido a que no la poseés.";
            }

        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex1) {

            }
            return "No se elimino el tag \"" + tagAEliminar.getNombreTag() + "\" de tu usuario, debido a un error inesperado.";
        }
    }
    
   /**
     * Este metodo verifica que la tag este asignada al usuario, y luego la
     * elimina devolviendo un mensaje de confirmacion
     *
     * @param tagAgregar
     * @return
     */
    public String agregarTagUsuario(TagUsuario tagAgregar) {
        try {
            //verificamos que el tag no lo tenga ya el usuario
            if (!this.saberSiUsuarioTieneTag(tagAgregar)) {
                CONEXION.setAutoCommit(false);//quitamos el autocommit
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO tag_usuario VALUES (?,?)");//query que inserta una nueva tupla
                query.setString(1, tagAgregar.getNombreTag());
                query.setString(2, tagAgregar.getNombreDeUsuario());
                if (query.executeUpdate() > 0) {//si se cumple el if entonces se agrego el tag y retornamos el mensjae de confirmacion
                    CONEXION.commit();//se hace el commit
                    return "Se registro el tag \"" + tagAgregar.getNombreTag() + "\" a tu usuario con exito.";//retornar confirmacion
                } else {//si no se afecto ninguna fila entonces retornamos confirmacion
                    return "No se agrego el tag \"" + tagAgregar.getNombreTag() + "\" a tu usuario.";
                }
            } else {//si lo tiene entonces solo devolvemos el mensaje de confrimacion
                return "No se inserto el tag \"" + tagAgregar.getNombreTag() + "\" a tu usuario debido a que ya lo poseés.";
            }

        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex1) {

            }
            return "No se insterto el tag \"" + tagAgregar.getNombreTag() + "\" a tu usuario, debido a un error inesperado.";
        }
    }
}
