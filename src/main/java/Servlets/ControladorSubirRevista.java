/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConsultaUsuarioEditor;
import Convetidores.Convertidor;
import Convetidores.ConvertidorString;
import modelos.Revista;
import herramientas.ConstructorDeObjeto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorSubirRevista", urlPatterns = {"/ControladorSubirRevista"})
@MultipartConfig(location = "tmp")
public class ControladorSubirRevista extends HttpServlet {

    /**
     * Este metodo manda a construir una Revista y la manda a insertar
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConsultaUsuarioEditor consulta = new ConsultaUsuarioEditor(new ConstructorDeObjeto());//objetos que nos serviran en este metodo
        Convertidor convertidor = new ConvertidorString(String.class);//mandamos a construir un un Objeto Revista a partir de la reuqest      
        Revista revista = consulta.getConstructorObjeto().crearRevistaDeUnRequest(request);//mandarmos a publicar la revista y guardamos la respuesta en un string      
        String confirmacion = consulta.publicarRevista(revista);//convertir la respuesta       
        String jsonConfirmacion = convertidor.deObjetoAJson(confirmacion);//adjuntamos la respuesta al response       
        response.getWriter().append(jsonConfirmacion);

    }
}
