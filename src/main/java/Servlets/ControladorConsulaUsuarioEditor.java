/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConsultaRevista;
import ConsultasALaDb.ConsultaUsuarioEditor;
import Convetidores.*;
import com.google.gson.Gson;
import herramientas.ConstructorDeObjeto;
import herramientas.ExtractorDeStringRequest;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelos.Revista;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorConsulaUsuarioEditor", urlPatterns = {"/ControladorConsulaUsuarioEditor"})
public class ControladorConsulaUsuarioEditor extends HttpServlet {

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
        String jsonRequest = extractor.extraerStringDeRequest();
        switch (request.getHeader("Accion")) {
            case "verRevistas":
                verTodasLasRevistas(jsonRequest, response);
                break;
            case "cambiarInteracciones":
                cambiarInteracciones(jsonRequest, response);
                break;
        }
    }

    public void verTodasLasRevistas(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaRevista consultaRevista = new ConsultaRevista(new ConstructorDeObjeto());//consutla que tiene el metodo guardarCostoPorDia
        ConvertidorString convertidorUsuario = new ConvertidorString(String.class);
        Gson gson = new Gson();
        String nombreUsuario = (String) convertidorUsuario.deJsonAClase(jsonRequest);//obtenemos el usuario contenido en le rquest
        ArrayList<Revista> revista = consultaRevista.traerTodasLasRevistasDeUnEditor(nombreUsuario); //obtenemos todas las revistas del usuario editor enviado    
        String jsonResponse = gson.toJson(revista);//convertimos las revisas en Json
        response.getWriter().append(jsonResponse);
    }
    
    public void cambiarInteracciones(String jsonRequest, HttpServletResponse response) throws IOException {
        ConsultaUsuarioEditor consultaEditor = new ConsultaUsuarioEditor(new ConstructorDeObjeto());//consulta que tiene el metodo para guardar las interacciones
        Convertidor convertidroRevista = new ConvertidorRevista(Revista.class);//convertidor para obtener la revista del request
        Convertidor convertidorString = new ConvertidorString(String.class);//convertidro que servira para responder al frontend
        Revista revista = (Revista) convertidroRevista.deJsonAClase(jsonRequest);//sacamos la revista contenida en el request
        String confirmacion = consultaEditor.guardarEstadosDeInteracciones(revista);
        String jsonResponse = convertidorString.deObjetoAJson(confirmacion);
        response.getWriter().append(jsonResponse);//respondemos con lo que diga el metodo
    }
}
