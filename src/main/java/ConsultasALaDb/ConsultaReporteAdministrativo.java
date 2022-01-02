/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultasALaDb;

import herramientas.ConstructorDeObjeto;
import herramientas.ManejadorDeFecha;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelos.Anuncio;
import modelos.CostoPorDia;
import modelosReportes.GananciaPorAnunciante;
import modelosReportes.GananciaPorSuscripcionAdminsrativa;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Luis Monterroso
 */
public class ConsultaReporteAdministrativo extends Consulta {

    public ConsultaReporteAdministrativo(ConstructorDeObjeto constructorObjeto) {
        super(constructorObjeto);
    }

    public Map<String, Object> reporteDeGanancias(String nombreRevista, String nombreUsuarioCreador, String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            String contenidoQuery;//el contenido de la query cambia si las fechas estan vacias o nulas
            PreparedStatement query;//query que se comunicara con la bd
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                contenidoQuery = "SELECT * , SUM(dinero_pago) AS ingreso, SUM(dinero_pago * 0.15) AS ganancia FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "AND fecha_de_pago BETWEEN ? AND ? GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador, nombre_de_suscriptor "
                        + "ORDER BY ganancia DESC";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//dar valores a los ? con los parametros del metodo
                query.setString(2, "%" + nombreUsuarioCreador + "%");//
                query.setString(3, primeraFecha);//
                query.setString(4, segundaFecha);//
            } else {
                contenidoQuery = "SELECT * ,  SUM(dinero_pago) AS ingreso, SUM(dinero_pago * 0.15) AS ganancia FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador, nombre_de_suscriptor "
                        + "ORDER BY ganancia DESC";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//dar valores a los ? con los parametros del metodo
                query.setString(2, "%" + nombreUsuarioCreador + "%");//
            }
            ResultSet resultado = query.executeQuery();//ejecutar la query
            //construir el array de ganancias
            ArrayList<GananciaPorSuscripcionAdminsrativa> ganancias = getConstructorObjeto().construirGananciasPorSuscripcionAdministrativa(resultado);
            // ganancias.add(0, new GananciaPorSuscripcionAdminsrativa(0, 0, "", "", "", ""));//elemento exra para evitar error de no mostrar todos los registros
            JRBeanArrayDataSource gananciasDatos = new JRBeanArrayDataSource(ganancias.toArray());
            //calculamos el costo de cada revista contenida en el registro de ganancias
            ArrayList<CostoPorDia> costosPorRevistas = costosDeRevistas(nombreRevista, nombreUsuarioCreador, primeraFecha, segundaFecha);
            //costosPorRevistas.add(0, new CostoPorDia(0, "", "", ""));
            JRBeanArrayDataSource costosDatos = new JRBeanArrayDataSource(costosPorRevistas.toArray());
            double gananciaBruta = 0;
            double totalIngreso = 0;
            double totalCosto = 0;
            double gananciasNetas = 0;
            for (GananciaPorSuscripcionAdminsrativa item : ganancias) {
                totalIngreso += item.getIngreso();
                gananciaBruta += item.getTotalDeGanancia();
            }
            for (CostoPorDia item : costosPorRevistas) {
                totalCosto += item.getCostoPorDia();
            }
            gananciasNetas = gananciaBruta - totalCosto;
            //crear el nuevo mapa de datos a partir de los resultados
            mapaDatos.put("gananciasDatos", gananciasDatos);
            mapaDatos.put("costosDatos", costosDatos);
            mapaDatos.put("totalIngreso", totalIngreso);
            mapaDatos.put("gananciaBruta", gananciaBruta);
            mapaDatos.put("totalCosto", totalCosto);
            mapaDatos.put("gananciasNetas", gananciasNetas);

