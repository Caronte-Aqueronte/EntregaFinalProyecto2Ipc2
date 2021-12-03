/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.*;
import Convetidores.*;
import ExtractorDeStringDelRequest.ExtractorDeStringRequest;
import ModelosApi.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorAccionRevista", urlPatterns = {"/ControladorAccionRevista"})
@MultipartConfig(location = "tmp")
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
        switch (request.getHeader("Accion")) {
            case "devolverCategorias":
                controlarDevolucionDeCategorias(response);
                break;
            case "surbirRevista":
                controlarSubidaDeRevista(request, response);
                break;
            case "guardarTags":
                controlarGuardarTagsDeRevista(request, response);
                break;
            case "recomendarRevistas":
                recomendarRevistas(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        controladorDevolverCaratulaDelPdf(request, response);
    }

    /**
     * Este metodo consigue un ArrayList con todas las Categoria de la base de
     * datos y las convierte en un Json para retornarlas al response
     *
     * @throws IOException
     */
    private void controlarDevolucionDeCategorias(HttpServletResponse response) throws IOException {
        //objetos que serviran en este metodo
        Consulta consulta = new ConsultaUsuario(new ConstructorDeObjeto());
        Gson gson = new Gson();
        //mandar a traer todas las tags a la bd
        ArrayList<Categoria> categorias = consulta.devolverCategorias();
        //convertir el ArrayList a un Json
        String jsonResponse = gson.toJson(categorias);
        //adjuntar el Json al response
        response.getWriter().append(jsonResponse);
    }

    /**
     * Este metodo manda a construir una Revista y la manda a insertar
     *
     * @throws IOException
     * @throws ServletException
     */
    private void controlarSubidaDeRevista(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //objetos que nos serviran en este metodo
        ConsultaUsuarioEditor consulta = new ConsultaUsuarioEditor(new ConstructorDeObjeto());
        Convertidor convertidor = new ConvertidorString(String.class);
        //mandamos a construir un un Objeto Revista a partir de la reuqest
        Revista revista = consulta.getConstructorObjeto().crearRevistaDeUnRequest(request);
        //mandarmos a publicar la revista y guardamos la respuesta en un string
        String confirmacion = consulta.publicarRevista(revista);
        //convertir la respuesta
        String jsonConfirmacion = convertidor.deObjetoAJson(confirmacion);
        //adjuntamos la respuesta al response
        response.getWriter().append(jsonConfirmacion);

    }

    /**
     * Este metodo extrae el json que de la request que contiene el listado de
     * tags que se asignaran a una revista y devuelve la respuesta de las
     * inserciones
     *
     * @throws IOException
     */
    private void controlarGuardarTagsDeRevista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExtractorDeStringRequest extractor = new ExtractorDeStringRequest(request);
        //Creamos una nueva consulta
        ConsultaUsuarioEditor consulta = new ConsultaUsuarioEditor(new ConstructorDeObjeto());
        //extraemos el json de la request
        String jsonDelRequest = extractor.extraerStringDeRequest();
        //el josn lo pasamos a objeto
        Gson gson = new Gson();
        ArrayList<TagRevista> tags = gson.fromJson(jsonDelRequest, new TypeToken<ArrayList<TagRevista>>() {
        }.getType());
        String respuesta = "";
        for (TagRevista item : tags) {//por cada elemento del array insertamos el tan
            System.out.println(item.getNombreTag());
            respuesta += "\n" + consulta.guardarTagDeRevista(item);//adjuntamos la respuesta a la respuesta general
        }
        //inicializamos el convertidor como un convertidor String
        Convertidor convertidor = new ConvertidorString(String.class);
        //convertimos la respuesta a Json 
        String respuestaJson = convertidor.deObjetoAJson(respuesta);
        //adjuntamos la respuesta al response
        response.getWriter().append(respuestaJson);
    }

    private void controladorDevolverCaratulaDelPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConsultaUsuarioEditor consulta = new ConsultaUsuarioEditor(new ConstructorDeObjeto());//inicializamos las consultas
        //extraemos el nombre de la revista para traer su miniatura
        String nombreRevista = request.getParameter("nombreRevista");
        //String nombreUsuario = request.getParameter("nombreUsuario");//obtenemos el nombre del usuario por medio del la reuqest
        BufferedInputStream fileStream = new BufferedInputStream(consulta.traerPdf(nombreRevista));//mandamos a traer la foto del usuario
        response.setContentType("image/png");//especificamos que es una ong
        int data;
        while ((data = fileStream.read()) > -1) {//escribimos el archivo en el response
            response.getOutputStream().write(data);
        }
    }

    private void recomendarRevistas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExtractorDeStringRequest extractor = new ExtractorDeStringRequest(request);
        //extraemos el json de la reuqest
        String jsonRequest = extractor.extraerStringDeRequest();
        //inicializar un convertidor como un convertidor de string
        Convertidor convertidor = new ConvertidorString(String.class);
        //traer el nombre del usuario con el convertidor
        String nombreUsuario = String.valueOf(convertidor.deJsonAClase(jsonRequest));
        //inicializamos la consulta
        ConsultaUsuarioLector consulta = new ConsultaUsuarioLector(new ConstructorDeObjeto());
        //mandamos a traer un array de revistas con las revistas recomendadas por el susuairo
        ArrayList<Revista> cardsRevistas = consulta.recomendarRevistas(nombreUsuario);
        //inicializar el convertidor gson
        Gson gson = new Gson();
        //convertir el array a json
        String jsonResponse = gson.toJson(cardsRevistas);
        //adjuntar la respuesta
        response.getWriter().append(jsonResponse);
    }

}
