/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import modelos.*;
import herramientas.*;
import ConsultasALaDb.*;
import Convetidores.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import modelos.Comentario;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorAccionRevista", urlPatterns = {"/ControladorAccionRevista"})
public class ControladorAccionRevista extends HttpServlet {

    /**
     * Regula todas las peticiones post que se hacen desde el frontend
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //extraemos el json del reuqest independientemente haya o no haya
        ExtractorDeStringRequest extractor = new ExtractorDeStringRequest(request);
        String jsonRequest = extractor.extraerStringDeRequest();
        switch (request.getHeader("Accion")) {
            case "devolverCategorias":
                controlarDevolucionDeCategorias(response);
                break;
            case "guardarTags":
                controlarGuardarTagsDeRevista(jsonRequest, response);
                break;
            case "recomendarRevistas":
                recomendarRevistas(jsonRequest, response);
                break;
            case "buscarPorCategoria":
                buscarRevistasPorCategoria(jsonRequest, response);
                break;
            case "buscarPorTag":
                buscarRevistasPorTag(jsonRequest, response);
                break;
            case "verSuscripciones":
                verSuscripciones(jsonRequest, response);
                break;
            case "verSuscripcion":
                verSuscripcion(jsonRequest, response);
                break;
            case "infoRevista":
                retornarResumen(jsonRequest, response);
                break;
            case "tagsRevista":
                retornarTags(jsonRequest, response);
                break;
            case "costoRevista":
                retornarCostoRevista(jsonRequest, response);
                break;
            case "suscribirse":
                suscribirseARevista(jsonRequest, response);
                break;
            case "saberEstadoDeInteraccionesConRevista":
                saberEstadoDeInteraccionesConRevista(jsonRequest, response);
                break;
            case "darLike":
                darLikeARevista(jsonRequest, response);
                break;
            case "hacerComentario":
                registrarComentario(jsonRequest, response);
                break;
            case "traerComentarios":
                retornarComentarios(jsonRequest, response);
                break;
            case "realizarPago":
                realizarPago(jsonRequest, response);
                break;
            case "traerTodasLasRevistas":
                traerTodasLasRevistas(response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        switch (accion) {
            case "traerCaratula":
                controladorDevolverCaratulaDelPdf(request, response);
                break;
            case "traerPdf":
                controladorDevolverPdf(request, response);
                break;
        }

    }

    /**
     * Este metodo consigue un ArrayList con todas las Categoria de la base de
     * datos y las convierte en un Json para retornarlas al response
     *
     * @throws IOException
     */
    private void controlarDevolucionDeCategorias(HttpServletResponse response) throws IOException {
        Consulta consulta = new ConsultaUsuario(new ConstructorDeObjeto());//objetos que serviran en este metodo
        Gson gson = new Gson();
        ArrayList<Categoria> categorias = consulta.devolverCategorias();//mandar a traer todas las tags a la bd      
        String jsonResponse = gson.toJson(categorias); //convertir el ArrayList a un Json       
        response.getWriter().append(jsonResponse);//adjuntar el Json al response
    }

    /**
     * Este metodo extrae el json que de la request que contiene el listado de
     * tags que se asignaran a una revista y devuelve la respuesta de las
     * inserciones
     *
     * @throws IOException
     */
    private void controlarGuardarTagsDeRevista(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaUsuarioEditor consulta = new ConsultaUsuarioEditor(new ConstructorDeObjeto());//Creamos una nueva consulta      
        Gson gson = new Gson();//el josn lo pasamos a objeto
        ArrayList<TagRevista> tags = gson.fromJson(jsonRequest, new TypeToken<ArrayList<TagRevista>>() {
        }.getType());
        String respuesta = "";
        for (TagRevista item : tags) {//por cada elemento del array insertamos el tan
            respuesta += "\n" + consulta.guardarTagDeRevista(item);//adjuntamos la respuesta a la respuesta general
        }
        Convertidor convertidor = new ConvertidorString(String.class);//inicializamos el convertidor como un convertidor String      
        String respuestaJson = convertidor.deObjetoAJson(respuesta);//convertimos la respuesta a Json        
        response.getWriter().append(respuestaJson);//adjuntamos la respuesta al response
    }

