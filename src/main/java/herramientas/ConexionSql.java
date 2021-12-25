package herramientas;

import java.sql.*;

public class ConexionSql {

    private static final String USUARIO = "usuarioRevistas";//usuario especial para la bd
    private static final String PASSWORD = "58650//813LaMg";//contra del usuario
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_revistas";//cadena de conexion
    public Connection CONEXION = conectar();//esta sera la conexion que usaremos por toda la app

    private Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");//cargamos el driver
            Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);//estabklecemos conwxion
            conexion.setAutoCommit(false);//el autocommit no nos va a servir, lo desactivamos
            return conexion;//retornamos la coexion
        } catch (SQLException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
