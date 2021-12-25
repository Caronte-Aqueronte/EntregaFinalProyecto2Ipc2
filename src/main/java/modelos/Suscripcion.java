/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.time.LocalDate;

/**
 *
 * @author Luis Monterroso
 */
public class Suscripcion extends Revista {

    private String nombreUsuario;
    private String metodoPago;
    private String fechaSuscripcion;

    /**
     * Constructor para crear una suscripcion en general
     *
     * @param nombreUsuario
     * @param metodoPago
     * @param nombreRevista
     *
     * @param fechaSuscripcion
     * @param usuarioCreador
     */
    public Suscripcion(String nombreUsuario, String metodoPago, String fechaSuscripcion, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.nombreUsuario = nombreUsuario;
        this.metodoPago = metodoPago;
        this.fechaSuscripcion = fechaSuscripcion;
    }

    /**
     * Constructor para generar una verificacion de suscripcion a una revista o
     * inicializar un pago
     *
     * @param nombreUsuario
     * @param nombreRevista
     * @param usuarioCreador
     */
    public Suscripcion(String nombreUsuario, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getFechaSuscripcion() {
        return fechaSuscripcion;
    }

    public void setFechaSuscripcion(String fechaSuscripcion) {
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

}
