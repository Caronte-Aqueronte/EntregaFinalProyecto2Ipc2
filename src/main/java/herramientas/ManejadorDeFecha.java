/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientas;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Luis Monterroso
 */
public class ManejadorDeFecha {

    public int verMesesEntreFechas(String primeraFecha, String segundaFecha) {
        //convertimos la fecha en un localdate
        LocalDate primeraFechaLocalDate = LocalDate.parse(primeraFecha);
        //convertimos la fecha en un localdate
        LocalDate segundaFechaLocalDate = LocalDate.parse(segundaFecha);
        //creamos un periodo entre las dos fechas
        Period periodo = Period.between(primeraFechaLocalDate, segundaFechaLocalDate);
        return periodo.getMonths();//retornamos los meses 
    }

    public int verAniosEntreFechas(String primeraFecha, String segundaFecha) {
        //convertimos la fecha en un localdate
        LocalDate primeraFechaLocalDate = LocalDate.parse(primeraFecha);
        //convertimos la fecha en un localdate
        LocalDate segundaFechaLocalDate = LocalDate.parse(segundaFecha);
        //creamos un periodo entre las dos fechas
        Period periodo = Period.between(primeraFechaLocalDate, segundaFechaLocalDate);
        return periodo.getYears();//retornamos los meses 
    }
    
      public int verDiasEntreFechas(String primeraFecha, String segundaFecha) {
        //convertimos la fecha en un localdate
        LocalDate primeraFechaLocalDate = LocalDate.parse(primeraFecha);
        //convertimos la fecha en un localdate
        LocalDate segundaFechaLocalDate = LocalDate.parse(segundaFecha);
        //creamos un periodo entre las dos fechas
        Period periodo = Period.between(primeraFechaLocalDate, segundaFechaLocalDate);
        return periodo.getDays();//retornamos los dias entre las dos fechas
    }
}
