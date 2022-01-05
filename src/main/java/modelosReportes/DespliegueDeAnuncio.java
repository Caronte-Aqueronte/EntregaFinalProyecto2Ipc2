/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelosReportes;

import modelos.Anuncio;

/**
 *
 * @author Luis Monterroso
 */
public class DespliegueDeAnuncio extends Anuncio {

    private int vecesQueSeDesplego;

    public DespliegueDeAnuncio(int vecesQueSeDesplego, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, nombreAnunciante);
        this.vecesQueSeDesplego = vecesQueSeDesplego;
    }

    public int getVecesQueSeDesplego() {
        return vecesQueSeDesplego;
    }

    public void setVecesQueSeDesplego(int vecesQueSeDesplego) {
        this.vecesQueSeDesplego = vecesQueSeDesplego;
    }

}
