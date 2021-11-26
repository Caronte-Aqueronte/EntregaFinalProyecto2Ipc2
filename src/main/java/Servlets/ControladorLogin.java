/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConstructorDeObjeto;
import ConsultasALaDb.ConsultaLogin;
import Convetidores.*;
import ExtractorDeStringDelRequest.ExtractorDeStringRequest;
import ModelosApi.ModeloResponseLogin;
import ModelosApi.Usuario;
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
@WebServlet(name = "ControladorLogin", urlPatterns = {"/ControladorLogin"})
public class ControladorLogin extends HttpServlet {

    private ConsultaLogin consultasLogin = new ConsultaLogin(new ConstructorDeObjeto());

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
        ExtractorDeStringRequest extractor = new ExtractorDeStringRequest(request);//creamos un extractor para obtener el string json
        
        Convertidor convertidorUsuario = new ConvertidorUsuario(Usuario.class);//creamos un convertidor del tipo convertidor usuario
        Convertidor convertidorResponse = new ConvertidorResponseModeloLogin(ModeloResponseLogin.class);
        
        String jsonDelRequest = extractor.extraerStringDeRequest();//traemos el string que tiene el request
        Usuario usuarioParaLogin = (Usuario) convertidorUsuario.deJsonAClase(jsonDelRequest);//convertimos la cadena de entrada a objeto
        ModeloResponseLogin modeloRespuesta = consultasLogin.hacerLogin(usuarioParaLogin);
        response.getWriter().append(convertidorResponse.deObjetoAJson(modeloRespuesta));

    }
}
