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
public class CostoPorDia extends Revista{
    private double costoPorDia;
    private String fechaDeValidez;

    public CostoPorDia(double costoPorDia, String fechaDeValidez, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.costoPorDia = costoPorDia;
        this.fechaDeValidez = fechaDeValidez;
    }

    public double getCostoPorDia() {
        return costoPorDia;
    }

    public void setCostoPorDia(double costoPorDia) {
        this.costoPorDia = costoPorDia;
    }

    public String getFechaDeValidez() {
        return fechaDeValidez;
    }

    public void setFechaDeValidez(String fechaDeValidez) {
        this.fechaDeValidez = fechaDeValidez;
    }
    
}
