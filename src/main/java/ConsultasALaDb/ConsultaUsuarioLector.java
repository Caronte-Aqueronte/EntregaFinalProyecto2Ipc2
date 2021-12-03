/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import ModelosApi.Revista;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaUsuarioLector extends Consulta{
    /**
     * Constructor de la clase ConsultaUsuarioLector que inicializa la clase padre
     * @param constructorObjeto 
     */
    public ConsultaUsuarioLector(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }
    
    public ArrayList<Revista> recomendarRevistas(String nombreUsuario){
        ArrayList<Revista> revistas = new ArrayList<>();
        try{
            PreparedStatement query = CONEXION.prepareStatement(
            "SELECT a.nombre_de_revista, a.descripcion, a.categoria FROM revista a"
             + " INNER JOIN tag_revista b INNER JOIN tag_usuario c"
             + " ON a.nombre_de_revista = b.nombre_de_revista AND b.nombre_tag = c.nombre_tag "
             + "AND c.nombre_de_usuario = ? ORDER BY RAND() LIMIT 15;");
            query.setString(1, nombreUsuario);
            ResultSet resultado = query.executeQuery();
            while(resultado.next()){
                String nombreRevista = resultado.getString("nombre_de_revista");
                String descripcion = resultado.getString("descripcion");
                String categoria = resultado.getString("categoria");
                revistas.add(new Revista(nombreRevista, descripcion, categoria));
            }
            return revistas;
        }catch(SQLException ex){
            return revistas;
        }
    }
    
}
