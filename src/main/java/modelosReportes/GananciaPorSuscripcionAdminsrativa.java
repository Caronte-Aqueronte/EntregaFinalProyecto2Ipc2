/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelosReportes;

/**
 *
 * @author Luis Monterroso
 */
public class GananciaPorSuscripcionAdminsrativa extends GananciaPorSuscripcion{
    private double ingreso;

    public GananciaPorSuscripcionAdminsrativa(double ingreso, double totalDeGanancia, String fechaPago, String nombreUsuario, String nombreRevista, String usuarioCreador) {
        super(totalDeGanancia, fechaPago, nombreUsuario, nombreRevista, usuarioCreador);
        this.ingreso = ingreso;
    }

    public double getIngreso() {
        return ingreso;
    }

    public void setIngreso(double ingreso) {
        this.ingreso = ingreso;
    }
    
}
