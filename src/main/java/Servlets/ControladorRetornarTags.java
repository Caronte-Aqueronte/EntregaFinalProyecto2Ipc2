/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.*;
import ModelosApi.Tag;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Este servlet manejara todas las transacciones que el usuario hara con sus
 * tags (eliminar, retornar, agregar)
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorRetornarTags", urlPatterns = {"/ControladorRetornarTags"})
public class ControladorRetornarTags extends HttpServlet {


    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //inicializar la consulta
        ConsultaTag consulta = new ConsultaTag(new ConstructorDeObjeto());
        //inicializar el convertidor
        Gson gson = new Gson();
        //mandamos a traer todos los tags existentes de la base de datos
        ArrayList<Tag> tags = consulta.traerTodosLosTags();
        //construir un json del arrayList
        String tagsJson = gson.toJson(tags);
        //adjuntar el string json al response
        response.getWriter().append(tagsJson);

    }
}
