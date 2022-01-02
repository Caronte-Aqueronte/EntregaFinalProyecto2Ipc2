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
public class GananciaPorAnunciante {

    private String nombreAnunciante;
    private double ganancia;

    public GananciaPorAnunciante(String nombreAnunciante, double ganancia) {
        this.nombreAnunciante = nombreAnunciante;
        this.ganancia = ganancia;
    }

    public String getNombreAnunciante() {
        return nombreAnunciante;
    }

    public void setNombreAnunciante(String nombreAnunciante) {
        this.nombreAnunciante = nombreAnunciante;
    }

    public double getGanancia() {
        return ganancia;
    }

    public void setGanancia(double ganancia) {
        this.ganancia = ganancia;
    }

}
