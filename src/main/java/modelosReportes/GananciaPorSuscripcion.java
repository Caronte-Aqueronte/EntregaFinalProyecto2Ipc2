/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelosReportes;

import modelos.Pago;

/**
 *
 * @author Luis Monterroso
 */
public class GananciaPorSuscripcion extends Pago {

    private double totalDeGanancia;

    public GananciaPorSuscripcion(double totalDeGanancia, String fechaPago, String nombreUsuario, String nombreRevista, String usuarioCreador) {
        super(fechaPago, nombreUsuario, nombreRevista, usuarioCreador);
        this.totalDeGanancia = totalDeGanancia;
    }

    public double getTotalDeGanancia() {
        return totalDeGanancia;
    }

    public void setTotalDeGanancia(double totalDeGanancia) {
        this.totalDeGanancia = totalDeGanancia;
    }

}
