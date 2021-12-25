/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
import modelos.CostoPorDia;
import java.sql.*;
import java.util.ArrayList;
import modelos.Anunciante;

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
            e.printStackTrace();
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
}
