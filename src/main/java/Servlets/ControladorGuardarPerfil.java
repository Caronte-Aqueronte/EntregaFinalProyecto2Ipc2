/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConstructorDeObjeto;
import ConsultasALaDb.ConsultaUsuario;
import Convetidores.Convertidor;
import Convetidores.ConvertidorPerfil;
import Convetidores.ConvertidorString;
import ExtractorDeStringDelRequest.ExtractorDeStringRequest;
import ModelosApi.Perfil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorGuardarPerfil", urlPatterns = {"/ControladorGuardarPerfil"})
public class ControladorGuardarPerfil extends HttpServlet {

    private ExtractorDeStringRequest extractor;
    private Convertidor convertidorString;
    private Convertidor convertidorPerfil;
    private Perfil perfil;
    private ConsultaUsuario consulta;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //iniicir la consulta
            consulta = new ConsultaUsuario(new ConstructorDeObjeto());
            //inicializar el extractor
            extractor = new ExtractorDeStringRequest(request);
            //traer la string json de la request
            String jsonRequest = extractor.extraerStringDeRequest();
            //inicializar el convertidores
            convertidorPerfil = new ConvertidorPerfil(Perfil.class);
            convertidorString = new ConvertidorString(String.class);
            //crear un Perfil con base al json mandado
            perfil = (Perfil) convertidorPerfil.deJsonAClase(jsonRequest);
            //mandar aingresar el nuevo perfil
            String respuesta = consulta.guardarPerfil(perfil);
            response.getWriter().append(convertidorString.deObjetoAJson(respuesta));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
