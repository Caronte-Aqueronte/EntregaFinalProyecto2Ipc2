package herramientas;

import modelos.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import modelosReportes.*;
import modelosReportes.LikeDeRevista;

public class ConstructorDeObjeto {

    /**
     * Este metodo crea un objeto Perfil apartir de explorar el ResultSet que
     * recibe como parametro
     *
     * @param resultSet
     * @return
     */
    public Perfil construirPerfil(ResultSet resultSet) {
        try {
            String descripcion = "";
            String hobbies = "";
            while (resultSet.next()) {
                descripcion = resultSet.getString("descripcion");
                hobbies = resultSet.getString("hobbies");
            }
            return new Perfil(descripcion, hobbies);
        } catch (SQLException ex) {
            return new Perfil("", "");
        }
    }

    /**
     * Este metodo crea un objeto Usuario apartir de explorar el ResultSet que
     * recibe como parametro
     *
     * @param resultSet
     * @return
     */
    public Usuario crearUsuario(ResultSet resultSet) {
        try {
            Usuario usuario;
            String nombre = "";
            String password = "";
            String rol = "";
            while (resultSet.next()) {
                nombre = resultSet.getString("nombre_de_usuario");
                password = resultSet.getString("password");
                rol = resultSet.getString("rol");
            }
            usuario = new Usuario(nombre, password, rol);
            return usuario;
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Este metodo crea un array de objetos Tag apartir de la exploracion del
     * ResultSet qeu recibe como parametro
     *
     * @param resultSet
     * @return
     */
    public ArrayList<Tag> crearArrayDeTags(ResultSet resultSet) {
        ArrayList<Tag> arrayDeTags = new ArrayList<>();
        try {
            String nombreTag;
            while (resultSet.next()) {//exploramos el resultset
                nombreTag = resultSet.getString("nombre_tag");//traemos el nombre del tag
                arrayDeTags.add(new Tag(nombreTag));//anadimos el nuevo tag
            }
            return arrayDeTags;//retornamos el array sin importar que este vacio
        } catch (SQLException ex) {
            return arrayDeTags;//retornamos el array sin importar que este vacio
        }
    }

    /**
     * Este metodo construye un array list de varios objetos Categoria
     * construidos a partir del ResultSet que recibe como parametroF
     *
     * @param resultSet
     * @return
     */
    public ArrayList<Categoria> crearArrayDeCategorias(ResultSet resultSet) {
        ArrayList<Categoria> categorias = new ArrayList<>();//crear un array nuevo de categorias si encaso hay error se devolvera vacia
        try {
            String nombreCategoria;//nombre de la categoria que se registrara en el array
            while (resultSet.next()) {
                nombreCategoria = resultSet.getString("nombre_de_categoria");//traermos el nombre la categoria
                categorias.add(new Categoria(nombreCategoria));//anadimos la nueva categoria
            }//acabado el while devolvemos el array
            return categorias;
        } catch (SQLException ex) {
            return categorias;//si hay error entonces devolvemos el array independiente de su estado
        }
    }

    /**
     * Construye una revista a partir de un request
     *
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public Revista crearRevistaDeUnRequest(HttpServletRequest request) throws IOException, ServletException {
        //construimos un objeto revista a partir del formData que vendra en la request
        String nombreRevista = request.getParameter("nombreRevista");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        Part parteArchivo = request.getPart("datafilePdf");//obtener las partes del archivo
        InputStream contenido = parteArchivo.getInputStream();//obtener el inputstream de las partes de; archivo
        Part parteArchivoMiniatura = request.getPart("datafileMiniatura");//obtener las partes del archivo de la miniatura
        InputStream miniatura = parteArchivoMiniatura.getInputStream();//obtener la miniatura a partir de las partres del mismo
        double costoDeSuscripcion = Double.valueOf(request.getParameter("costoDeSuscripcion"));
        String estadoSuscripcion = request.getParameter("estadoSuscripcion");
        String estadoComentarios = request.getParameter("estadoComentarios");
        String estadoLikes = request.getParameter("estadoLikes");
        String fechaDePublicacion = request.getParameter("fechaDePublicacion");
        String usuarioCreador = request.getParameter("usuarioCreador");
        Revista revista = new Revista(nombreRevista, descripcion, categoria, contenido, miniatura, costoDeSuscripcion, //creamos el objeto
                estadoSuscripcion, estadoComentarios, estadoLikes, fechaDePublicacion, usuarioCreador);//
        return revista;
    }

    public InteraccionConRevista constrirInteracciones(ResultSet resultado) {
        InteraccionConRevista intereacciones = new InteraccionConRevista(false, false, false);
        try {
            while (resultado.next()) {//exploramos el resultset y traemos los estados que se encuentran contenidos en el
                String estadoLikes = resultado.getString("estado_de_likes");
                String estadoSuscripciones = resultado.getString("estado_de_suscripciones");
                String estadoComentarios = resultado.getString("estado_de_comentarios");
                //depende del valor contenido de los estados asi seteamos los valores de las banderas
                if (estadoLikes.equals("Activo")) {
                    intereacciones.setEstadoLikes(true);
                } else {
                    intereacciones.setEstadoLikes(false);
                }

                if (estadoSuscripciones.equals("Activo")) {
                    intereacciones.setEstadoSuscripciones(true);
                } else {
                    intereacciones.setEstadoSuscripciones(false);
                }

                if (estadoComentarios.equals("Activo")) {
                    intereacciones.setEstadoComentarios(true);
                } else {
                    intereacciones.setEstadoComentarios(false);
                }
            }
            return intereacciones;
        } catch (Exception e) {
            return intereacciones;
        }
    }

    public ArrayList<Comentario> construirArrayDeComentarios(ResultSet resultado) {
        ArrayList<Comentario> comentarios = new ArrayList<>();//iniciar un nuevo array
        try {
            while (resultado.next()) {//exploramos todo el resultset en busca de la informacion que tiene contenida
                //a partir de las columnas conrenidoas en el resultSet traer todos los valores de las tuplas
                String nombreRevista = resultado.getString("nombre_de_revista");
                String usuarioCreador = resultado.getString("revista_nombre_de_usuario_creador");
                LocalDate fechaCreacion = resultado.getDate("fecha_de_comentario").toLocalDate();
                String contenidoComentario = resultado.getString("contenido_de_comentario");
                //a patir de los valores crear un nuevo comentario
                Comentario comentario = new Comentario(contenidoComentario, fechaCreacion, nombreRevista, usuarioCreador);
                //anadir el nuevo comentario al array
                comentarios.add(comentario);
            }
            return comentarios;
        } catch (SQLException ex) {
            return comentarios;//si hay error devolver el array vacio
        }
    }

    /**
     * Este metodo crea una suscripcion a partir se los datos contenidos en uns
     * ResultSet
     *
     * @param resultado
     * @return
     */
    public Suscripcion crearSuscripcion(ResultSet resultado) {
        try {
            while (resultado.next()) {//exploramos el resultado y construimos una suscripcion a partir de los meta datos
                String nombreRevista = resultado.getString("nombre_de_revista");
                String nombreUsuarioCreador = resultado.getString("nombre_de_usuario_creador");
                String nombreSuscriptor = resultado.getString("nombre_de_suscriptor");
                String tipoSuscripcion = resultado.getString("tipo_de_suscripcion");
                String fechaSuscripcion = resultado.getString("fecha_de_suscripcion");
                //creamos la suscripcion a partir de los datos recopilados
                Suscripcion suscripcion = new Suscripcion(nombreSuscriptor, tipoSuscripcion,
                        fechaSuscripcion, nombreRevista, nombreUsuarioCreador);
                return suscripcion;
            }
            //si llega aqui entonces no entro al while y retornmaos null
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public Pago crearPago(ResultSet resultado) {
        try {
            while (resultado.next()) {//exploramos el resultado y construimos el pago a partir de los meta datos
                String nombreRevista = resultado.getString("nombre_de_revista");
                String nombreUsuarioCreador = resultado.getString("nombre_de_usuario_creador");
                String nombreSuscriptor = resultado.getString("nombre_de_suscriptor");
                String fechaPago = resultado.getString("fecha_de_pago");
                //creamos el pago a partir de los datos recopilados
                Pago pago = new Pago(fechaPago, nombreSuscriptor, nombreRevista, nombreUsuarioCreador);
                return pago;
            }
            //si llega aqui entonces no entro al while y retornmaos null
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayList<Anunciante> construirArrayDeAnunciantes(ResultSet resultSet) {
        ArrayList<Anunciante> anunciantes = new ArrayList<>();//array vacio en caso de error devolver
        try {
            while (resultSet.next()) {
                Anunciante anunciante = new Anunciante(resultSet.getString("nombre_anunciante"));
                anunciantes.add(anunciante);//agregamos el anunciante al array
            }
            return anunciantes;
        } catch (SQLException e) {
            return anunciantes;
        }
    }

    public AnuncioImagen construirAnuncioImagenDeRequest(HttpServletRequest request) throws IOException, ServletException {
        //construimos un objeto revista a partir del formData que vendra en la request
        String texto = request.getParameter("texto");
        String nombreAnuncio = request.getParameter("nombreAnuncio");
        String nombreAnunciante = request.getParameter("nombreAnunciante");
        InputStream imagen = request.getPart("imagen").getInputStream();
        return new AnuncioImagen(texto, imagen, nombreAnuncio, nombreAnunciante);
    }

    public ArrayList<Anuncio> construirAnuncios(ResultSet resultado) {
        ArrayList<Anuncio> anuncios = new ArrayList<>();
        try {
            //explorar el resulado, conseguir ls parametros de un Anuncio y construir uno a partir de ello
            while (resultado.next()) {
                String nombreAnunciante = resultado.getString("nombre_anunciante");
                String nombreAnuncio = resultado.getString("nombre_anuncio");
                String tipoAnuncio = resultado.getString("tipo_anuncio");
                double pago = resultado.getDouble("pago");
                String estado = resultado.getString("estado");
                String fechaDeCreacion = resultado.getString("fecha_creacion");
                //crear el anuncio y agregarlos al array
                Anuncio anuncio = new Anuncio(nombreAnuncio, tipoAnuncio, pago, estado, fechaDeCreacion, nombreAnunciante);
                anuncios.add(anuncio);
            }
            return anuncios;
        } catch (SQLException e) {
            return anuncios;
        }
    }

    public ArrayList<Suscripcion> construirSuscripciones(ResultSet resultado) {
        ArrayList<Suscripcion> suscripciones = new ArrayList<>();//array vacio en caso de eerror devolver ese
        try {
            //exploramos el ResultSet moviendo el cursor con .next() y obtenemos las columnas del mismo
            while (resultado.next()) {
                //obtener los parametros para crear una nueva suscripcion
                String nombreRevista = resultado.getString("nombre_de_revista");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                String nombreUsuario = resultado.getString("nombre_de_suscriptor");
                String metodoPago = resultado.getString("tipo_de_suscripcion");
                String fechaSuscripcion = resultado.getString("fecha_de_suscripcion");
                Suscripcion suscripcion = new Suscripcion(nombreUsuario, metodoPago, fechaSuscripcion, nombreRevista, usuarioCreador);
                suscripciones.add(suscripcion);//agregamos la nueva suscripcion
            }
            return suscripciones;
        } catch (SQLException e) {
            return suscripciones;
        }
    }

    public ArrayList<LikeDeRevista> likesDeRevista(ResultSet resultado) {
        ArrayList<LikeDeRevista> likes = new ArrayList<>();
        try {
            //exploreamos el resultset moviendo el cursor
            while (resultado.next()) {
                //obtenemos los valores de cada tupla para crear el objeto like
                String nombreRevista = resultado.getString("nombre_de_revista");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                LocalDate fechaLike = resultado.getDate("fecha_de_like").toLocalDate();
                int numeroLikes = resultado.getInt("numero_de_likes");
                LikeDeRevista like = new LikeDeRevista(fechaLike, numeroLikes, nombreRevista, usuarioCreador);
                likes.add(like);//anadimos el like al array
            }
            return likes;
        } catch (SQLException e) {
            return likes;
        }
    }

    public ArrayList<LikeDeRevista> likesDeUnaRevista(ResultSet resultado) {
        ArrayList<LikeDeRevista> likes = new ArrayList<>();
        try {
            //exploreamos el resultset moviendo el cursor
            while (resultado.next()) {
                //obtenemos los valores de cada tupla para crear el objeto like
                String nombreRevista = resultado.getString("nombre_de_revista");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                LocalDate fechaLike = resultado.getDate("fecha_de_like").toLocalDate();
                LikeDeRevista like = new LikeDeRevista(fechaLike, nombreRevista, usuarioCreador);
                likes.add(like);//anadimos el like al array
            }
            return likes;
        } catch (SQLException e) {
            return likes;
        }
    }

    public ArrayList<GananciaPorSuscripcion> construirGanancias(ResultSet resultado) {
        ArrayList<GananciaPorSuscripcion> ganancias = new ArrayList<>();
        try {
            //exploramos cada tipla del resultSet para obtener sus columnas
            while (resultado.next()) {
                String fechaPago = resultado.getString("fecha_de_pago");//obtenemos cada cpolumna del resultset para poder crear un objeto
                String nombreUsuario = resultado.getString("nombre_de_suscriptor");//
                String nombreRevista = resultado.getString("nombre_de_revista");//
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");//
                double totalDeGanancia = resultado.getDouble("ganancia");//
                GananciaPorSuscripcion gananciaSuscripcion = new GananciaPorSuscripcion(totalDeGanancia, fechaPago,
                        nombreUsuario, nombreRevista, usuarioCreador);//creamos el nuevo objeto que representa la ganancia por suscripcion
                ganancias.add(gananciaSuscripcion);
            }
            return ganancias;
        } catch (SQLException e) {
            return ganancias;
        }
    }

    public ArrayList<GananciaPorSuscripcionAdminsrativa> construirGananciasPorSuscripcionAdministrativa(ResultSet resultSet) {
        ArrayList<GananciaPorSuscripcionAdminsrativa> ganancias = new ArrayList<>();
        try {
            //exploreamos el resultSet para obtener sus columnas
            while (resultSet.next()) {
                String fechaPago = resultSet.getString("fecha_de_pago");//obtenemos cada cpolumna del resultset para poder crear un objeto
                String nombreUsuario = resultSet.getString("nombre_de_suscriptor");//
                String nombreRevista = resultSet.getString("nombre_de_revista");//
                String usuarioCreador = resultSet.getString("nombre_de_usuario_creador");//
                double totalDeGanancia = resultSet.getDouble("ganancia");//
                double ingreso = resultSet.getDouble("ingreso");//
                GananciaPorSuscripcionAdminsrativa ganancia = new GananciaPorSuscripcionAdminsrativa(ingreso, totalDeGanancia,
                        fechaPago, nombreUsuario, nombreRevista, usuarioCreador);
                ganancias.add(ganancia);
            }
            return ganancias;
        } catch (SQLException e) {
            return ganancias;
        }
    }
}
