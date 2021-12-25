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
import java.util.ArrayList;
import modelos.Anunciante;
import modelos.CostoPorDia;
import modelos.Revista;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ConstroladorConsultaAdminstrativa", urlPatterns = {"/ConstroladorConsultaAdminstrativa"})
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
        String jsonRequest = extractor.extraerStringDeRequest();
        switch (request.getHeader("Accion")) {
            case "asignarCostoPorDia":
                asignarCostoPorDia(jsonRequest, response);
                break;
            case "verCostoPorDia":
                verCostoPorDia(jsonRequest, response);
                break;
            case "crearAnunciante":
                crearAnunciante(jsonRequest, response);
                break;
            case "traerAnunciantes":
                traerAnunciantes(response);
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
}
