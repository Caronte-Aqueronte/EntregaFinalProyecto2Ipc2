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
public class RevistaComentada extends Revista {

    private int numeroDeComentarios;

    public RevistaComentada(int numeroDeComentarios, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.numeroDeComentarios = numeroDeComentarios;
    }

    public int getNumeroDeComentarios() {
        return numeroDeComentarios;
    }

    public void setNumeroDeComentarios(int numeroDeComentarios) {
        this.numeroDeComentarios = numeroDeComentarios;
    }
}
