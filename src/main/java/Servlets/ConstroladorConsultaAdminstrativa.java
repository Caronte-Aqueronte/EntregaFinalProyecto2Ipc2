/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConsultaUsuarioAdministrador;
import ConsultasALaDb.ConsultaRevista;
import herramientas.ExtractorDeStringRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import herramientas.ConstructorDeObjeto;
import Convetidores.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import javax.servlet.annotation.MultipartConfig;
import modelos.Anunciante;
import modelos.AnuncioImagen;
import modelos.AnuncioTexto;
import modelos.CostoPorDia;
import modelos.Revista;
import modelos.TagAnuncio;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ConstroladorConsultaAdminstrativa", urlPatterns = {"/ConstroladorConsultaAdminstrativa"})
@MultipartConfig(location = "tmp")
public class ConstroladorConsultaAdminstrativa extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
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
        String jsonRequest;
        switch (request.getHeader("Accion")) {
            case "asignarCostoPorDia":
                jsonRequest = extractor.extraerStringDeRequest();
                asignarCostoPorDia(jsonRequest, response);
                break;
            case "verCostoPorDia":
                jsonRequest = extractor.extraerStringDeRequest();
                verCostoPorDia(jsonRequest, response);
                break;
            case "crearAnunciante":
                jsonRequest = extractor.extraerStringDeRequest();
                crearAnunciante(jsonRequest, response);
                break;
            case "traerAnunciantes":
                traerAnunciantes(response);
                break;
            case "guardarTagsAnuncio":
                jsonRequest = extractor.extraerStringDeRequest();
                guardarTagsAnuncio(jsonRequest, response);
                break;
            case "guardarAnuncioTexto":
                jsonRequest = extractor.extraerStringDeRequest();
                guardarAnuncioTexto(jsonRequest, response);
                break;
            case "guardarAnuncioImagen":
                guardarAnuncioImagen(request, response);
                break;
        }
    }

    public void asignarCostoPorDia(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaUsuarioAdministrador consultaAdministrativa = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());//consutla que tiene el metodo guardarCostoPorDia
        Convertidor convertidorCostoPorDia = new ConvertidorCostoPorDia(CostoPorDia.class);//convertidor
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidor
        //obtenemos el costo del jsonReuqest
        CostoPorDia costo = (CostoPorDia) convertidorCostoPorDia.deJsonAClase(jsonRequest);
        //mandamos a guardar el costo por dia a la bd
        String confimacion = consultaAdministrativa.guardarCostoPorDia(costo);
        String jsonResponse = convertidorString.deObjetoAJson(confimacion);
        response.getWriter().append(jsonResponse);
    }

    public void verCostoPorDia(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaRevista consultaRevista = new ConsultaRevista(new ConstructorDeObjeto());//consutla que tiene el metodo guardarCostoPorDia
        Convertidor convertidorRevista = new ConvertidorRevista(Revista.class);
        Gson gson = new Gson();
        Revista revista = (Revista) convertidorRevista.deJsonAClase(jsonRequest);
        //mandamos a guardar el costo por dia a la bd
        double costoPorDia = consultaRevista.verCostoPorDiaDeRevista(revista);
        String jsonResponse = gson.toJson(costoPorDia);
        response.getWriter().append(jsonResponse);
    }

    public void crearAnunciante(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaUsuarioAdministrador consulta = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());
        Convertidor convertidorAnunciante = new ConvertidorAnunciante(Anunciante.class);//convertidor para extraer el anunciante de la quequest
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidor para crear el jsonResponse
        Anunciante anunciante = (Anunciante) convertidorAnunciante.deJsonAClase(jsonRequest); //sacamos el anunciante
        String confirmacion = consulta.guardarAnunciante(anunciante);//mandamos a guardar el anunciante
        String jsonReposponse = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsonReposponse);
    }

    public void traerAnunciantes(HttpServletResponse response) throws IOException {
        ConsultaUsuarioAdministrador consulta = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());
        Gson gson = new Gson();//objeto que convertira el array en json
        ArrayList<Anunciante> anunciantes = consulta.traerAnunciantes();//mandamos a traer todos los anunciantes
        String jsonReposponse = gson.toJson(anunciantes);//construimos el json con gson
        response.getWriter().append(jsonReposponse);
    }

    public void guardarTagsAnuncio(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaUsuarioAdministrador consulta = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());
        Gson gson = new Gson();//creamos un objeto gson para obtener los tags del rquest
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidor para crear el jsonResponse
        ArrayList<TagAnuncio> tags = gson.fromJson(jsonRequest, new TypeToken<ArrayList<TagAnuncio>>() {
        }.getType());//traemos los tags que estan contenidos enel request
        String confirmacion = consulta.guardarTagsAnuncio(tags);//mandamos a guardar los tags
        String jsonReponse = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsonReponse);
    }

    public void guardarAnuncioTexto(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaUsuarioAdministrador consulta = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());
        Convertidor convertidorAnuncio = new ConvertidorAnuncioTexto(AnuncioTexto.class);//convertidor para extraer el anuncio de texto de la quequest
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidor para crear el jsonResponse
        AnuncioTexto anuncio = (AnuncioTexto) convertidorAnuncio.deJsonAClase(jsonRequest);//traemos el anuncio del request
        String confirmacion = consulta.gurdarAnuncioDeTexto(anuncio);//mandamos a guardar el anunciante
        String jsonReponse = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsonReponse);
    }

    public void guardarAnuncioImagen(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ConsultaUsuarioAdministrador consulta = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidor para crear el jsonResponse
        AnuncioImagen anuncio = consulta.getConstructorObjeto().construirAnuncioImagenDeRequest(request);
        String confirmacion = consulta.guardarAnuncioImagen(anuncio);//mandamos a guardar el anunciante
        String jsonReponse = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsonReponse);
    }
    public void guardarAnuncioVideo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ConsultaUsuarioAdministrador consulta = new ConsultaUsuarioAdministrador(new ConstructorDeObjeto());
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidor para crear el jsonResponse
        AnuncioImagen anuncio = consulta.getConstructorObjeto().construirAnuncioImagenDeRequest(request);
        String confirmacion = consulta.guardarAnuncioImagen(anuncio);//mandamos a guardar el anunciante
        String jsonReponse = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsonReponse);
    }
}
