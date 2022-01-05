/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelosReportes;

import modelos.Anunciante;

/**
 *
 * @author Luis Monterroso
 */
public class DespligueDeAnunciante extends Anunciante{
    private int numeroDeDespliegues;

    public DespligueDeAnunciante(int numeroDeDespliegues, String nombreAnunciante) {
        super(nombreAnunciante);
        this.numeroDeDespliegues = numeroDeDespliegues;
    }

    public int getNumeroDeDespliegues() {
        return numeroDeDespliegues;
    }

    public void setNumeroDeDespliegues(int numeroDeDespliegues) {
        this.numeroDeDespliegues = numeroDeDespliegues;
    }
    
}
