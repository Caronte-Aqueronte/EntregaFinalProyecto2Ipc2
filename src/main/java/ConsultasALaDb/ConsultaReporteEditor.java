/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
import java.sql.*;
import java.util.ArrayList;
import modelos.*;
import modelosReportes.GananciaPorSuscripcion;
import modelosReportes.LikeDeRevista;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaReporteEditor extends Consulta {

    public ConsultaReporteEditor(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public JRBeanArrayDataSource reporteDeComentarios(String nombreRevista, String primeraFecha, String segundaFecha) {
        try {
            String contenidoQuery;
            PreparedStatement query;
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {//si alguina fecha esta vacia entonces la query cambia
                contenidoQuery = "SELECT * FROM comentario WHERE nombre_de_revista like ? AND DATE(fecha_de_comentario) BETWEEN ? AND ?";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//damos valores alos ? con los parametros del emtodo
                query.setString(2, primeraFecha);//
                query.setString(3, segundaFecha);//
            } else {//si no etonces dejamos la query con fechas
                contenidoQuery = "SELECT * FROM comentario WHERE nombre_de_revista like ?";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");
            }

            ResultSet resultado = query.executeQuery();//ejecutar la query
            ArrayList<Comentario> comentarios = getConstructorObjeto().construirArrayDeComentarios(resultado);//construir el array apartir del resultado
            comentarios.add(0, new Comentario("", null, "", "", ""));//agrefar un comentario extra pues no se muestra el primero
            JRBeanArrayDataSource datos = new JRBeanArrayDataSource(comentarios.toArray());//enviamos el array list como array al Jr
            return datos;
        } catch (SQLException ex) {
            return null;
        }
    }

    public JRBeanArrayDataSource reporteDeSuscripciones(String nombreRevista, String primeraFecha, String segundaFecha) {
        try {
            String contenidoQuery;
            PreparedStatement query;
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {//si alguina fecha esta vacia entonces la query cambia
                contenidoQuery = "SELECT * FROM suscripcion WHERE nombre_de_revista like ? AND DATE(fecha_de_suscripcion) BETWEEN ? AND ?";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//damos valores alos ? con los parametros del emtodo
                query.setString(2, primeraFecha);//
                query.setString(3, segundaFecha);//
            } else {//si no etonces dejamos la query con fechas
                contenidoQuery = "SELECT * FROM suscripcion WHERE nombre_de_revista like ?";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");
            }

            ResultSet resultado = query.executeQuery();//ejecutar la query
            ArrayList<Suscripcion> suscripciones = getConstructorObjeto().construirSuscripciones(resultado);//construir el array apartir del resultado
            suscripciones.add(0, new Suscripcion("", "", "", "", ""));//agrefar un comentario extra pues no se muestra el primero
            JRBeanArrayDataSource datos = new JRBeanArrayDataSource(suscripciones.toArray());//enviamos el array list como array al Jr
            return datos;
        } catch (SQLException ex) {
            return null;
        }
    }

    public ArrayList<LikeDeRevista> reporteRevistasMasLikeadas(String nombreRevista, String primeraFecha, String segundaFecha) {
        try {
            String contenidoQuery;
            PreparedStatement query;
            //lo primero es preprarar un JRBeanArrayDataSource con el listado de las 5 revistas mas ikeadas
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {//si alguina fecha esta vacia entonces la query cambia
                //esta query cuenta las veces que aparece una revista y las ordena de mayor a menor
                contenidoQuery = "SELECT * , COUNT(nombre_de_revista AND nombre_de_usuario_creador) AS numero_de_likes FROM "
                        + "`like` WHERE nombre_de_revista like ? AND DATE(fecha_de_like) "
                        + "BETWEEN ? AND ? GROUP BY nombre_de_revista, nombre_de_usuario_creador ORDER BY numero_de_likes DESC";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//damos valores alos ? con los parametros del emtodo
                query.setString(2, primeraFecha);//
                query.setString(3, segundaFecha);//
            } else {//si no etonces dejamos la query con fechas
                contenidoQuery = "SELECT * , COUNT(nombre_de_revista AND nombre_de_usuario_creador) AS numero_de_likes FROM "
                        + "`like` WHERE nombre_de_revista like ? GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador ORDER BY numero_de_likes DESC";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");
            }
            ResultSet resultado = query.executeQuery();

            ArrayList<LikeDeRevista> likes = getConstructorObjeto().likesDeRevista(resultado);//construimos el array de likes
            return likes;
        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayList<LikeDeRevista> likesPorRevista(ArrayList<LikeDeRevista> revistasMasLikeadas, String primeraFecha, String segundaFecha) {
        ArrayList<LikeDeRevista> likesPorRevista = new ArrayList<>();
        try {
            PreparedStatement query;

            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                for (LikeDeRevista item : revistasMasLikeadas) {//recorrres cada elemento del array y traer sus likes
                    query = CONEXION.prepareStatement("SELECT * FROM `like` WHERE nombre_de_revista = "
                            + "? AND nombre_de_usuario_creador = ? AND fecha_de_like BETWEEN ? AND ?;");//esta query trae todos los likes de la revista en el iterador
                    query.setString(1, item.getNombreRevista());//damos valores a los ? con los atributos del iterador
                    query.setString(2, item.getUsuarioCreador());//
                    query.setString(3, primeraFecha);//
                    query.setString(4, segundaFecha);//
                    ResultSet resultado2 = query.executeQuery();//ejecutar la query
                    ArrayList<LikeDeRevista> likeDeEstaRevista = getConstructorObjeto().likesDeUnaRevista(resultado2);
                    likesPorRevista.addAll(likeDeEstaRevista);
                }
            } else {
                for (LikeDeRevista item : revistasMasLikeadas) {//recorrres cada elemento del array y traer sus likes
                    query = CONEXION.prepareStatement("SELECT * FROM `like` WHERE nombre_de_revista = "
                            + "? AND nombre_de_usuario_creador = ?;");//esta query trae todos los likes de la revista en el iterador
                    query.setString(1, item.getNombreRevista());//damos valores a los ? con los atributos del iterador
                    query.setString(2, item.getUsuarioCreador());//
                    ResultSet resultado2 = query.executeQuery();//ejecutar la query
                    ArrayList<LikeDeRevista> likeDeEstaRevista = getConstructorObjeto().likesDeUnaRevista(resultado2);
                    likesPorRevista.addAll(likeDeEstaRevista);
                }
            }

            return likesPorRevista;
        } catch (Exception e) {
            return likesPorRevista;
        }
    }

    public ArrayList<GananciaPorSuscripcion> reporteSuscripciones(String nombreRevista,
            String usuarioCreador, String primeraFecha, String segundaFecha) {
        ArrayList<GananciaPorSuscripcion> gananciasPorSuscripciones = new ArrayList<>();
        try {
            String contenidoQuery;
            PreparedStatement query;
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {

                //si alguina fecha esta vacia entonces la query cambia
                //condencsa todos los pagos de una suscripcion
                contenidoQuery = "SELECT * , sum(dinero_pago - (dinero_pago * 0.15)) AS ganancia FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "AND fecha_de_pago BETWEEN ? AND ? GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador, nombre_de_suscriptor "
                        + "ORDER BY ganancia DESC";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//damos valores alos ? con los parametros del emtodo
                query.setString(2, "%" + usuarioCreador + "%");
                query.setString(3, primeraFecha);//
                query.setString(4, segundaFecha);//

            } else {//si no etonces dejamos la query sin fechas
                contenidoQuery = "SELECT * , sum(dinero_pago - (dinero_pago * 0.15)) AS ganancia FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "GROUP BY nombre_de_revista, nombre_de_usuario_creador, nombre_de_suscriptor "
                        + "ORDER BY ganancia DESC";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");
                query.setString(2, "%" + usuarioCreador + "%");
            }
            ResultSet resultado = query.executeQuery();//ejecutar la query independiente de su contenido
            gananciasPorSuscripciones = getConstructorObjeto().construirGanancias(resultado);//construimos el array con el resultset
            return gananciasPorSuscripciones;
        } catch (SQLException ex) {
            return gananciasPorSuscripciones;
        }

    }

    public double calcularTotalDeGanancias(ArrayList<GananciaPorSuscripcion> ganancias) {
        double total = 0;
        //exploramos el array y por cada iteracion sumamos el total con el total individual por suscripcion
        for (GananciaPorSuscripcion item : ganancias) {
            total += item.getTotalDeGanancia();
        }
        return total;
    }
}
