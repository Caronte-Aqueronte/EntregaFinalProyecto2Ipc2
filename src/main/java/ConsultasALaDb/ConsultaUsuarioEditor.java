/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
import modelos.Revista;
import modelos.TagRevista;
import java.sql.*;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaUsuarioEditor extends ConsultaUsuario {

    public ConsultaUsuarioEditor(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    /**
     * Este metodo verifica que la revista no exista para el editor, de ser asi
     * relega la insercion a otro metodo; de lo contrario inserta una nueva
     * tupla a la tabla revista en la base de datos.
     *
     * @param revista
     * @return
     */
    public String publicarRevista(Revista revista) {
        try {
            //verificar que el usuario no tenga una revista con el mismo nombre, si la tiene entoncs mandamos a crear una nueva edicion
            if (saberSiRevistaPerteneceAUsuario(revista)) {

            } else {//si no entonces solo insertamos la nueva revista en la bd
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO revista VALUES (?,?,?,?,?,?,?,?,?,?,?);");
                //damos los valores de los "?"
                query.setString(1, revista.getNombreRevista());
                query.setString(2, revista.getDescripcion());
                query.setString(3, revista.getUsuarioCreador());
                query.setString(4, revista.getCategoria());
                query.setBlob(5, revista.getContenido());
                query.setBlob(6, revista.getMiniatura());
                query.setDouble(7, revista.getCostoDeSuscripcion());
                query.setString(8, revista.getFechaDePublicacion());
                query.setString(9, revista.getEstadoSuscripcion());
                query.setString(10, revista.getEstadoComentarios());
                query.setString(11, revista.getEstadoLikes());
                if (query.executeUpdate() > 0) {//si el valor de las filas afectadas es mayo a 0 entonces se inserto la tupla
                    CONEXION.commit();//hacemos el commit
                    return "Se publico tu revista \"" + revista.getNombreRevista() + "\" con exito.";
                } else {// si no entonces no se publico la revista
                    return "No se publico tu revista \"" + revista.getNombreRevista() + "\".";
                }
            }
            return "No se publico tu revista \"" + revista.getNombreRevista() + "\".";
        } catch (SQLException ex) {
            return "No se publico tu revista \"" + revista.getNombreRevista() + "\".";
        }
    }

    /**
     * Este metodo busca una tupla con el nombre del usuario y el nombre de la
     * revista dentro de la tabla revista contenida en la base de datos,
     * devuelve true o false dependiendo si existe o no dicha tupla.
     *
     * @param revista
     * @return
     */
    public boolean saberSiRevistaPerteneceAUsuario(Revista revista) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM revista WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");
            query.setString(1, revista.getNombreRevista());//dar valores a los ?
            query.setString(2, revista.getUsuarioCreador());//
            ResultSet resultado = query.executeQuery();
            while (resultado.next()) {//si entra al while etnocnes hay una tupla contedinda y la revista existe
                return true;
            }//si no entra entonces el usuario no tiene la revista
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Este metodo inserta una nueva tupla a la tabla tagrevista con los datos
     * que contenga el objeto TagRevista que recibe como parametro
     *
     * @param tag
     * @return
     */
    public String guardarTagDeRevista(TagRevista tag) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "INSERT INTO tag_revista VALUES (?,?,?);");//query que inserta un nuevo tag en la tabla tag_rvista de la bd
            query.setString(1, tag.getNombreTag());//
            query.setString(2, tag.getNombreRevista());//
            query.setString(3, tag.getUsuarioCreador());//
            if (query.executeUpdate() > 0) {
                CONEXION.commit();
                return "Se inserto el tag \"" + tag.getNombreTag() + "\" a tu revista";
            }
            return "No inserto el tag \"" + tag.getNombreTag() + "\" a tu revista";
        } catch (SQLException ex) {
            return "No inserto el tag \"" + tag.getNombreTag() + "\" a tu revista";
        }
    }
    
    public String guardarEstadosDeInteracciones(Revista revista){
        try {
            PreparedStatement query =CONEXION.prepareStatement(//query que modifica las interacciones de una revista
            "UPDATE revista SET estado_de_suscripciones = ?, estado_de_comentarios = ?, "
                    + "estado_de_likes = ? WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");
            query.setString(1, revista.getEstadoSuscripcion());//le damos valores a los ?
            query.setString(2, revista.getEstadoComentarios());//
            query.setString(3, revista.getEstadoLikes());//
            query.setString(4, revista.getNombreRevista());//
            query.setString(5, revista.getUsuarioCreador());//
            if(query.executeUpdate() > 0){//si se edito mas de 0 entonces si surieron efecto los cambios
                CONEXION.commit();//realizamos el commit y respondemos al usuario
                return "Se guardaron tus configuraciones con exito.";
            }
            return "No se guardaron tus configuraciones.";
        } catch (SQLException e) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex) {
            }
            return "No se actualizaron tus configuraciones debido a un error con el servidor";
        }
    }
}
