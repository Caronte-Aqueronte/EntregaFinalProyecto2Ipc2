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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorRetornarPerfil", urlPatterns = {"/ControladorRetornarPerfil"})
public class ControladorRetornarPerfil extends HttpServlet {

    private ExtractorDeStringRequest extractor;
    private Convertidor convertidorPerfil;
    private Convertidor convertidorString;
    private Perfil perfil;
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
        //inicializar la consulta
        ConsultaUsuario consultaADb = new ConsultaUsuario(new ConstructorDeObjeto());
        //inicializar el extractor
        extractor = new ExtractorDeStringRequest(request);
        //inicializar los convertidores
        convertidorPerfil = new ConvertidorPerfil(Perfil.class);
        convertidorString = new ConvertidorString(String.class);             ;
        //extraer el string json del request
        String jsonDelRequest = extractor.extraerStringDeRequest();
        //obtener el nombre del usuario del request en el string
        String nombreDeUsuario = (String) convertidorString.deJsonAClase(jsonDelRequest);
        Perfil pefilResponse = consultaADb.obtenerPerfilDeUsuario(nombreDeUsuario);
        //pasamos el perfil a json
        response.getWriter().append(convertidorPerfil.deObjetoAJson(pefilResponse));
    }

}
