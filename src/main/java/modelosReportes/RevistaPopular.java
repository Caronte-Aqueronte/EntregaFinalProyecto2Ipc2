/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelosReportes;

import modelos.Revista;


/**
 *
 * @author Luis Monterroso
 */
public class RevistaPopular extends Revista {

    private int numeroDeSuscripciones;

    public RevistaPopular(int numeroDeSuscripciones, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.numeroDeSuscripciones = numeroDeSuscripciones;
    }

    public int getNumeroDeSuscripciones() {
        return numeroDeSuscripciones;
    }

    public void setNumeroDeSuscripciones(int numeroDeSuscripciones) {
        this.numeroDeSuscripciones = numeroDeSuscripciones;
    }

}
