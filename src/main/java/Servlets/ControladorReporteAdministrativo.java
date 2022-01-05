/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConsultaReporteAdministrativo;
import herramientas.ConstructorDeObjeto;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorReporteAdministrativo", urlPatterns = {"/ControladorReporteAdministrativo"})
public class ControladorReporteAdministrativo extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String tipoReporte = request.getParameter("tipoReporte");
            switch (tipoReporte) {
                case "Ganancias":
                    reporteDeGanancias(request, response);
                    break;
                case "gananciasAnuncios":
                    reporteDeGananciasPorAnuncios(request, response);
                    break;
                case "gananciasTotales":
                    reporteDeGananciasTotales(response);
                    break;
                case "masPopulares":
                    reporte5RevistasMasPopulares(request, response);
                    break;
                case "masComentadas":
                    reporte5RevistasMasComentadas(request, response);
                    break;
                case "reporteAnuncios":
                    reporteDeAnuncios(request, response);
                    break;
                case "reporteEfectividadDeAnuncios":
                    reporteEfectividadDeAnuncios(request, response);
                    break;
            }
        } catch (NullPointerException e) {

        }
    }

    private void reporteDeGanancias(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //obtenemos los parametros para pasarle al metodo
        String primeraFecha = request.getParameter("primeraFecha");
        String segundaFecha = request.getParameter("segundaFecha");
        String nombreRevista = request.getParameter("nombreRevista");
        String nombreUsuarioCreador = request.getParameter("usuarioCreador");
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.reporteDeGanancias(nombreRevista, nombreUsuarioCreador, primeraFecha, segundaFecha);
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/GananciasPorRevistas.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Ganancias por revistas");
    }

    private void reporteDeGananciasPorAnuncios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //obtenemos los parametros para pasarle al metodo
        String primeraFecha = request.getParameter("primeraFecha");
        String segundaFecha = request.getParameter("segundaFecha");
        String nombreAnunciante = request.getParameter("nombreAnunciante");
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.reporteGananciasPorAnunciante(nombreAnunciante, primeraFecha, segundaFecha);
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/GananciaDeAnuncios.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Ganancias por anuncios");
    }

    private void reporteDeGananciasTotales(HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.reporteDeGananciasTotales();
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/GananciasTotales.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Ganancias totales");
    }

    private void reporte5RevistasMasPopulares(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //obtenemos los parametros para pasarle al metodo
        String primeraFecha = request.getParameter("primeraFecha");
        String segundaFecha = request.getParameter("segundaFecha");
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.reporteRevistasMasPopulares(primeraFecha, segundaFecha);
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/CincoRevistasMasPopulares.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Cinco revistas mas populares");
    }

    private void reporte5RevistasMasComentadas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //obtenemos los parametros para pasarle al metodo
        String primeraFecha = request.getParameter("primeraFecha");
        String segundaFecha = request.getParameter("segundaFecha");
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.reporteRevistasMasComentadas(primeraFecha, segundaFecha);
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/CincoMasComentadas.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Cinco revistas mas comentadas");
    }

    private void reporteDeAnuncios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //obtenemos los parametros para pasarle al metodo
        String primeraFecha = request.getParameter("primeraFecha");
        String segundaFecha = request.getParameter("segundaFecha");
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.reporteDeAnuncios(primeraFecha, segundaFecha);
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/ReporteAnuncios.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Reporde de anuncios");
    }

    private void reporteEfectividadDeAnuncios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //consulta que contiene los metodos para armar el reporte
        ConsultaReporteAdministrativo consulta = new ConsultaReporteAdministrativo(new ConstructorDeObjeto());
        //obtenemos los parametros para pasarle al metodo
        String primeraFecha = request.getParameter("primeraFecha");
        String segundaFecha = request.getParameter("segundaFecha");
        //mandamosa construir el reporte
        Map<String, Object> mapaDatos = consulta.efectividadDeAnuncios(primeraFecha, segundaFecha);
        InputStream reporteStream = getClass().getResourceAsStream("/reportes/EfectividadDeAnuncios.jasper");//treamos el reporte de los resorces
        cargarReportes(response, mapaDatos, reporteStream, "Reporde de efectividad de anuncios");
    }

    private void cargarReportes(HttpServletResponse response, Map<String, Object> mapaDatos, InputStream reporteStream, String nombreArchivo) throws IOException {
        try ( ServletOutputStream out = response.getOutputStream()) {//obtenemos el stream del servlet
            JasperReport reporte = (JasperReport) JRLoader.loadObject(reporteStream);//cargamos el reporte
            response.setContentType("application/pdf");//indicamos que es un pdf la salida
            response.addHeader("Content-disposition", "inline; filename=" + nombreArchivo + ".pdf");
            JasperPrint reportePintado = JasperFillManager.fillReport(reporte, mapaDatos, new JREmptyDataSource());//pintamos el reporte
            JasperExportManager.exportReportToPdfStream(reportePintado, out);//exportamos el reporte
            out.flush();
            out.close();
        } catch (JRException ex) {
        }
    }
}
