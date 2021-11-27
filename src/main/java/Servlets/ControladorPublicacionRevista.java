/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.*;
import ModelosApi.Categoria;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorPublicacionRevista", urlPatterns = {"/ControladorPublicacionRevista"})
public class ControladorPublicacionRevista extends HttpServlet {

    private HttpServletRequest request;
    private HttpServletResponse response;

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
        //inicializamos la reuqest y el response
        this.request = request;
        this.response = response;
        switch (request.getHeader("Accion")) {
            case "devolverCategorias":
                controlarDevolucionDeCategorias();
                break;
        }
    }

    /**
     * Este metodo consigue un ArrayList con todas las Categoria de la base de
     * datos y las convierte en un Json para retornarlas al response
     *
     * @throws IOException
     */
    private void controlarDevolucionDeCategorias() throws IOException {
        //objetos que serviran en este metodo
        Consulta consulta = new ConsultaLogin(new ConstructorDeObjeto());
        Gson gson = new Gson();
        //mandar a traer todas las tags a la bd
        ArrayList<Categoria> categorias = consulta.devolverCategorias();
        //convertir el ArrayList a un Json
        String jsonResponse = gson.toJson(categorias);
        //adjuntar el Json al response
        response.getWriter().append(jsonResponse);
    }

}
