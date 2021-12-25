/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author Luis Monterroso
 */
public class Pago extends Suscripcion {

    private String fechaPago;

    /**
     * Crea un pago
     *
     * @param fechaPago
     * @param nombreUsuario
     * @param nombreRevista
     * @param usuarioCreador
     */
    public Pago(String fechaPago, String nombreUsuario, String nombreRevista, String usuarioCreador) {
        super(nombreUsuario, nombreRevista, usuarioCreador);
        this.fechaPago = fechaPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

}
