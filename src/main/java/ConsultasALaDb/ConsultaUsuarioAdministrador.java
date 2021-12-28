/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
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
            AnuncioTexto anuncioReal = new AnuncioTexto(anuncio.getTexto(), anuncio.getNombreAnuncio(), anuncio.getNombreAnunciante());
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
                queryInsercionAnuncioTexto.setString(3, anuncioReal.getTexto());//
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
    public String guardarTagsAnuncio(ArrayList<TagAnuncio> tags){
        try {
            for(TagAnuncio item: tags){
                PreparedStatement query = CONEXION.prepareStatement(
                        "INSERT INTO tag_anuncio VALUES (?,?,?)");
                query.setString(1, item.getNombreTag());//damos los valoes con los atributos de la clase TagAnuncio
                query.setString(2, item.getNombreAunciante());//
                query.setString(3, item.getNombreAnuncio());//
                if(query.executeUpdate() > 0){//si al ejecutar la query no se inserta nada formazmos un roklback
                    CONEXION.commit();
                }else{
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
}
