/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConstructorDeObjeto;
import ConsultasALaDb.ConsultaUsuario;
import Convetidores.Convertidor;
import Convetidores.ConvertidorString;
import Convetidores.ConvertidorUsuario;
import ExtractorDeStringDelRequest.ExtractorDeStringRequest;
import ModelosApi.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "CrearUsuario", urlPatterns = {"/ControladorCrearUsuario"})
public class ControladorCrearUsuario extends HttpServlet {

    private ConsultaUsuario consulta;
    private ExtractorDeStringRequest extractor;
    private Convertidor convertidorUsuario;
    private Convertidor convertidorString;

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
        //inicializar los objetos que nos serviran;
        consulta = new ConsultaUsuario(new ConstructorDeObjeto());
        extractor = new ExtractorDeStringRequest(request);
        convertidorUsuario = new ConvertidorUsuario(Usuario.class);
        convertidorString = new ConvertidorString(String.class);
        //obtener el json del request
        String jsonRequest = extractor.extraerStringDeRequest();
        //obtener el usuario del json
        Usuario usuario = (Usuario) convertidorUsuario.deJsonAClase(jsonRequest);
        //mandamos la confirmacion dle metodo crearUsuario
        response.getWriter().append(convertidorString.deObjetoAJson(consulta.crearUsuario(usuario)));

    }

}
