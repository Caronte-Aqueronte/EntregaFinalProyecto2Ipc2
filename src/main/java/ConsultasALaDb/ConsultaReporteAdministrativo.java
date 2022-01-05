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
import modelos.Comentario;
import modelos.CostoPorDia;
import modelos.HistorialAnuncio;
import modelos.Suscripcion;
import modelosReportes.DespliegueDeAnuncio;
import modelosReportes.DespligueDeAnunciante;
import modelosReportes.GananciaPorAnunciante;
import modelosReportes.GananciaPorSuscripcionAdminsrativa;
import modelosReportes.RevistaComentada;
import modelosReportes.RevistaPopular;
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

    private Map<String, Object> reporteDeGananciasPorRevista(String nombreRevista, String nombreUsuarioCreador, String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            String contenidoQuery;//el contenido de la query cambia si las fechas estan vacias o nulas
            PreparedStatement query;//query que se comunicara con la bd
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                contenidoQuery = "SELECT * , SUM(dinero_pago) AS ingreso, SUM(dinero_pago * 0.15) AS ganancia FROM "
                        + "pago WHERE nombre_de_revista like ? AND nombre_de_usuario_creador like ? "
                        + "AND fecha_de_pago BETWEEN ? AND ? GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador, "
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
                        + "nombre_de_usuario_creador "
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

    /**
     * Invoca a reporteGananciasPorAnunciante(), reporteDeGanancias() para
     * obtener los costos que han tenido las revistas hasta el dia de hoy, los
     * ingresos por revista y los ingresos por anuncio.
     *
     * @return
     */
    public Map<String, Object> reporteDeGananciasTotales() {

        Map<String, Object> mapaDatos = new HashMap<>();//este mapa es e, que regresaremos
        Map<String, Object> entradasYCostosPorRevista = reporteDeGananciasPorRevista("", "", "", "");
        Map<String, Object> entradasPorAnuncios = reporteGananciasPorAnunciante("", "", "");

        double gananciasNetas = (double) entradasYCostosPorRevista.get("gananciasNetas");//traemos las ganancias netas
        double gananciasPorAnuncios = (double) entradasPorAnuncios.get("totalGanancia");//traemos las ganancias de los anuncios
        double gananciasTotales = gananciasNetas + gananciasPorAnuncios;//ahora calculamos las gannacias netas de nuevo con las ganancias por los anuncios

        mapaDatos.put("gananciasTotales", gananciasTotales);
        mapaDatos.put("gananciasPorRevistas", gananciasNetas);
        mapaDatos.put("gananciasPorAnuncios", gananciasPorAnuncios);
        mapaDatos.put("totalCostos", entradasYCostosPorRevista.get("totalCosto"));
        mapaDatos.put("costosPorRevistas", entradasYCostosPorRevista.get("costosDatos"));
        mapaDatos.put("gananciasPorRevistasDatos", entradasYCostosPorRevista.get("gananciasDatos"));
        mapaDatos.put("gananciasPorAnunciosDatos", entradasPorAnuncios.get("gananciasPorAnunciante"));
        return mapaDatos;

    }

    public Map<String, Object> reporteRevistasMasPopulares(String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            PreparedStatement query;//creamos una query vacia
            //si las fechas no estan vacias o nulas entonces hacemos una qyery con periodo de tiempo
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                query = CONEXION.prepareStatement(
                        "SELECT nombre_de_revista , nombre_de_usuario_creador, "
                        + "fecha_de_suscripcion, COUNT(nombre_de_revista AND nombre_de_usuario_creador) "
                        + "AS numero_de_suscripciones FROM suscripcion WHERE fecha_de_suscripcion BETWEEN "
                        + "? AND ? GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador ORDER BY numero_de_suscripciones DESC LIMIT 5;");
                query.setString(1, primeraFecha);
                query.setString(2, segundaFecha);
            } else {//si las fechas estan nulas hacemos la query sin fechas
                query = CONEXION.prepareStatement(
                        "SELECT nombre_de_revista , nombre_de_usuario_creador, "
                        + "fecha_de_suscripcion, COUNT(nombre_de_revista AND nombre_de_usuario_creador) "
                        + "AS numero_de_suscripciones FROM suscripcion GROUP BY nombre_de_revista, "
                        + "nombre_de_usuario_creador ORDER BY numero_de_suscripciones DESC LIMIT 5;");
            }
            ResultSet resultado = query.executeQuery();
            ArrayList<RevistaPopular> revistasPopulares = getConstructorObjeto().construirRevistasPopulares(resultado);
            ArrayList<Suscripcion> suscripcionesPorRevista = new ArrayList<>();
            //por cada revista consultamos las suscripciones en el intervalo de tiempo
            for (RevistaPopular item : revistasPopulares) {
                if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                    query = CONEXION.prepareStatement(
                            "SELECT * FROM suscripcion WHERE fecha_de_suscripcion BETWEEN ? AND ? "
                            + "AND nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");
                    query.setString(1, primeraFecha);
                    query.setString(2, segundaFecha);
                    query.setString(3, item.getNombreRevista());
                    query.setString(4, item.getUsuarioCreador());
                } else {//si las fechas estan nulas hacemos la query sin fechas
                    query = CONEXION.prepareStatement(
                            "SELECT * FROM suscripcion WHERE nombre_de_revista = ? AND nombre_de_usuario_creador = ?;");
                    query.setString(1, item.getNombreRevista());
                    query.setString(2, item.getUsuarioCreador());
                }
                ResultSet resultadoSuscripciones = query.executeQuery();//eecutar la query
                //construir las suscripciones
                ArrayList<Suscripcion> suscripcionesDeRevista = getConstructorObjeto().construirSuscripciones(resultadoSuscripciones);
                suscripcionesPorRevista.addAll(suscripcionesDeRevista);//anadir el array al array principal
            }
            //crear los benas con los arraylist en array
            JRBeanArrayDataSource cincoMasPopulares = new JRBeanArrayDataSource(revistasPopulares.toArray());
            JRBeanArrayDataSource suscripcionesPorRevistaPopular = new JRBeanArrayDataSource(suscripcionesPorRevista.toArray());
            //ingresar los beans al mapa
            mapaDatos.put("cincoMasPopulares", cincoMasPopulares);
            mapaDatos.put("suscripcionesPorRevistaPopular", suscripcionesPorRevistaPopular);
            return mapaDatos;
        } catch (SQLException e) {
            return mapaDatos;
        }
    }

    public Map<String, Object> reporteRevistasMasComentadas(String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            PreparedStatement query;//creamos una query vacia
            //si las fechas no estan vacias o nulas entonces hacemos una qyery con periodo de tiempo
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                query = CONEXION.prepareStatement(
                        "SELECT nombre_de_revista , revista_nombre_de_usuario_creador, "
                        + "fecha_de_comentario, COUNT(nombre_de_revista AND revista_nombre_de_usuario_creador) "
                        + "AS numero_de_comentarios FROM comentario WHERE DATE(fecha_de_comentario) BETWEEN ? AND ? "
                        + "GROUP BY nombre_de_revista, "
                        + "revista_nombre_de_usuario_creador ORDER BY numero_de_comentarios DESC LIMIT 5;");
                query.setString(1, primeraFecha);//damos valores a los ? con los parametros del metodo
                query.setString(2, segundaFecha);//
            } else {//si las fechas estan nulas hacemos la query sin fechas
                query = CONEXION.prepareStatement(
                        "SELECT nombre_de_revista , revista_nombre_de_usuario_creador, "
                        + "fecha_de_comentario, COUNT(nombre_de_revista AND revista_nombre_de_usuario_creador) "
                        + "AS numero_de_comentarios FROM comentario GROUP BY nombre_de_revista, "
                        + "revista_nombre_de_usuario_creador ORDER BY numero_de_comentarios DESC LIMIT 5;");
            }
            ResultSet resultado = query.executeQuery();
            ArrayList<RevistaComentada> revistasMasComentadas = getConstructorObjeto().construirRevistasComentadas(resultado);
            ArrayList<Comentario> comentariosPorRevista = new ArrayList<>();
            //por cada revista comentada traemos los comentarios que existen erntre las dos fechas
            for (RevistaComentada item : revistasMasComentadas) {
                if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                    query = CONEXION.prepareStatement(
                            "SELECT * FROM comentario WHERE DATE(fecha_de_comentario) BETWEEN ? AND ? "
                            + "AND nombre_de_revista = ? AND revista_nombre_de_usuario_creador = ?;");
                    query.setString(1, primeraFecha);
                    query.setString(2, segundaFecha);
                    query.setString(3, item.getNombreRevista());
                    query.setString(4, item.getUsuarioCreador());
                } else {//si las fechas estan nulas hacemos la query sin fechas
                    query = CONEXION.prepareStatement(
                            "SELECT * FROM comentario WHERE nombre_de_revista = ? AND revista_nombre_de_usuario_creador = ?;");
                    query.setString(1, item.getNombreRevista());
                    query.setString(2, item.getUsuarioCreador());
                }
                ResultSet resultadoSuscripciones = query.executeQuery();//eecutar la query
                //construir las suscripciones
                ArrayList<Comentario> suscripcionesDeRevista = getConstructorObjeto().construirArrayDeComentarios(resultadoSuscripciones);
                comentariosPorRevista.addAll(suscripcionesDeRevista);//anadir el array al array principal
            }
            //crear los benas con los arraylist en array
            JRBeanArrayDataSource cincoMasComentadas = new JRBeanArrayDataSource(revistasMasComentadas.toArray());
            JRBeanArrayDataSource comentariosPorRevistas = new JRBeanArrayDataSource(comentariosPorRevista.toArray());
            //ingresar los beans al mapa
            mapaDatos.put("cincoMasComentadas", cincoMasComentadas);
            mapaDatos.put("comentariosPorRevistas", comentariosPorRevistas);
            return mapaDatos;
        } catch (SQLException e) {
            return mapaDatos;
        }
    }

    public Map<String, Object> reporteDeAnuncios(String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            PreparedStatement query;//creamos una query vacia
            //si las fechas no estan vacias o nulas entonces hacemos una qyery con periodo de tiempo
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                query = CONEXION.prepareStatement(
                        "SELECT * FROM proyecto_revistas.historial_anuncio WHERE fecha_de_aparicion BETWEEN ? AND ?;");
                query.setString(1, primeraFecha);//damos valores a los ? con los parametros del metodo
                query.setString(2, segundaFecha);//
            } else {//si las fechas estan nulas hacemos la query sin fechas
                query = CONEXION.prepareStatement(
                        "SELECT * FROM proyecto_revistas.historial_anuncio;");
            }
            ResultSet resultado = query.executeQuery();
            ArrayList<HistorialAnuncio> historialDeAnuncios = getConstructorObjeto().construirHistorialDeAnuncios(resultado);
            //crear los benas con los arraylist en array
            JRBeanArrayDataSource historial = new JRBeanArrayDataSource(historialDeAnuncios.toArray());
            //ingresar los beans al mapa
            mapaDatos.put("historial", historial);
            return mapaDatos;
        } catch (SQLException e) {
            return mapaDatos;
        }
    }

    public Map<String, Object> efectividadDeAnuncios(String primeraFecha, String segundaFecha) {
        Map<String, Object> mapaDatos = new HashMap<>();
        try {
            PreparedStatement query;

            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                query = CONEXION.prepareStatement(
                        "SELECT *, COUNT(nombre_anunciante AND nombre_anuncio) AS despligues_de_anunciante FROM historial_anuncio "
                        + "WHERE fecha_de_aparicion BETWEEN ? AND ? GROUP BY nombre_anunciante "
                        + "ORDER BY despligues_de_anunciante DESC;");//seleccionamos todas las tuplas que enten entre las fechas
                query.setString(1, primeraFecha);//
                query.setString(2, segundaFecha);//
            } else {
                query = CONEXION.prepareStatement(
                        "SELECT *, COUNT(nombre_anunciante AND nombre_anuncio) AS despligues_de_anunciante FROM historial_anuncio "
                        + "GROUP BY nombre_anunciante ORDER BY despligues_de_anunciante DESC;");
            }
            ResultSet resultadoDesplieguesPorAnunciantes = query.executeQuery();
            ArrayList<DespligueDeAnunciante> desplieguesDeAnunciante = getConstructorObjeto().construirDesplieguesDeAnunciante(resultadoDesplieguesPorAnunciantes);
            if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                query = CONEXION.prepareStatement(
                        "SELECT *, COUNT(nombre_anunciante AND nombre_anuncio) AS veces_que_se_desplego FROM historial_anuncio "
                        + "WHERE fecha_de_aparicion BETWEEN ? AND ? "
                        + "GROUP BY nombre_anunciante, nombre_anuncio ORDER BY veces_que_se_desplego DESC;");//seleccionamos todas las tuplas que enten entre las fechas
                query.setString(1, primeraFecha);//
                query.setString(2, segundaFecha);//
            } else {
                query = CONEXION.prepareStatement(
                        "SELECT *, COUNT(nombre_anunciante AND nombre_anuncio) AS veces_que_se_desplego FROM historial_anuncio "
                        + "GROUP BY nombre_anunciante, nombre_anuncio ORDER BY veces_que_se_desplego DESC;");
            }
            ResultSet resultado = query.executeQuery();
            ArrayList<DespliegueDeAnuncio> despligues = getConstructorObjeto().construirDesplieguesDeAnuncio(resultado);
            ArrayList<HistorialAnuncio> historialDeDespligues = new ArrayList<>();
            //por cada elemento del array consultamos los anuncios que tiene el anunciante en el peridodo de tiempo
            for (DespliegueDeAnuncio item : despligues) {
                //si no esta vacia o nula entonces hacemos una query en base a fechas
                if (primeraFecha != null && segundaFecha != null && !primeraFecha.isBlank() && !segundaFecha.isBlank()) {
                    query = CONEXION.prepareStatement(
                            "SELECT * FROM proyecto_revistas.historial_anuncio "
                            + "WHERE nombre_anunciante = ? AND nombre_anuncio = ? AND "
                            + "fecha_de_aparicion BETWEEN ? AND ?;");//seleccionamos todas las tuplas que enten entre las fechas
                    query.setString(1, item.getNombreAnunciante());//damos valores a los ? con los parametros
                    query.setString(2, item.getNombreAnuncio());//
                    query.setString(3, primeraFecha);//
                    query.setString(4, segundaFecha);//
                } else {//si no si el nombre del anunciante no esta nulo entonces procedemos con la query en base a nombre de anunciante
                    query = CONEXION.prepareStatement(
                            "SELECT * FROM proyecto_revistas.historial_anuncio "
                            + "WHERE nombre_anunciante = ? AND nombre_anuncio = ?");//seleccionamos todas las tuplas que cumplan con el nombre del anunciante              query.setString(1, "%" + nombreAnunciante + "%");
                    query.setString(1, item.getNombreAnunciante());//damos valores a los ? con los parametros
                    query.setString(2, item.getNombreAnuncio());//
                }
                ResultSet depliegue = query.executeQuery();
                ArrayList<HistorialAnuncio> historialDeDepligue = getConstructorObjeto().construirHistorialDeAnuncios(depliegue);
                historialDeDespligues.addAll(historialDeDepligue);
            }
            //construimos los beans a partir de los arrays
            JRBeanArrayDataSource vecesQueSeDesplego = new JRBeanArrayDataSource(despligues.toArray());
            JRBeanArrayDataSource historialDeDespliguesDatos = new JRBeanArrayDataSource(historialDeDespligues.toArray());
            JRBeanArrayDataSource desplieguesPorAnuncianteDatos = new JRBeanArrayDataSource(desplieguesDeAnunciante.toArray());
            mapaDatos.put("despliguesDatos", vecesQueSeDesplego);
            mapaDatos.put("historialDeDespliguesDatos", historialDeDespliguesDatos);
            mapaDatos.put("desplieguesPorAnuncianteDatos", desplieguesPorAnuncianteDatos);
            return mapaDatos;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return mapaDatos;

        }
    }
}
