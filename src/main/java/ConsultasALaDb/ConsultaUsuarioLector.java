/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import modelos.Revista;
import modelos.Suscripcion;
import herramientas.ConstructorDeObjeto;
import herramientas.ManejadorDeFecha;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import modelos.Comentario;
import modelos.Pago;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaUsuarioLector extends ConsultaUsuario {

    /**
     * Constructor de la clase ConsultaUsuarioLector que inicializa la clase
     * padre
     *
     * @param constructorObjeto
     */
    public ConsultaUsuarioLector(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public ArrayList<Revista> verSuscripcionesDeUsuario(String nombreUsuario) {
        ArrayList<Revista> revistas = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT a.nombre_de_revista, a.descripcion, a.categoria, a.nombre_de_usuario_creador FROM "
                    + "revista a INNER JOIN suscripcion b ON a.nombre_de_revista = b.nombre_de_revista "
                    + " AND a.nombre_de_usuario_creador = b.nombre_de_usuario_creador "
                    + "AND b.nombre_de_suscriptor = ?;");//query que selecciona todas las revistas que cumplan con la condicion en la tag buscada
            query.setString(1, nombreUsuario);//damos valor al ?
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

    public boolean verSiUsuarioEstaSuscritoARevista(Suscripcion consulta) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM suscripcion WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ? "
                    + "AND nombre_de_suscriptor = ?;");//Esta query selecciona todas las tupls que cumplan con las condiciones
            query.setString(1, consulta.getNombreRevista());//*Damos valores reales a los ?
            query.setString(2, consulta.getUsuarioCreador());//*
            query.setString(3, consulta.getNombreUsuario());//*
            ResultSet resultado = query.executeQuery();//ejecutar el query
            //explorar el query con .next() y si entra al while retornar true de lo contrario retornar false
            while (resultado.next()) {
                return true;
            }
            return false;//si no entro al while etnonces el usuario no esta suscrito y retnornamos false
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Este metodo selecciona 15 revistas al azar que posean los tags que el
     * usuario tiene asignado.
     *
     * @param nombreUsuario
     * @return
     */
    public ArrayList<Revista> recomendarRevistas(String nombreUsuario) {
        ArrayList<Revista> revistas = new ArrayList<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT a.nombre_de_revista, a.descripcion, a.categoria, a.nombre_de_usuario_creador FROM revista a"
                    + " INNER JOIN tag_revista b INNER JOIN tag_usuario c"
                    + " ON a.nombre_de_revista = b.nombre_de_revista AND b.nombre_tag = c.nombre_tag "
                    + "AND c.nombre_de_usuario = ? ORDER BY RAND() LIMIT 15;");
            query.setString(1, nombreUsuario);
            ResultSet resultado = query.executeQuery();
            while (resultado.next()) {
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                revistas.add(new Revista(nombreRevista, descripcion, categoria, usuarioCreador));
            }
            return revistas;
        } catch (SQLException ex) {
            return revistas;
        }
    }

    public String suscribirseARevista(Suscripcion suscripcion) {
        try {
            if (saberEstadoDeInteraccionesConRevista(suscripcion.getNombreRevista(),//revisamos que la revista recibe suscriptores nuevos
                    suscripcion.getUsuarioCreador()).isEstadoSuscripciones() == true) {
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO suscripcion VALUES (?,?,?,?,?);");//esta query insert un nuevo registro en la tbla suscripcion
                query.setString(1, suscripcion.getNombreRevista());//seteamos los valores de los ?
                query.setString(2, suscripcion.getUsuarioCreador());//
                query.setString(3, suscripcion.getNombreUsuario());//
                query.setString(4, suscripcion.getMetodoPago());//
                query.setString(5, suscripcion.getFechaSuscripcion());//
                if (query.executeUpdate() > 0) {//si se afecto mas de una fila entonces hacemos el pago
                    //insertamos el primer pago de la suscripcion, primero obtenemos el costo de la rvista
                    ConsultaRevista consultaRevista = new ConsultaRevista(getConstructorObjeto());
                    double precioRevista = consultaRevista.retornarPrecioDeRevista(new Revista(suscripcion.getNombreRevista(), suscripcion.getUsuarioCreador()));
                    double pago = 0;
                    //obtenemos el dinero del pago a partir del metodo de pago
                    if (suscripcion.getMetodoPago().equals("Anual")) {
                        pago = precioRevista * 12;
                    } else {
                        pago = precioRevista;
                    }
                    PreparedStatement queryPago = CONEXION.prepareStatement(//insterta una nueva tupla en la tabla pago
                            "INSERT INTO pago VALUES (?,?,?,?,?);");//
                    queryPago.setString(1, suscripcion.getNombreRevista());//
                    queryPago.setString(2, suscripcion.getUsuarioCreador());//
                    queryPago.setString(3, suscripcion.getNombreUsuario());//
                    queryPago.setDouble(4, pago);//
                    queryPago.setString(5, suscripcion.getFechaSuscripcion());//
                    if (queryPago.executeUpdate() > 0) {
                        CONEXION.commit();
                        return "Se registro tu suscripcion con exito.";
                    }
                    return "No se logro registrar tu suscripcion.";
                }
                return "No se logro registrar tu suscripcion.";
            } else {
                return "No se logro registrar tu suscripcion debido a que la revista "
                        + "no tiene habilidata la opcion de recibir suscriptores nuevos.";
            }

        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex2) {

            }
            return "No se logro registrar tu suscripcion. Intentalo mas tarde";
        }
    }

    public String hacerPago(Pago pago) {
        try {
            ConsultaRevista consultaRevista = new ConsultaRevista(this.getConstructorObjeto());//esta consulta servira para obtener el precio de suscripcion de una revista
            //vemos que tipo de suscripcion tiene el usuairo mandando a traer la revista especidficada
            Suscripcion suscripcion = traerSuscripcion(new Suscripcion(pago.getNombreUsuario(), pago.getNombreRevista(), pago.getUsuarioCreador()));
            Revista revista = new Revista(pago.getNombreRevista(), pago.getUsuarioCreador());//revista que servira para traer el precio de suscripcion a esa revista
            Pago ultimoPago = traerUltimoPagoDeSuscripcion(suscripcion);//mandamos a traer el ultimo pago realizado por el usuario
            int tiempoEntreFechas = 0;//inicializamos el tiempo que hay entre la fecha de pago y la fecha del sistema
            String fechaPago = pago.getFechaPago();//traemos la fecha de pago
            ManejadorDeFecha manejadorDeFecha = new ManejadorDeFecha();//creamos un manejador de fechas
            double precio = consultaRevista.retornarPrecioDeRevista(revista);//este es el precio por suscripcion de uina revista
            double totalAPagar = 0;
            //obtenemos cuanto tiene que pagar el usuario sefun la fecha de pago actual con la ultima fecha de pago del mismo
            switch (suscripcion.getMetodoPago()) {//vemos si se trata de manera mensual o anual y asi ejecutamos un metodo del maejador de fecha
                case "Anual":
                    tiempoEntreFechas = manejadorDeFecha.verAniosEntreFechas(ultimoPago.getFechaPago(), fechaPago);
                    System.out.println(tiempoEntreFechas);
                    if (tiempoEntreFechas <= 0) {
                        return "No a pasado un año entre tu ultima fecha de pago y tu nuevo pago";
                    }
                    totalAPagar = (precio * 12) * (tiempoEntreFechas);
                    break;
                case "Mensual":
                    tiempoEntreFechas = manejadorDeFecha.verMesesEntreFechas(ultimoPago.getFechaPago(), fechaPago);
                    if (tiempoEntreFechas <= 0) {
                        return "No a pasado un mes entre tu ultima fecha de pago y tu nuevo pago";
                    }
                    totalAPagar = precio * (tiempoEntreFechas);
                    break;
            }
            PreparedStatement queryPago = CONEXION.prepareStatement(
                    "INSERT INTO pago VALUES (?,?,?,?,?)");//inserta una nueva tupla a pago
            queryPago.setString(1, pago.getNombreRevista());//dar valores alos ?
            queryPago.setString(2, pago.getUsuarioCreador());//
            queryPago.setString(3, pago.getNombreUsuario());//
            queryPago.setDouble(4, totalAPagar);//
            queryPago.setString(5, pago.getFechaPago());//

            if (queryPago.executeUpdate() > 0) {//si al ejecutar la query se modifica mas de una tupla entonces mandamos conirmacions
                CONEXION.commit();
                return "Se realizo el pago con exito.";
            }
            return "No se realizo tu pago";
        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return "Error en el servidor. Intentelo de nuevo";
        }
    }

    public Pago traerUltimoPagoDeSuscripcion(Suscripcion suscripcion) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(//query que busca todos los pagos hechos por el usuario y la primera tupla es la ultima fecha de pago
                    "SELECT * FROM pago WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ? AND nombre_de_suscriptor = ?"
                    + "ORDER BY fecha_de_pago DESC;");
            query.setString(1, suscripcion.getNombreRevista());//damos valores alos ?
            query.setString(2, suscripcion.getUsuarioCreador());//
            query.setString(3, suscripcion.getNombreUsuario());//
            ResultSet resutado = query.executeQuery();//ejecutar la query
            Pago ultimoPago = getConstructorObjeto().crearPago(resutado);//mandar a crear un pago
            return ultimoPago;
        } catch (SQLException e) {
            return null;
        }
    }

    public Suscripcion traerSuscripcion(Suscripcion suscripcion) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT * FROM suscripcion WHERE nombre_de_revista = ? AND "
                    + "nombre_de_usuario_creador = ? AND nombre_de_suscriptor = ?;");//query que busca 1 tupla que cumpla con la llave principal de la tanla suscriocion
            query.setString(1, suscripcion.getNombreRevista());//damos valores a los ?
            query.setString(2, suscripcion.getUsuarioCreador());//
            query.setString(3, suscripcion.getNombreUsuario());//
            ResultSet resultado = query.executeQuery();//ejecutar la query
            Suscripcion infoSuscripcion = getConstructorObjeto().crearSuscripcion(resultado);
            return infoSuscripcion; //retornamos la suscripcion contenida en el resulset
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void darLikeARevista(Revista revista) {
        if (saberEstadoDeInteraccionesConRevista(revista.getNombreRevista(), revista.getUsuarioCreador()).isEstadoLikes()) {
            try {
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO `like` VALUES (?,?,CURDATE());");//inserta una nueva tupla a la tabla like
                query.setString(1, revista.getNombreRevista());//damos valores a los ?
                query.setString(2, revista.getUsuarioCreador());//
                if (query.executeUpdate() > 0) {//si al ejecutar la query se modifico mas de 0 tuplas entoncws hacemos commit
                    CONEXION.commit();
                }
            } catch (SQLException e) {
                try {
                    CONEXION.rollback();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public String hacerComentario(Comentario comentario) {
        if (saberEstadoDeInteraccionesConRevista(comentario.getNombreRevista(), comentario.getUsuarioCreador()).isEstadoComentarios()) {
            try {
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO comentario VALUES(?,?,NOW(),?)");//inserta una nueva tupla en la tabla comentario
                query.setString(1, comentario.getNombreRevista());//damos valores a los ?
                query.setString(2, comentario.getUsuarioCreador());//
                query.setString(3, comentario.getContenidoComentario());//
                if (query.executeUpdate() > 0) {//si al insertar se modifica mas de una tabla entonces podemos hacer el commit
                    CONEXION.commit();
                    return "Se inserto tu comentario con exito.";
                }
                //si no entonces retornamos error
                return "No se inserto tu comentario.";
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "No se inserto tu comentario debido a un error del servidor.";
            }
        }
        return "No se inserto tu comentario. La revista no acepta comentarios actualmente.";
    }
}
