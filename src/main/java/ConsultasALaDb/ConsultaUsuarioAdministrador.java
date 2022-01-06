/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import modelos.*;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaUsuarioAdministrador extends ConsultaUsuario {

    /**
     * Constructor que inicializa a la clase pare Consulta
     *
     * @param constructorObjeto
     */
    public ConsultaUsuarioAdministrador(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public String guardarCostoPorDia(CostoPorDia costo) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "INSERT INTO costo_por_dia VALUES(?,?,?,?);");//inserta una nueva tupla a costo por dia
            query.setString(1, costo.getNombreRevista());//damos valores a los ?
            query.setString(2, costo.getUsuarioCreador());//
            query.setDouble(3, costo.getCostoPorDia());//
            query.setString(4, costo.getFechaDeValidez());//
            if (query.executeUpdate() > 0) {
                CONEXION.commit();
                return "Se guardo el costo por dia con exito.";
            }
            return "No se guardo el costo por dia";
        } catch (SQLException e) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex) {

            }
            return "No se guardo el costo por dia debido a un error en el servidor.";
        }
    }

    public String guardarAnunciante(Anunciante anunciante) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "INSERT INTO anunciante VALUES (?);");//inserta una nueva tupla en la tabla anunciante
            query.setString(1, anunciante.getNombreAnunciante());//dmos valore al ? con el niombre del anunciante
            if (query.executeUpdate() > 0) {//si al ejecutar se afecta mas de una fila entones hacemos el commit
                CONEXION.commit();
                return "Se guardo con exito el aununciante " + anunciante.getNombreAnunciante() + ".";
            }
            //si no entro al if entonces no se guardo nada 
            return "No se guardo el anunciante";
        } catch (Exception e) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex) {
            }
            return "No se guardo el anunciante debido a un error en el servidor. Puede deberse a que ya existe un anunciante con el mismo nombre";
        }
    }

    public ArrayList<Anunciante> traerAnunciantes() {
        ArrayList<Anunciante> anunciantes = new ArrayList<>();
        try {
            Statement query = CONEXION.createStatement();//creamos la query con la conexxion
            ResultSet resultado = query.executeQuery("SELECT * FROM anunciante;");
            anunciantes = getConstructorObjeto().construirArrayDeAnunciantes(resultado);//mandamos a construir el array de anunciantes
            return anunciantes;
        } catch (SQLException e) {
            return anunciantes;
        }
    }

    public String gurdarAnuncioDeTexto(AnuncioTexto anuncio) {
        try {
            AnuncioTexto anuncioReal = new AnuncioTexto(anuncio.getTextoAnuncio(), anuncio.getNombreAnuncio(), anuncio.getNombreAnunciante());
            PreparedStatement queryInsercionAnuncio = CONEXION.prepareStatement(
                    "INSERT INTO anuncio VALUES (?,?,?,?,?,?);");//inserta una nueva tupla dentro de la tabla anuncio
            queryInsercionAnuncio.setString(1, anuncioReal.getNombreAnunciante());//damos valores a los ?
            queryInsercionAnuncio.setString(2, anuncioReal.getNombreAnuncio());//
            queryInsercionAnuncio.setString(3, anuncioReal.getTipoAnuncio());//
            queryInsercionAnuncio.setDouble(4, anuncioReal.getPago());//
            queryInsercionAnuncio.setString(5, anuncioReal.getEstado());//
            queryInsercionAnuncio.setString(6, LocalDate.now().toString());//obtenemos la fecha de hoy
            if (queryInsercionAnuncio.executeUpdate() > 0) {
                PreparedStatement queryInsercionAnuncioTexto = CONEXION.prepareStatement(
                        "INSERT INTO anuncio_texto VALUES (?,?,?)");
                queryInsercionAnuncioTexto.setString(1, anuncioReal.getNombreAnunciante());//damos valores a los ?
                queryInsercionAnuncioTexto.setString(2, anuncioReal.getNombreAnuncio());//
                queryInsercionAnuncioTexto.setString(3, anuncioReal.getTextoAnuncio());//
                if (queryInsercionAnuncioTexto.executeUpdate() > 0) {//si al ejecutarse se afecta mas de una tupla entonces se inserto el anuncio
                    CONEXION.commit();//hacer el commit
                    return "Se inserto el anuncio con exito.";
                }
            }
            return "No se registro el anuncio.";
        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return "No se registro el anuncio debido a un error con el servidor.";
        }
    }

    public String guardarAnuncioVideo(AnuncioVideo anuncio) {
        try {
            AnuncioVideo anuncioReal = new AnuncioVideo(anuncio.getLink(), anuncio.getTextoAnuncio(), anuncio.getNombreAnuncio(), anuncio.getNombreAnunciante());
            PreparedStatement queryInsercionAnuncio = CONEXION.prepareStatement(
                    "INSERT INTO anuncio VALUES (?,?,?,?,?,?);");//inserta una nueva tupla dentro de la tabla anuncio
            queryInsercionAnuncio.setString(1, anuncioReal.getNombreAnunciante());//damos valores a los ?
            queryInsercionAnuncio.setString(2, anuncioReal.getNombreAnuncio());//
            queryInsercionAnuncio.setString(3, anuncioReal.getTipoAnuncio());//
            queryInsercionAnuncio.setDouble(4, anuncioReal.getPago());//
            queryInsercionAnuncio.setString(5, anuncioReal.getEstado());//
            queryInsercionAnuncio.setString(6, LocalDate.now().toString());//obtenemos la fecha de hoy
            if (queryInsercionAnuncio.executeUpdate() > 0) {
                PreparedStatement queryInsercionAnuncioTexto = CONEXION.prepareStatement(
                        "INSERT INTO anuncio_video_texto VALUES (?,?,?,?)");
                queryInsercionAnuncioTexto.setString(1, anuncioReal.getNombreAnunciante());//damos valores a los ?
                queryInsercionAnuncioTexto.setString(2, anuncioReal.getNombreAnuncio());//
                queryInsercionAnuncioTexto.setString(3, anuncioReal.getLink());//
                queryInsercionAnuncioTexto.setString(4, anuncioReal.getTextoAnuncio());//
                if (queryInsercionAnuncioTexto.executeUpdate() > 0) {//si al ejecutarse se afecta mas de una tupla entonces se inserto el anuncio
                    CONEXION.commit();//hacer el commit
                    return "Se inserto el anuncio con exito.";
                }
            }
            return "No se registro el anuncio.";
        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return "No se registro el anuncio debido a un error con el servidor.";
        }
    }

    public String guardarAnuncioImagen(AnuncioImagen anuncio) {
        try {
            PreparedStatement queryInsercionAnuncio = CONEXION.prepareStatement(
                    "INSERT INTO anuncio VALUES (?,?,?,?,?,?);");//inserta una nueva tupla dentro de la tabla anuncio
            queryInsercionAnuncio.setString(1, anuncio.getNombreAnunciante());//damos valores a los ?
            queryInsercionAnuncio.setString(2, anuncio.getNombreAnuncio());//
            queryInsercionAnuncio.setString(3, anuncio.getTipoAnuncio());//
            queryInsercionAnuncio.setDouble(4, anuncio.getPago());//
            queryInsercionAnuncio.setString(5, anuncio.getEstado());//
            queryInsercionAnuncio.setString(6, LocalDate.now().toString());//obtenemos la fecha de hoy
            if (queryInsercionAnuncio.executeUpdate() > 0) {
                PreparedStatement queryInsercionAnuncioTexto = CONEXION.prepareStatement(
                        "INSERT INTO anuncio_imagen_texto VALUES (?,?,?,?)");
                queryInsercionAnuncioTexto.setString(1, anuncio.getNombreAnunciante());//damos valores a los ?
                queryInsercionAnuncioTexto.setString(2, anuncio.getNombreAnuncio());//
                queryInsercionAnuncioTexto.setBlob(3, anuncio.getImagen());//
                queryInsercionAnuncioTexto.setString(4, anuncio.getTexto());//
                if (queryInsercionAnuncioTexto.executeUpdate() > 0) {//si al ejecutarse se afecta mas de una tupla entonces se inserto el anuncio
                    CONEXION.commit();//hacer el commit
                    return "Se inserto el anuncio con exito.";
                }
            }
            return "No se registro el anuncio.";
        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return "No se registro el anuncio debido a un error con el servidor.";
        }
    }

    public String guardarTagsAnuncio(ArrayList<TagAnuncio> tags) {
        try {
            for (TagAnuncio item : tags) {
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO tag_anuncio VALUES (?,?,?)");
                query.setString(1, item.getNombreTag());//damos los valoes con los atributos de la clase TagAnuncio
                query.setString(2, item.getNombreAunciante());//
                query.setString(3, item.getNombreAnuncio());//
                if (query.executeUpdate() > 0) {//si al ejecutar la query no se inserta nada formazmos un roklback
                    CONEXION.commit();
                } else {
                    throw new SQLException();
                }
            }
            //si acaba el for each entonces mandamos un mensaje de confirmacion exitoso
            return "Se guardaron todos tus tags con exito.";
        } catch (SQLException e) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex) {

            }
            return "No se guardaron los tags";
        }
    }

    public ArrayList<Anuncio> retornarTodosLosAnuncios() {
        ArrayList<Anuncio> anuncios = new ArrayList<>();
        try {
            Statement query = CONEXION.createStatement();//creamos la query que se ejecutara
            ResultSet resultado = query.executeQuery(
                    "SELECT * FROM anuncio;");//SELECCIONAMOS TODAS LAS TUPLAS DE LA TABLA ANUNCIO
            anuncios = getConstructorObjeto().construirAnuncios(resultado);
            return anuncios;
        } catch (SQLException e) {
            return anuncios;
        }
    }

    public String cambiarEstadoDeAnuncio(Anuncio anuncio) {
        try {
            //primero vemos que estado tiene el anuncio en este momento
            PreparedStatement query = CONEXION.prepareStatement("SELECT estado FROM anuncio "
                    + "WHERE nombre_anunciante = ? AND nombre_anuncio = ?;");//traemos el estado de una revista
            query.setString(1, anuncio.getNombreAnunciante());//dar valores a los ?
            query.setString(2, anuncio.getNombreAnuncio());//
            ResultSet resultado = query.executeQuery();//ejecutar la query
            String estadoActual = "Activo";
            while (resultado.next()) {//exploramos la primer tupla del resultset
                estadoActual = resultado.getString("estado");
                break;
            }
            String nuevoEstado = "Activo";
            //decidimos el nuevo estado segun el estado actual del anuncio
            switch (estadoActual) {
                case "Activo":
                    nuevoEstado = "Inactivo";
                    break;
                case "Inactivo":
                    nuevoEstado = "Activo";
                    break;
            }
            //luego de obtener el estado nuevo entonces editamos ese registro
            query = CONEXION.prepareStatement("UPDATE anuncio SET estado = ? WHERE "
                    + "nombre_anunciante = ? AND nombre_anuncio = ?;");//actualiza el estado
            query.setString(1, nuevoEstado);
            query.setString(2, anuncio.getNombreAnunciante());
            query.setString(3, anuncio.getNombreAnuncio());
            if (query.executeUpdate() > 0) {
                CONEXION.commit();
                return nuevoEstado;
            }
            return estadoActual;
        } catch (SQLException e) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex) {
            }
            return "Error";
        }
    }

    public AnuncioVideo mostrarAnuncioVideo(String usuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT c.nombre_anunciante, c.nombre_anuncio, c.texto, c.link_video "
                    + "FROM tag_usuario a INNER JOIN tag_anuncio b INNER JOIN anuncio_video_texto c "
                    + "INNER JOIN anuncio d ON a.nombre_tag = b.nombre_tag AND b.nombre_anuncio = c.nombre_anuncio "
                    + "AND c.nombre_anuncio = d.nombre_anuncio AND c.nombre_anunciante = d.nombre_anunciante "
                    + "AND d.estado = ? AND a.nombre_de_usuario = ? ORDER BY RAND() LIMIT 1;");//recomienda un anuncio segun las tags del usuario que se mande
            query.setString(2, usuario);
            query.setString(1, "Activo");
            ResultSet resultado = query.executeQuery();//ejecutar la query
            AnuncioVideo anuncio = null;
            while (resultado.next()) {
                String nombreAnunciante = resultado.getString("nombre_anunciante");//traemos los datos del resultset
                String nombreAnuncio = resultado.getString("nombre_anuncio");//
                String textoAnuncio = resultado.getString("texto");//
                String link = resultado.getString("link_video");//
                anuncio = new AnuncioVideo(link, textoAnuncio, nombreAnuncio, nombreAnunciante);
            }
            return anuncio;
        } catch (SQLException e) {
            return null;
        }
    }

    public AnuncioImagen mostrarAnuncioImagen(String usuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT c.nombre_anunciante, c.nombre_anuncio, c.texto "
                    + "FROM tag_usuario a INNER JOIN tag_anuncio b INNER JOIN anuncio_imagen_texto c "
                    + "INNER JOIN anuncio d ON a.nombre_tag = b.nombre_tag AND b.nombre_anuncio = c.nombre_anuncio "
                    + "AND c.nombre_anuncio = d.nombre_anuncio AND c.nombre_anunciante = d.nombre_anunciante "
                    + "AND d.estado = ? AND a.nombre_de_usuario = ? ORDER BY RAND() LIMIT 1;");//recomienda un anuncio segun las tags del usuario que se mande
            query.setString(2, usuario);
            query.setString(1, "Activo");
            ResultSet resultado = query.executeQuery();//ejecutar la query
            AnuncioImagen anuncio = null;
            while (resultado.next()) {
                String nombreAnunciante = resultado.getString("nombre_anunciante");//traemos los datos del resultset
                String nombreAnuncio = resultado.getString("nombre_anuncio");//
                String textoAnuncio = resultado.getString("texto");//
                anuncio = new AnuncioImagen(textoAnuncio, null, nombreAnuncio, nombreAnunciante);
            }
            return anuncio;
        } catch (SQLException e) {
            return null;
        }
    }

    public AnuncioTexto mostrarAnuncioTexto(String usuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT c.nombre_anunciante, c.nombre_anuncio, c.texto "
                    + "FROM tag_usuario a INNER JOIN tag_anuncio b INNER JOIN anuncio_texto c "
                    + "INNER JOIN anuncio d ON a.nombre_tag = b.nombre_tag AND b.nombre_anuncio = c.nombre_anuncio "
                    + "AND c.nombre_anuncio = d.nombre_anuncio AND c.nombre_anunciante = d.nombre_anunciante "
                    + "AND d.estado = ? AND a.nombre_de_usuario = ? ORDER BY RAND() LIMIT 1;");//recomienda un anuncio segun las tags del usuario que se mande
            query.setString(2, usuario);
            query.setString(1, "Activo");
            ResultSet resultado = query.executeQuery();//ejecutar la query
            AnuncioTexto anuncio = null;
            while (resultado.next()) {
                String nombreAnunciante = resultado.getString("nombre_anunciante");//traemos los datos del resultset
                String nombreAnuncio = resultado.getString("nombre_anuncio");//
                String textoAnuncio = resultado.getString("texto");//
                anuncio = new AnuncioTexto(textoAnuncio, nombreAnuncio, nombreAnunciante);
            }
            return anuncio;
        } catch (SQLException e) {
            return null;
        }
    }

    public void guardarHistorialDeAnuncio(HistorialAnuncio historial) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "INSERT INTO historial_anuncio (nombre_anunciante, nombre_anuncio, link_donde_aparecio"
                    + ", fecha_de_aparicion) VALUES (?,?,?,NOW())");//inserta una nueva fila
            query.setString(1, historial.getNombreAnunciante());//damos vlaores a los ? con los traiburos del objeto historial
            query.setString(2, historial.getNombreAnuncio());//
            query.setString(3, historial.getUrl());//
            if (query.executeUpdate() > 0) {
                CONEXION.commit();
            }
        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException e) {
            }
        }
    }

    public InputStream traerImagenDeAnuncio(String nombreAnuncio, String nombreAunciante) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT imagen FROM anuncio_imagen_texto WHERE nombre_anunciante = ?"
                    + " AND nombre_anuncio = ?;");//inserta una nueva fila
            query.setString(1, nombreAunciante);//damos valors a lo s? con los parametros
            query.setString(2, nombreAnuncio);
            ResultSet resultado = query.executeQuery();
            resultado.next();
            return resultado.getBlob("imagen").getBinaryStream();
        } catch (SQLException ex) {
            return null;
        }
    }
    public String guardarCategoria(Categoria categoria) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "INSERT INTO categoria VALUES(?)");//inserta una nueva fila
            query.setString(1, categoria.getNombreCategoria());//damos valors a lo s? con los parametros
            if(query.executeUpdate() > 0){
                CONEXION.commit();
                return "Se guardo la categoria con exito.";
            }
            return "No se inserto la categoria";
        } catch (SQLException ex) {
            return "No se inserto la categoria por un error en el servidor";
        }
    }
      public String guardarTag(Tag tag) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "INSERT INTO tag VALUES(?)");//inserta una nueva fila
            query.setString(1, tag.getNombreTag());//damos valors a lo s? con los parametros
            if(query.executeUpdate() > 0){
                CONEXION.commit();
                return "Se guardo el tag con exito.";
            }
            return "No se inserto el tag";
        } catch (SQLException ex) {
            return "No se inserto el tag por un error en el servidor";
        }
    }
}
