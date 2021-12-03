/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.*;
import Convetidores.*;
import ExtractorDeStringDelRequest.ExtractorDeStringRequest;
import ModelosApi.Perfil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Este servlet manejara las acciones relacionadas con el perfil de los usuarios
 * (retornar, guardar un perfil), las acciones mas especificas como el manejo de
 * tags y el manejo de archivos se realizan en otros servlets.
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorPerfil", urlPatterns = {"/ControladorPerfil"})
public class ControladorPerfil extends HttpServlet {

    /**
     * Metodo que regula todas las request de tipo post que vienen desde el
     * frontend
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //inicializamos el extractor
        ExtractorDeStringRequest extractor = new ExtractorDeStringRequest(request);
        //conseguimos el json de la request
        String jsonRequest = extractor.extraerStringDeRequest();
        switch (request.getHeader("Accion")) {
            case "guardarPerfil":
                controlarGuardadoDePerfil(jsonRequest, response);
                break;
            case "retornarPerfil":
                controlarRetornoDePerfil(jsonRequest, response);
                break;
        }
    }

    /**
     * Este metodo regula la accion de guardar un nuevo perfil o en su defecto
     * la creacion de uno nuevo (la accion crear o actualizar perfil la regula
     * el metodo guardarPerfil dentro de la clase ConsultaUsuario).
     *
     * @param jsonRequest
     * @throws IOException
     */
    private void controlarGuardadoDePerfil(String jsonRequest, HttpServletResponse response) throws IOException {
        //iniicir la consulta
        ConsultaUsuario consulta = new ConsultaUsuario(new ConstructorDeObjeto());
        //inicializar el convertidor como un ConvertidorPerfil para obtener el objeto del string jsonRequest
        Convertidor convertidor = new ConvertidorPerfil(Perfil.class);
        //crear un Perfil con base al json mandado
        Perfil perfil = (Perfil) convertidor.deJsonAClase(jsonRequest);
        //mandar aingresar el nuevo perfil
        String respuesta = consulta.guardarPerfil(perfil);
        //inicializar el convertidor como ConvertidorString para enviar la respuesta escrita del metodo guardarPerfil
        convertidor = new ConvertidorString(String.class);
        response.getWriter().append(convertidor.deObjetoAJson(respuesta));
    }

    /**
     * Este metodo regula la accion de retornar un perfil en espefico segun el
     * nombre de usuario que se especifique en el jsonRequest, logra el objetivo
     * usando el metodo obtenerPerfilDeUsuario dentro de la clase
     * ConsultaUsuario.
     *
     * @param jsonRequest
     * @throws IOException
     */
    private void controlarRetornoDePerfil(String jsonRequest, HttpServletResponse response) throws IOException {
        //inicializar la consulta
        ConsultaUsuario consulta = new ConsultaUsuario(new ConstructorDeObjeto());
        //inicializar el convertidor como un ConvertidorString para obtener el nombre del usuario adjunto en el jsonRequest
       Convertidor convertidor = new ConvertidorString(String.class);
        //obtener el nombre del usuario del request en el string
        String nombreDeUsuario = (String) convertidor.deJsonAClase(jsonRequest);
        //obtenemos el perfil que corresponde al nombre del usuario que se me mando
        Perfil perfilResponse = consulta.obtenerPerfilDeUsuario(nombreDeUsuario);
        //inicializar el convertidor como un ConvertidorPerfil para pasar el objeto "perfilResponse" a un json
        convertidor = new ConvertidorPerfil(Perfil.class);
        //pasamos el perfil a json
        response.getWriter().append(convertidor.deObjetoAJson(perfilResponse));
    }

}