            return mapaDatos;
        } catch (SQLException e) {
            return mapaDatos;
        }
    }

    private ArrayList<CostoPorDia> costosDeRevistas(String nombreRevista, String nombreUsuarioCreador, String primeraFecha, String segundaFecha) {
        ArrayList<CostoPorDia> costosPorRevista = new ArrayList<>();
        ManejadorDeFecha manejadorDeFecha = new ManejadorDeFecha();//objeto que nos inicara el lapso de dias entre dos fechas
        try {
            String contenidoQuery;//el contenido de la query cambia si las fechas estan vacias o nulas
            PreparedStatement query;//query que se comunicara con la bd
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                contenidoQuery = "SELECT * FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "AND fecha_de_pago between ? AND ? "
                        + "GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//dar valores a los ? con los parametros del metodo
                query.setString(2, "%" + nombreUsuarioCreador + "%");//
                query.setString(3, primeraFecha);//
                query.setString(4, segundaFecha);//
            } else {
                segundaFecha = LocalDate.now().toString();// si la ultima fech esta nula o bacia entonces tomamos la fecha de hoy
                contenidoQuery = "SELECT * FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador";
                query = CONEXION.prepareStatement(contenidoQuery);
                query.setString(1, "%" + nombreRevista + "%");//dar valores a los ? con los parametros del metodo
                query.setString(2, "%" + nombreUsuarioCreador + "%");//
            }
            ResultSet resultado = query.executeQuery();//ejecutamos la query
            ArrayList<CostoPorDia> costos = new ArrayList<>();

            while (resultado.next()) {//exploramos cada tupla del resultset pues son las revistas canceladas y buscamos su costo por dia
                costos = new ArrayList<>();//por cada iteracion resetamos el array de costos
                String nombreDeRevista = resultado.getString("nombre_de_revista");
                String usuarioCreador = resultado.getString("nombre_de_usuario_creador");
                PreparedStatement queryCostos = CONEXION.prepareStatement(
                        "SELECT * FROM costo_por_dia WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?"
                        + " AND fecha_de_validez <= ?  ORDER BY fecha_de_validez ASC;");
                queryCostos.setString(1, nombreDeRevista);
                queryCostos.setString(2, usuarioCreador);
                queryCostos.setString(3, segundaFecha);
                ResultSet resultadoCostosPorDia = queryCostos.executeQuery();
                while (resultadoCostosPorDia.next()) {
                    String nombreRevistaCosto = resultadoCostosPorDia.getString("nombre_de_revista");
                    String usuarioCreadorCosto = resultadoCostosPorDia.getString("nombre_de_usuario_creador");
                    double costoPorDia = resultadoCostosPorDia.getDouble("costo_por_dia");
                    String fecha_de_validez = resultadoCostosPorDia.getString("fecha_de_validez");
                    CostoPorDia costo = new CostoPorDia(costoPorDia, fecha_de_validez, nombreRevistaCosto, usuarioCreadorCosto);
                    costos.add(costo);//agregamos al array los costos de la revista del primer resultset
                }
                //al salir del whle que crea los costos de la revista en turno de analisis exploramos el array de costos
                //para hacer la sumatoria
                double costoTotalDeRevista = 0;
                for (int x = 0; x < costos.size(); x++) {
                    int cantidadDeDiasEntreFechas = 0;//sera el multiplicador
                    if (x == (costos.size() - 1)) {//si se trata del ultimo costo entonces la comparacion la hacemos con la ultima fecha
                        cantidadDeDiasEntreFechas = manejadorDeFecha.verDiasEntreFechas(//vamos los dias en que la revista uvo este precio
                                costos.get(x).getFechaDeValidez(), segundaFecha);
                        costoTotalDeRevista += costos.get(x).getCostoPorDia() * cantidadDeDiasEntreFechas;
                    } else {//si no entonces la comparacion la hacemos con la fecha de la siguiente posicion
                        cantidadDeDiasEntreFechas = manejadorDeFecha.verDiasEntreFechas(//vamos los dias en que la revista uvo este precio
                                costos.get(x).getFechaDeValidez(), costos.get(x + 1).getFechaDeValidez());
                        costoTotalDeRevista += costos.get(x).getCostoPorDia() * cantidadDeDiasEntreFechas;
                    }
                }
                costosPorRevista.add(new CostoPorDia(costoTotalDeRevista, "", nombreDeRevista, usuarioCreador));
            }
            return costosPorRevista;
        } catch (SQLException e) {
            return costosPorRevista;
        }
    }

    public Map<String, Object> reporteGananciasPorAnunciante(String nombreAnunciante, String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            PreparedStatement query = CONEXION.prepareStatement("xx");
            PreparedStatement queryAnuncios = CONEXION.prepareStatement("xx");
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                query = CONEXION.prepareStatement("SELECT nombre_anunciante, SUM(pago) AS ganancia FROM anuncio WHERE nombre_anunciante like ? "
                        + "AND fecha_creacion BETWEEN ? AND ? "
                        + "GROUP BY (nombre_anunciante);");//seleccionamos todas las tuplas que enten entre las fechas
                query.setString(1, "%" + nombreAnunciante + "%");//damos valores a los ? con los parametros
                query.setString(2, primeraFecha);//
                query.setString(3, segundaFecha);//
            } else if (nombreAnunciante != null) {
                query = CONEXION.prepareStatement("SELECT nombre_anunciante, SUM(pago) AS ganancia FROM anuncio WHERE nombre_anunciante like ? "
                        + "GROUP BY (nombre_anunciante);");//seleccionamos todas las tuplas que enten entre las fechas
                query.setString(1, "%" + nombreAnunciante + "%");//damos valores a los ? con los parametros
            }
            ResultSet resultado = query.executeQuery();
            ArrayList<GananciaPorAnunciante> gananciasDeAnunciantes = new ArrayList<>();
            while (resultado.next()) {//crear un nuevo objeto de GananciaPorAnunciante y agregarla al aryyat
                gananciasDeAnunciantes.add(new GananciaPorAnunciante(resultado.getString("nombre_anunciante"), resultado.getDouble("ganancia")));
            }
            double totalGanancia = 0;
            ArrayList<Anuncio> anuncios = new ArrayList<>();
            //por cada elemento del array consultamos los anuncios que tiene el anunciante en el peridodo de tiempo
            for (GananciaPorAnunciante item : gananciasDeAnunciantes) {
                //si no esta vacia o nula entonces hacemos una query en base a fechas
                if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                    queryAnuncios = CONEXION.prepareStatement("SELECT * FROM anuncio WHERE nombre_anunciante like ? "
                            + "AND fecha_creacion BETWEEN ? AND ?;");//seleccionamos todas las tuplas que enten entre las fechas
                    queryAnuncios.setString(1, "%" + item.getNombreAnunciante() + "%");//damos valores a los ? con los parametros
                    queryAnuncios.setString(2, primeraFecha);//
                    queryAnuncios.setString(3, segundaFecha);//
                } else if (nombreAnunciante != null) {//si no si el nombre del anunciante no esta nulo entonces procedemos con la query en base a nombre de anunciante
                    queryAnuncios = CONEXION.prepareStatement("SELECT * FROM anuncio WHERE nombre_anunciante like ?;");//seleccionamos todas las tuplas que cumplan con el nombre del anunciante              query.setString(1, "%" + nombreAnunciante + "%");
                    queryAnuncios.setString(1, "%" + item.getNombreAnunciante() + "%");
                }
                ResultSet anunciosPorAnunciante = queryAnuncios.executeQuery();
                ArrayList<Anuncio> anunciosDeAnunciante = getConstructorObjeto().construirAnuncios(anunciosPorAnunciante);
                anuncios.addAll(anunciosDeAnunciante);
                totalGanancia += item.getGanancia();
            }

            JRBeanArrayDataSource anunciosDatos = new JRBeanArrayDataSource(anuncios.toArray());
            JRBeanArrayDataSource gananciasPorAnunciante = new JRBeanArrayDataSource(gananciasDeAnunciantes.toArray());

            mapaDatos.put("anunciosDatos", anunciosDatos);
            mapaDatos.put("totalGanancia", totalGanancia);
            mapaDatos.put("gananciasPorAnunciante", gananciasPorAnunciante);
            return mapaDatos;
        } catch (SQLException ex) {
            return mapaDatos;

        }
    }
}
