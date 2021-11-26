/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConstructorDeObjeto;
import ConsultasALaDb.ConsultaTag;
import Convetidores.*;
import ExtractorDeStringDelRequest.ExtractorDeStringRequest;
import ModelosApi.Tag;
import ModelosApi.TagUsuario;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Este servlet manejara todas las transacciones que el usuario hara con sus
 * tags (eliminar, retornar, agregar)
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorTagsDelUsuario", urlPatterns = {"/ControladorTagsDelUsuario"})
public class ControladorTagsDelUsuario extends HttpServlet {

    private ConsultaTag consulta;
    private Convertidor convertidor;
    private ExtractorDeStringRequest extractor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * Regula todas las peticiones post que llegan desde el frontend
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //inicializarmos la request y el response
        this.request = request;
        this.response = response;
        //inicializar la consulta
        consulta = new ConsultaTag(new ConstructorDeObjeto());
        //inicializar el extractor
        extractor = new ExtractorDeStringRequest(request);
        //por ultimo extraemos el json que trae el request
        String jsonRequest = extractor.extraerStringDeRequest();
        //verificamos el header "Accion" para saber que desea hacer el usuario
        switch (request.getHeader("Accion")) {
            case "retornarTagsDelUsuario":
                controlarRetornoDeTagsDelUsuario(jsonRequest);
                break;
            case "agregarNuevoTag":
                controlarAgregacionDeNuevoTagParaUsuario(jsonRequest);
                break;
            case "eliminarTag":
                controlarEliminacionDeTagDelPerfilDelUsuario(jsonRequest);
                break;
        }
    }

    /**
     * Regula el retorno de todas las tags de un usuario mediante el metodo
     * traerTodosLosTagsDeUnUsuario dentro de la clase ConsultaTag
     *
     * @param jsonRequest
     * @throws IOException
     */
    private void controlarRetornoDeTagsDelUsuario(String jsonRequest) throws IOException {
        //inicializar el gson
        Gson gson = new Gson();
        //inicializar el convertidor
        convertidor = new ConvertidorString(String.class);
        //pasamos el json a string para obtener el nombre del usuario
        String nombreUsuario = String.valueOf(convertidor.deJsonAClase(jsonRequest));
        //mandamos a traer todos los tags existentes para el usuario de la base de datos
        ArrayList<Tag> tags = consulta.traerTodosLosTagsDeUnUsuario(nombreUsuario);
        //construir un json del arrayList
        String tagsJson = gson.toJson(tags);
        //adjuntar el string json al response
        response.getWriter().append(tagsJson);
    }

    /**
     * Este metodo controla la agregacion de un nuevo tag de un usuario en
     * cuestion mediante el metodo agregarTagUsuario dentro de la clase
     * ConsultaTag
     *
     * @param jsonRequest
     * @throws IOException
     */
    private void controlarAgregacionDeNuevoTagParaUsuario(String jsonRequest) throws IOException {
        //inicializar el convertidor como un ConvertidorTagUsuario para obtener el tag a agregar en formato json
        convertidor = new ConvertidorTagUsuario(TagUsuario.class);
        //convertir el string en objeto
        TagUsuario tagAgregar = (TagUsuario) convertidor.deJsonAClase(jsonRequest);
        //mandar a eliminar el tag
        String confirmacion = consulta.agregarTagUsuario(tagAgregar);
        //volver a inicializar el convertidor como ConvertidorString para devolver la respuesta del metodo en la clase ConsultaTag
        convertidor = new ConvertidorString(String.class);
        //pasamos la confirmacion a Json
        String jsonResponse = convertidor.deObjetoAJson(confirmacion);
        //retornamos el response
        response.getWriter().append(jsonResponse);
    }

    /**
     * Este metodo controla la eliminacion de los tags del usuario mediante la
     * invocacion del metodo eliminarTagDeUsuario contenido en la clase
     * ConsultaTag
     *
     * @param jsonRequest
     */
    private void controlarEliminacionDeTagDelPerfilDelUsuario(String jsonRequest) {
        //inicializar el convertidor como un ConvertidorTagUsuario para obtener el tag a eliminar en formato json
        convertidor = new ConvertidorTagUsuario(TagUsuario.class);
        //convertir el string en objeto
        TagUsuario tagAEliminar = (TagUsuario) convertidor.deJsonAClase(jsonRequest);
        //mandar a eliminar el tag
        String confirmacion = consulta.eliminarTagDeUsuario(tagAEliminar);
        //volver a inicializar el convertidor como ConvertidorString para devolver la respuesta del metodo en la clase ConsultaTag
        convertidor = new ConvertidorString(String.class);
        //pasamos la confirmacion a Json
        String jsonResponse = convertidor.deObjetoAJson(confirmacion);
        try {
            //retornamos el response
            response.getWriter().append(jsonResponse);
        } catch (IOException ex) {
            Logger.getLogger(ControladorTagsDelUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
