/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
import java.io.InputStream;
import java.sql.*;

import java.util.ArrayList;
import modelos.*;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaRevista extends Consulta {

    public ConsultaRevista(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public InputStream traerCaratulaPdf(String nombreUsuario, String usuarioCreador) {
        try {

            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT miniatura FROM revista WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");//statement que guardara el blob de la foto de perfil
            query.setString(1, nombreUsuario);//dar valor al ?
            query.setString(2, usuarioCreador);//dar valor al ?
            ResultSet resultado = query.executeQuery();//ejecutamos la query
            while (resultado.next()) {//si entra al while etonces exite la foto de usuario
                return resultado.getBlob("miniatura").getBinaryStream();//obtener el blod y apartir de el obtener el InputStream
            }
            return null;

        } catch (SQLException ex) {
            return null;
        }
    }

    public InputStream traerPdf(String nombreUsuario, String usuarioCreador) {
        try {

            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT contenido FROM revista WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");//manda a traer el blob que representa el df de la revista
            query.setString(1, nombreUsuario);//dar valor al ?
            query.setString(2, usuarioCreador);//dar valor al ?
            ResultSet resultado = query.executeQuery();//ejecutamos la query
            while (resultado.next()) {//si entra al while etonces exite el pdf de usuario
                return resultado.getBlob("contenido").getBinaryStream();//obtener el blod y apartir de el obtener el InputStream
            }
            return null;

        } catch (SQLException ex) {
            return null;
        }
    }

    public ArrayList<Revista> buscarRevistasPorCategoria(String busqueda) {
        ArrayList<Revista> revistas = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM revista WHERE categoria = ?;");//query que selecciona todas las revistas que cumplan con la condicion en la categoria
            query.setString(1, busqueda);//damos valor al ?
            ResultSet resultado = query.executeQuery();//ejecutar la query
            while (resultado.next()) {
                //se traen los valores en base a los nombres de cada columna
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                //se contruye un objeto de tipo Revista a partir de los datos y se agrega al array
                revistas.add(new Revista(nombreRevista, descripcion, categoria, usuarioCreador));
            }
            //retornar las revistas
            return revistas;
        } catch (SQLException ex) {
            //en caso de error retornar array vacio
            return revistas;
        }
    }

    public ArrayList<Revista> buscarRevistasPorTag(String busqueda) {
        ArrayList<Revista> revistas = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM revista a INNER JOIN tag_revista b ON"
                    + " a.nombre_de_revista = b.nombre_de_revista AND b.nombre_tag = ?;");//query que selecciona todas las revistas que cumplan con la condicion en la tag buscada
            query.setString(1, busqueda);//damos valor al ?
            ResultSet resultado = query.executeQuery();//ejecutar la query
            while (resultado.next()) {
                //se traen los valores en base a los nombres de cada columna
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                //se contruye un objeto de tipo Revista a partir de los datos y se agrega al array
                revistas.add(new Revista(nombreRevista, descripcion, categoria, usuarioCreador));
            }
            //retornar las revistas
            return revistas;
        } catch (SQLException ex) {
            //en caso de error retornar array vacio
            return revistas;
        }
    }

    public Revista buscarInfoDeRevista(Revista revista) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM revista WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");
            query.setString(1, revista.getNombreRevista());
            query.setString(2, revista.getUsuarioCreador());
            ResultSet resultado = query.executeQuery();
            while (resultado.next()) {
                //obtenemos todos los datos del resultset que nos servira para crear la revista
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                int numeroDeLikes = verLikesDeRevista(revista);
                Revista revistaReturn = new Revista(nombreRevista, descripcion, categoria, usuarioCreador, numeroDeLikes);
                return revistaReturn;
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public int verLikesDeRevista(Revista revista) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM `like` WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");
            query.setString(1, revista.getNombreRevista());
            query.setString(2, revista.getUsuarioCreador());
            ResultSet resultado = query.executeQuery();
            int contadorLikes = 0;
            while (resultado.next()) {
                contadorLikes++;
            }
            return contadorLikes;
        } catch (SQLException e) {
            return 0;
        }
    }

    public ArrayList<Tag> retornarTagsDeRevista(Revista revista) {
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM tag_revista WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?");
            query.setString(1, revista.getNombreRevista());
            query.setString(2, revista.getUsuarioCreador());
            ResultSet resultado = query.executeQuery();//ejecutamos la query y obtenmos el resultset
            while (resultado.next()) {//exploramos todos las tuplas del reusltado
                String nombreTag = resultado.getString("nombre_tag");//extraemos el nombre dle tag para
                Tag tag = new Tag(nombreTag);//creamos un nuevo objeto Tag
                tags.add(tag);//agregamos el tags al array
            }
            return tags;
        } catch (SQLException e) {
            return tags;
        }
    }

    public double retornarPrecioDeRevista(Revista revista) {
        double precio = 0;
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT costo_de_suscripcion FROM revista WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?");
            query.setString(1, revista.getNombreRevista());
            query.setString(2, revista.getUsuarioCreador());
            ResultSet resultado = query.executeQuery();//ejecutamos la query y obtenmos el resultset
            while (resultado.next()) {//exploramos todos las tuplas del reusltado
                precio = resultado.getDouble("costo_de_suscripcion");//extraemos el nombre dle tag para
            }
            return precio;
        } catch (SQLException e) {
            return precio;
        }
    }

    public ArrayList<Comentario> traerComentariosDeUnaRevista(Revista revista) {
        ArrayList<Comentario> comentarios = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM comentario WHERE nombre_de_revista = ? AND "
                    + "revista_nombre_de_usuario_creador = ?;");//manda a taer todos los comentarios que cumplan con la condicion
            query.setString(1, revista.getNombreRevista());//damos valores a los ?
            query.setString(2, revista.getUsuarioCreador());//
            ResultSet resultado = query.executeQuery();//ejecutar la query
            comentarios = getConstructorObjeto().construirArrayDeComentarios(resultado);//traer el array a partir de el resultado
            return comentarios;
        } catch (SQLException ex) {
            return comentarios;
        }
    }

    public ArrayList<Revista> traerTodasLasRevisas() {
        ArrayList<Revista> revistas = new ArrayList<>();
        try {
            Statement query = CONEXION.createStatement();

            ResultSet resultado = query.executeQuery("SELECT * FROM revista;");//ejecutar la query
            while (resultado.next()) {
                //se traen los valores en base a los nombres de cada columna
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                //se contruye un objeto de tipo Revista a partir de los datos y se agrega al array
                revistas.add(new Revista(nombreRevista, descripcion, categoria, usuarioCreador));
            }
            //retornar las revistas
            return revistas;
        } catch (SQLException ex) {
            //en caso de error retornar array vacio
            return revistas;
        }
    }

    public double verCostoPorDiaDeRevista(Revista revista) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(//query que selecciona la tupla que cumpla la condicion ordenada decendentemente segun la fehca
                    "SELECT * FROM costo_por_dia WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?"
                    + " ORDER BY fecha_de_validez DESC");
            query.setString(1, revista.getNombreRevista());//damos valores a los ?
            query.setString(2, revista.getUsuarioCreador());//
            ResultSet resultado = query.executeQuery();//ejecutar la query
            while (resultado.next()) {//explorar todas las tuplas y devolver solo el precio de la primera
                return resultado.getDouble("costo_por_dia");
            }
            return 0;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public ArrayList<Revista> traerTodasLasRevistasDeUnEditor(String nombreUsuario) {
        ArrayList<Revista> revistas = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(//selecciona todas las revistas que tengan el mismo nombre de usario creador
                    "SELECT * FROM revista WHERE nombre_de_usuario_creador = ?;");
            query.setString(1, nombreUsuario);//damos valores a los ??
            ResultSet resultado = query.executeQuery();//ejecutamos la query
            while (resultado.next()) {//recorrecr las tulpas para crear revistas a partir de ellas
                //obtenemos todos los datos del resultset que nos servira para crear la revista
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                Revista revista = new Revista(nombreRevista, descripcion, categoria, usuarioCreador);
                revistas.add(revista);
            }
            return revistas;
        } catch (SQLException e) {
            return revistas;
        }
    }
}
