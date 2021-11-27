/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import ConexionAMySql.ConexionSql;
import ModelosApi.*;
import java.sql.*;
import java.util.ArrayList;

public abstract class Consulta extends ConexionSql {

    private ConstructorDeObjeto constructorObjeto;

    public Consulta(ConstructorDeObjeto constructorObjeto) {
        this.constructorObjeto = constructorObjeto;
    }

    //getter
    public ConstructorDeObjeto getConstructorObjeto() {
        return constructorObjeto;
    }

    /**
     * Este metodo se comunica con la base de datos y busca el nombre del
     * usuario que recibe como parametro dentro de la tabla usuario, si existe
     * deulve true de lo contrario devuelve false.
     *
     * @param nombreUsuario
     * @return
     */
    public boolean saberSiExisteUsuario(String nombreUsuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(//query que manda a traer todos los usuarios que coniciden conese nombre
                    "SELECT * FROM usuario WHERE nombre_de_usuario = ?;");
            query.setString(1, nombreUsuario);//dar valor al ?
            ResultSet resultado = query.executeQuery();//ejecutar la query
            while (resultado.next()) {//si este while itera entonces exite el usuario
                return true;//retornar ture
            }
            //si el while no itero entonces retornamos false
            System.out.println("No entro while");
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Este metodo se comunica con la base de datos y manda a traer el usuario
     * que pertenece al nombre de usuario que se recibe como parametro.
     *
     * @param nombreUsuario
     * @return
     */
    public boolean saberSiExistePerfil(String nombreUsuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(//query que manda a traer todos los usuarios que coniciden conese nombre
                    "SELECT * FROM perfil WHERE nombre_de_usuario = ?");
            query.setString(1, nombreUsuario);//dar valores al ?
            ResultSet resultado = query.executeQuery();//ejecutar query
            while (resultado.next()) {//si entra al while el perfil existe
                return true;
            }//si no entra el while el perfil no existe
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Este metodo obtiene el perfil de un usuario mediante el string que recibe
     * si no encuentra nada entonces retornara un perfil vacio
     *
     * @param nombreUsuario
     * @return
     */
    public Perfil obtenerPerfilDeUsuario(String nombreUsuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(//query que manda a traer todos los usuarios que coniciden conese nombre
                    "SELECT b.descripcion, b.hobbies FROM usuario a INNER JOIN perfil b ON"
                    + " a.nombre_de_usuario = b.nombre_de_usuario WHERE a.nombre_de_usuario = ?;");
            query.setString(1, nombreUsuario);
            ResultSet resultado = query.executeQuery();
            Perfil perfil = constructorObjeto.construirPerfil(resultado);
            return perfil;
        } catch (SQLException ex) {
            return new Perfil("", "");
        }
    }

    /**
     * Este metodo se comunica con la base de datos y verifica si el tag que se
     * le envia como parametro existe y esta asignado al usuario en cuestion.
     *
     * @param tagAVerificar
     * @return
     */
    public boolean saberSiUsuarioTieneTag(TagUsuario tagAVerificar) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(//query que manda a traer todas las tuplas que coincidan  con las condiciones
                    "SELECT * FROM tag_usuario WHERE nombre_tag = ? AND nombre_de_usuario = ?;");
            query.setString(1, tagAVerificar.getNombreTag());//dar valores a los ?
            query.setString(2, tagAVerificar.getNombreDeUsuario());//
            ResultSet resultado = query.executeQuery();//ejecutar la query
            while (resultado.next()) {//si entra al while etnonces existe ese tag
                return true;
            }//si no entro al while etnonces no existe ese tag
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Este metodo consulta a la base de datos pidiendo todas las tuplas de la
     * tabla categoria, consigue un array de Categoria y lo devuelve.
     *
     * @return
     */
    public ArrayList<Categoria> devolverCategorias() {
        try {
            Statement query = CONEXION.createStatement();//creamos el statement que llevara la query sql
            ResultSet resultado = query.executeQuery(
                    "SELECT * FROM categoria;");//esta query devolvera todo el contenido de la tabla categoria en la bd
            //mandamos a crear el array de categorias a partir del ResultSet con la query ejecutada
            ArrayList<Categoria> categorias = constructorObjeto.crearArrayDeCategorias(resultado);
            return categorias;//devolver el arraylist
        } catch (SQLException ex) {
            return new ArrayList<Categoria>();//si hay error devolvemos un vacio
        }
    }

}