    /**
     * Este metodo extrae el parametro del reuqest que representa el nombre de
     * la revista de la cual se quiere la portada, la envia el metoso
     * traerCaratulaPdf
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void controladorDevolverCaratulaDelPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializamos las consultas
        //extraemos el nombre de la revista para traer su miniatura
        String nombreRevista = request.getParameter("nombreRevista");
        String usuarioCreador = request.getParameter("usuarioCreador");
        BufferedInputStream fileStream = new BufferedInputStream(consulta.traerCaratulaPdf(nombreRevista, usuarioCreador));//mandamos a traer la foto del usuario
        response.setContentType("image/png");//especificamos que es una ong
        int data;
        while ((data = fileStream.read()) > -1) {//escribimos el archivo en el response
            response.getOutputStream().write(data);
        }
    }

    /**
     * Este metodo extrae el parametro del reuqest que representa el nombre de
     * la revista de la cual se quiere el pdf, la envia el metoso traerPdf
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void controladorDevolverPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializamos las consultas
        //extraemos el nombre de la revista para traer su miniatura
        String nombreRevista = request.getParameter("nombreRevista");
        String usuarioCreador = request.getParameter("usuarioCreador");
        BufferedInputStream fileStream = new BufferedInputStream(consulta.traerPdf(nombreRevista, usuarioCreador));//mandamos a traer la foto del usuario
        response.setContentType("application/pdf");//especificamos que es una ong
        int data;
        while ((data = fileStream.read()) > -1) {//escribimos el archivo en el response
            response.getOutputStream().write(data);
        }
    }

    private void recomendarRevistas(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorString(String.class);//inicializar un convertidor como un convertidor de string      
        String nombreUsuario = String.valueOf(convertidor.deJsonAClase(jsonRequest));//traer el nombre del usuario con el convertidor      
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());//inicializamos la consulta      
        ArrayList<Revista> cardsRevistas = consulta.recomendarRevistas(nombreUsuario);//mandamos a traer un array de revistas con las revistas recomendadas por el susuairo        
        Gson gson = new Gson();//inicializar el convertidor gson       
        String jsonResponse = gson.toJson(cardsRevistas);//convertir el array a json   
        response.getWriter().append(jsonResponse);//adjuntar la respuesta
    }

    private void buscarRevistasPorCategoria(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//Crear nueva consulta      
        Convertidor convertidor = new ConvertidorString(String.class); //Crear un nuevo convertidor        
        String busqueda = String.valueOf(convertidor.deJsonAClase(jsonRequest));//crear un string que representa la busqueda con el convertidor      
        ArrayList<Revista> revistas = consulta.buscarRevistasPorCategoria(busqueda);//crear un array de Revista a partir del metodo      
        Gson gson = new Gson();//convertirmos ese array con la herramienta gson
        String arrayEnJson = gson.toJson(revistas);
        response.getWriter().append(arrayEnJson);//adjuntamos el json al response
    }

    private void buscarRevistasPorTag(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//Crear nueva consulta      
        Convertidor convertidor = new ConvertidorString(String.class);//Crear un nuevo convertidor    
        String busqueda = String.valueOf(convertidor.deJsonAClase(jsonRequest));//crear un string que representa la busqueda con el convertidor       
        ArrayList<Revista> revistas = consulta.buscarRevistasPorTag(busqueda);//crear un array de Revista a partir del metodo         
        Gson gson = new Gson();//convertirmos ese array con la herramienta gson
        String arrayEnJson = gson.toJson(revistas);
        response.getWriter().append(arrayEnJson);//adjuntamos el json al response
    }

    private void verSuscripciones(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorString(String.class);//inicializar un convertidor como un convertidor de string      
        String nombreUsuario = String.valueOf(convertidor.deJsonAClase(jsonRequest));//traer el nombre del usuario con el convertidor        
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());//inicializamos la consulta
        ArrayList<Revista> cardsRevistas = consulta.verSuscripcionesDeUsuario(nombreUsuario);//mandamos a traer un array de revistas con las revistas recomendadas por el susuairo
        Gson gson = new Gson(); //inicializar el convertidor gson    
        String jsonResponse = gson.toJson(cardsRevistas);//convertir el array a json
        response.getWriter().append(jsonResponse); //adjuntar la respuesta
    }

    private void verSuscripcion(String jsonRequest, HttpServletResponse response) throws IOException {
        //Inicializamos un convertidor como ConvertidorCOnsultaSuscripcion para obtener los datos del json de request
        Convertidor convertidor = new ConvertidorSuscripcion(Suscripcion.class);
        Suscripcion consultaSuscripcion = (Suscripcion) convertidor.deJsonAClase(jsonRequest);//obteneoms el objeto del json
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto()); //creamos la consulta
        Boolean bandera = consulta.verSiUsuarioEstaSuscritoARevista(consultaSuscripcion);//mandamos a consular la
        convertidor = new ConvertidorBooleano(Boolean.class);//ahora convertimos el convertidor a booleano
        String jsonResponse = convertidor.deObjetoAJson(bandera);//convrtimos la bandera a json
        response.getWriter().append(jsonResponse);//insertamos el json al response
    }

    public void retornarResumen(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorRevista(Revista.class);//inicializamos un convertidor
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializmaos una consulta de revista
        Revista infoRevista = (Revista) convertidor.deJsonAClase(jsonRequest);//obtenemos una revista a partir del json
        Revista revistaResponse = consulta.buscarInfoDeRevista(infoRevista);//obtenemos la informacion de la revitsa
        String jsonResponse = convertidor.deObjetoAJson(revistaResponse);
        response.getWriter().append(jsonResponse);
    }

    public void retornarTags(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorRevista(Revista.class);//inicializamos un convertidor
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializmaos una consulta de revista
        Revista infoRevista = (Revista) convertidor.deJsonAClase(jsonRequest);//obtenemos una revista a partir del json
        ArrayList<Tag> tagsRevista = consulta.retornarTagsDeRevista(infoRevista);//obtenemos la informacion de la revitsa
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(tagsRevista);
        response.getWriter().append(jsonResponse);
    }

    public void retornarCostoRevista(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorRevista(Revista.class);//inicializamos un convertidor
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializmaos una consulta de revista
        Revista infoRevista = (Revista) convertidor.deJsonAClase(jsonRequest);//obtenemos una revista a partir del json
        double costo = consulta.retornarPrecioDeRevista(infoRevista);//obtenemos la informacion de la revitsa
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(costo);
        response.getWriter().append(jsonResponse);
    }

    public void suscribirseARevista(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidorSuscripcion = new ConvertidorSuscripcion(Suscripcion.class);//creamos un convertidor
        Convertidor convertidorString = new ConvertidorString(String.class);//creamos un convertidor de String
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());//inicializamos una consukta
        Suscripcion suscripcion = (Suscripcion) convertidorSuscripcion.deJsonAClase(jsonRequest);//traemos el objeto contenido en el json
        String confimacion = consulta.suscribirseARevista(suscripcion);//mandamos la suscripcion
        //trasformamos la condifmacion a json
        String jsonResponse = convertidorString.deObjetoAJson(confimacion);
        response.getWriter().append(jsonResponse);
    }

    public void saberEstadoDeInteraccionesConRevista(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorRevista(Revista.class);//inicializamos un convertidor
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializmaos una consulta de revista
        Revista infoRevista = (Revista) convertidor.deJsonAClase(jsonRequest);//obtenemos una revista a partir del json
        InteraccionConRevista interaccion = consulta.saberEstadoDeInteraccionesConRevista(infoRevista.getNombreRevista(), infoRevista.getUsuarioCreador());//obtenemos la informacion de la revitsa
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(interaccion);
        response.getWriter().append(jsonResponse);
    }

    public void darLikeARevista(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidor = new ConvertidorRevista(Revista.class);//inicializamos un convertidor
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());//inicializmaos una consulta de revista
        Revista infoRevista = (Revista) convertidor.deJsonAClase(jsonRequest);//obtenemos una revista a partir del json
        consulta.darLikeARevista(infoRevista);
    }

    public void registrarComentario(String jsonRequest, HttpServletResponse response) throws IOException {
        //inicializar los convertidores pertinentes
        Convertidor convertidorComentario = new ConvertidorComentario(Comentario.class);
        Convertidor convertidorString = new ConvertidorString(String.class);
        //
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());
        //extraer el comentario contenido en el request
        Comentario comentario = (Comentario) convertidorComentario.deJsonAClase(jsonRequest);
        //mandamos a insertar el comentario
        String confirmacion = consulta.hacerComentario(comentario);
        //convertimos la confirmacion en json
        String jsoConfirmacion = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsoConfirmacion);
    }

    public void retornarComentarios(String jsonRequest, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        Convertidor convertidor = new ConvertidorRevista(Revista.class);
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());
        //obtenemos la revista contenida en el reuqest
        Revista revista = (Revista) convertidor.deJsonAClase(jsonRequest);
        ArrayList<Comentario> comentarios = consulta.traerComentariosDeUnaRevista(revista);//traemos los comentarios de la revista
        String jsonResponse = gson.toJson(comentarios);//convertimos el array en json
        response.getWriter().append(jsonResponse);//retornamos el json
    }

    public void realizarPago(String jsonRequest, HttpServletResponse response) throws IOException {
        Convertidor convertidorPago = new ConvertidorPago(Pago.class);
        Convertidor convertidorString = new ConvertidorString(String.class);
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());
        Pago pago = (Pago) convertidorPago.deJsonAClase(jsonRequest);//convertimos la fecha de pago en un string
        String confirmacion = "La fecha esta vacia";
        if (pago.getFechaPago() != null && !pago.getFechaPago().isEmpty()) {//verificamos que la fecha no esta vaca o nula
            confirmacion = consulta.hacerPago(pago);//mandamos a insertar el pago a la bd
            String jsonResponse = convertidorString.deObjetoAJson(confirmacion);
            response.getWriter().append(jsonResponse);
        } else {
            String jsonResponse = convertidorString.deObjetoAJson(confirmacion);
            response.getWriter().append(jsonResponse);
        }
    }

    public void traerTodasLasRevistas(HttpServletResponse response) throws IOException {
        ConsultaRevista consulta = new ConsultaRevista(new ConstructorDeObjeto());//inicializamos la consulta que tiene el metodo para traer todas las revistas
        Gson gson = new Gson();//herramienta que convertira el array en json
        ArrayList<Revista> revistas = consulta.traerTodasLasRevisas();//mandamos a traer todas las revistas
        String jsonResponse = gson.toJson(revistas);//convertimos el array en json
        response.getWriter().append(jsonResponse);//adjuntamos el json
    }
}
