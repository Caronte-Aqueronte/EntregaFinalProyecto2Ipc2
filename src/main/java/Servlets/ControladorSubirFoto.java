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
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorSubirFoto", urlPatterns = {"/ControladorSubirFoto"})
@MultipartConfig(location = "tmp")
public class ControladorSubirFoto extends HttpServlet {

    private ConsultaUsuario consulta;
    private Convertidor convertidorString;

    /**
     * Metodo que retorna la imagen de perfil de un usuario que espeficique la
     * request
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        consulta = new ConsultaUsuario(new ConstructorDeObjeto());//inicializamos las consultas
        String nombreUsuario = request.getParameter("nombreUsuario");//obtenemos el nombre del usuario por medio del la reuqest
        BufferedInputStream fileStream = new BufferedInputStream(consulta.traerFoto(nombreUsuario));//mandamos a traer la foto del usuario
        response.setContentType("image/png");//especificamos que es una ong
        int data;
        while ((data = fileStream.read()) > -1) {//escribimos el archivo en el response
            response.getOutputStream().write(data);
        }

    }

    /**
     * Este metodo manda a guardar la foto del usuario a la base de datos
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        convertidorString = new ConvertidorString(String.class);//inicializamos el convertidor
        consulta = new ConsultaUsuario(new ConstructorDeObjeto());//inicializar consulta
        String nombreUsuario = request.getParameter("nombreUsuario");//obtener el nombre del usuario del request
        Part parteArchivo = request.getPart("datafile");//obtener las partes del archivo
        InputStream stream = parteArchivo.getInputStream();//obtener el inputstream
        response.getWriter().append(convertidorString.deObjetoAJson(consulta.guardarFoto(stream, nombreUsuario)));//responder al llamados
    }
}
