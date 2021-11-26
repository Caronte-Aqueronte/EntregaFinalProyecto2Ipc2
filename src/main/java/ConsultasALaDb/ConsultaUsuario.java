package ConsultasALaDb;

import ModelosApi.Perfil;
import ModelosApi.Usuario;

import java.io.*;
import java.sql.*;


public class ConsultaUsuario extends Consulta {

    public ConsultaUsuario(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public String crearUsuario(Usuario usuario) {
        try {

            InputStream stream = ConsultaUsuario.class.getResourceAsStream("/imagenes/usuario.png");
            if (!saberSiExisteUsuario(usuario.getUsuario())) {//si el usuario no existe entonces podemos ingresar el nuevo
                PreparedStatement query = CONEXION.prepareStatement(//query con la sintaxis para insertar un nuevo usuario en usuarios
                        "INSERT INTO usuario VALUES (?,?,?)");
                query.setString(1, usuario.getUsuario());//damos valores a los ?
                query.setString(2, usuario.getPassword());//
                query.setString(3, usuario.getRol());//
                query.executeUpdate();//ejecutamos la query de insertar un usuario
                query = CONEXION.prepareStatement(//queda crear el perfil
                        "INSERT INTO perfil VALUES (?,?,?,?);");//query que crea el perifl del usuario
                query.setString(1, usuario.getUsuario());//dar valores a los ?
                query.setBlob(2, stream);
                query.setString(3, "");
                query.setString(4, "");
                query.executeUpdate();//ejecutamos la query de insertar un perfil
                CONEXION.commit();//hacemos el commit
                return "Se registro el nuevo usuario";//si se ingreso retornamos el mensaje
            }
            return "No se creo el usuario, ya existe un usuario con el mismo nombre";
        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException ex2) {
            }
            return "No se creo el usuario";
        }
    }

    public String guardarFoto(InputStream foto, String nombreUsuario) {
        try {
            PreparedStatement query = CONEXION.prepareStatement(
                    "UPDATE perfil SET foto = ? WHERE nombre_de_usuario = ?;");//statement que guardara el blob de la foto de perfil
            query.setBlob(1, foto);//mandamos la foto como un blob
            query.setString(2, nombreUsuario);
            query.executeUpdate();//ejecutamos la query
            CONEXION.commit();//se hace el commit
            return "Se actualizo foto de perfil con exito";//si no hay error entonces retornamos mensaje

        } catch (SQLException ex) {
            try {//si hay error deshacemos todos los cambios
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return "No guardaron los cambios de tu foto de perfil";
        }
    }

    public InputStream traerFoto(String nombreUsuario) {
        try {

            PreparedStatement query = CONEXION.prepareStatement(
                    "SELECT foto FROM perfil WHERE nombre_de_usuario = ?;");//statement que guardara el blob de la foto de perfil
            query.setString(1, nombreUsuario);//dar valor al ?
            ResultSet resultado = query.executeQuery();//ejecutamos la query
            while (resultado.next()) {//si entra al while etonces exite la foto de usuario
                return resultado.getBlob("foto").getBinaryStream();//obtener el blod y apartir de el obtener el InputStream
            }
            return null;

        } catch (SQLException ex) {
            try {//si hay error deshacemos todos los cambios
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return null;
        }
    }

    public String guardarPerfil(Perfil perfil) {
        try {
            PreparedStatement query;
            if (saberSiExistePerfil(perfil.getUsuario())) {//si existe el usuario entonces lo editamos
                query = CONEXION.prepareStatement(
                        "UPDATE perfil SET descripcion = ?, hobbies = ? WHERE nombre_de_usuario = ?");
                query.setString(1, perfil.getDescripcion());
                query.setString(2, perfil.getHobbies());
                query.setString(3, perfil.getUsuario());
                if (query.executeUpdate() > 0) {
                    CONEXION.commit();
                    return "Se guardaron los cambios de tu perfil";
                }
                return "No guardaron los cambios de tu perfil";
            } else {//si no existe el perfil etonces lo guardamos
                query = CONEXION.prepareStatement(
                        "INSERT INTO perfil (nombre_de_usuario , descripcion, hobbies) VALUES (?,?,?)");
                query.setString(1, perfil.getUsuario());
                query.setString(2, perfil.getDescripcion());
                query.setString(3, perfil.getHobbies());
                if (query.executeUpdate() > 0) {
                    CONEXION.commit();
                    return "Se guardaron los cambios de tu perfil";
                }
                return "No guardaron los cambios de tu perfil";
            }

        } catch (SQLException ex) {
            try {
                CONEXION.rollback();
            } catch (SQLException e) {
            }
            return "No guardaron los cambios de tu perfil";
        }
    }
}
