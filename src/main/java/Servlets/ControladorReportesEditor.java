/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import ConsultasALaDb.ConsultaReporteEditor;
import herramientas.ConstructorDeObjeto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelosReportes.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Luis Monterroso
 */
@WebServlet(name = "ControladorReportesEditor", urlPatterns = {"/ControladorReportesEditor"})
public class ControladorReportesEditor extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipoReporte = request.getParameter("tipoReporte");
        switch (tipoReporte) {
            case "reporteComentarios":
                reporteComentarios(request, response);
                break;
            case "reporteSuscripciones":
                reporteSuscripciones(request, response);
                break;
            case "reporteLikes":
                reporteLikes(request, response);
                break;
            case "reporteGanancias":
                reporteGanancias(request, response);
                break;
        }
    }

    public void reporteComentarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ServletOutputStream out = response.getOutputStream();//obtenemos el stream del servlet
            String nombreRevista = request.getParameter("nombreRevista");//obtenemos los parametros de la url
            String primeraFecha = request.getParameter("primeraFecha");//
            String segundaFecha = request.getParameter("segundaFecha");//
            ConsultaReporteEditor consulta = new ConsultaReporteEditor(new ConstructorDeObjeto());//nueva consulta
            JRBeanArrayDataSource datosReporte = consulta.reporteDeComentarios(nombreRevista, primeraFecha, segundaFecha);//mandmaos a traer los datos del reporte
            InputStream reporteStream = getClass().getResourceAsStream("/reportes/Reporte comentarios.jasper");//treamos el reporte de los resorces
            JasperReport reporte = (JasperReport) JRLoader.loadObject(reporteStream);//cargamos el reorte
            Map<String, Object> mapaDatos = new HashMap<>();
            mapaDatos.put("datos", datosReporte);//anadimos al map los datos del reporte
            response.setContentType("application/pdf");//indicamos que es un pdf la salida
            response.addHeader("Content-disposition", "inline; filename=ReporteComentarios.pdf");
            JasperPrint reportePintado = JasperFillManager.fillReport(reporte, mapaDatos, datosReporte);//pintamos el reporte
            JasperExportManager.exportReportToPdfStream(reportePintado, out);//exportamos el reporte
            out.flush();
            out.close();
        } catch (JRException ex) {

        }
    }

    public void reporteSuscripciones(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ServletOutputStream out = response.getOutputStream();//obtenemos el stream del servlet
            String nombreRevista = request.getParameter("nombreRevista");//obtenemos los parametros de la url
            String primeraFecha = request.getParameter("primeraFecha");//
            String segundaFecha = request.getParameter("segundaFecha");//
            ConsultaReporteEditor consulta = new ConsultaReporteEditor(new ConstructorDeObjeto());//nueva consulta
            JRBeanArrayDataSource datosReporte = consulta.reporteDeSuscripciones(nombreRevista, primeraFecha, segundaFecha);//mandmaos a traer los datos del reporte
            InputStream reporteStream = getClass().getResourceAsStream("/reportes/Reporte suscripciones.jasper");//treamos el reporte de los resorces
            JasperReport reporte = (JasperReport) JRLoader.loadObject(reporteStream);//cargamos el reorte
            Map<String, Object> mapaDatos = new HashMap<>();
            mapaDatos.put("datos", datosReporte);//anadimos al map los datos del reporte
            response.setContentType("application/pdf");//indicamos que es un pdf la salida
            response.addHeader("Content-disposition", "inline; filename=ReporteSuscripciones.pdf");
            JasperPrint reportePintado = JasperFillManager.fillReport(reporte, mapaDatos, datosReporte);//pintamos el reporte
            JasperExportManager.exportReportToPdfStream(reportePintado, out);//exportamos el reporte
            out.flush();
            out.close();
        } catch (JRException ex) {

        }
    }

    public void reporteLikes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ServletOutputStream out = response.getOutputStream();//obtenemos el stream del servlet
            String nombreRevista = request.getParameter("nombreRevista");//obtenemos los parametros de la url
            String primeraFecha = request.getParameter("primeraFecha");//
            String segundaFecha = request.getParameter("segundaFecha");//
            ConsultaReporteEditor consulta = new ConsultaReporteEditor(new ConstructorDeObjeto());//nueva consulta
            ArrayList<LikeDeRevista> listadoRevistasMasGustadas = consulta.reporteRevistasMasLikeadas(nombreRevista, primeraFecha, segundaFecha);//mandmaos a traer los datos del reporte
            ArrayList<LikeDeRevista> likesPorRevista = consulta.likesPorRevista(listadoRevistasMasGustadas,primeraFecha,segundaFecha);
            listadoRevistasMasGustadas.add(0, new LikeDeRevista(null, 0, "", ""));
            JRBeanArrayDataSource masGustadas = new JRBeanArrayDataSource(listadoRevistasMasGustadas.toArray());
            JRBeanArrayDataSource porRevistas = new JRBeanArrayDataSource(likesPorRevista.toArray());
            InputStream reporteStream = getClass().getResourceAsStream("/reportes/Reporte likes.jasper");//treamos el reporte de los resorces
            JasperReport reporte = (JasperReport) JRLoader.loadObject(reporteStream);//cargamos el reorte
            Map<String, Object> mapaDatos = new HashMap<>();
            mapaDatos.put("listadoRevistasMasGustadas", masGustadas);
            mapaDatos.put("likesPorRevista", porRevistas);
            response.setContentType("application/pdf");//indicamos que es un pdf la salida
            response.addHeader("Content-disposition", "inline; filename=ReporteLikes.pdf");
            JasperPrint reportePintado = JasperFillManager.fillReport(reporte, mapaDatos, masGustadas);//pintamos el reporte
            JasperExportManager.exportReportToPdfStream(reportePintado, out);//exportamos el reporte
            out.flush();
            out.close();
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    public void reporteGanancias(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ServletOutputStream out = response.getOutputStream();//obtenemos el stream del servlet
            String nombreRevista = request.getParameter("nombreRevista");//obtenemos los parametros de la url
            String usuarioCreador = request.getParameter("usuarioCreador");//usuario creador que esta haciendo el reporte
            String primeraFecha = request.getParameter("primeraFecha");//
            String segundaFecha = request.getParameter("segundaFecha");//
            ConsultaReporteEditor consulta = new ConsultaReporteEditor(new ConstructorDeObjeto());//nueva consulta
            ArrayList<GananciaPorSuscripcion> ganancias = consulta.reporteSuscripciones(nombreRevista, usuarioCreador, primeraFecha, segundaFecha);
            ganancias.add(0, new GananciaPorSuscripcion(0, "", "", "", ""));
            Double totalGanancias = consulta.calcularTotalDeGanancias(ganancias);//calculamos las ganancias totales del las ganancias individuales 
            JRBeanArrayDataSource datosGanancias = new JRBeanArrayDataSource(ganancias.toArray());//construir un bean a partir del array devuelto por el metodo
            InputStream reporteStream = getClass().getResourceAsStream("/reportes/TotalGananciasSuscripciones.jasper");//treamos el reporte de los resorces
            JasperReport reporte = (JasperReport) JRLoader.loadObject(reporteStream);//cargamos el reorte
            Map<String, Object> mapaDatos = new HashMap<>();//map con los parametros que requiere el reporte .jasper
            mapaDatos.put("datos", datosGanancias);//anadimos al map los datos del reporte
            mapaDatos.put("total", totalGanancias);//anadimos al map los datos del reporte
            response.setContentType("application/pdf");//indicamos que es un pdf la salida
            response.addHeader("Content-disposition", "inline; filename=ReporteComentarios.pdf");
            JasperPrint reportePintado = JasperFillManager.fillReport(reporte, mapaDatos, datosGanancias);//pintamos el reporte
            JasperExportManager.exportReportToPdfStream(reportePintado, out);//exportamos el reporte
            out.flush();
            out.close();
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }
}
