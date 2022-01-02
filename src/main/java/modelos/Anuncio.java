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
public class Anuncio extends Anunciante {

    private String nombreAnuncio;
    private String tipoAnuncio;
    private double pago;
    private String estado;
    private String fechaDeCreacion;

    public Anuncio(String nombreAnuncio, String tipoAnuncio, double pago, String estado, String nombreAnunciante) {
        super(nombreAnunciante);
        this.nombreAnuncio = nombreAnuncio;
        this.tipoAnuncio = tipoAnuncio;
        this.pago = pago;
        this.estado = estado;
    }

    public Anuncio(String nombreAnuncio, String tipoAnuncio, double pago, String estado, String fechaDeCreacion, String nombreAnunciante) {
        super(nombreAnunciante);
        this.nombreAnuncio = nombreAnuncio;
        this.tipoAnuncio = tipoAnuncio;
        this.pago = pago;
        this.estado = estado;
        this.fechaDeCreacion = fechaDeCreacion;
    }

    /**
     * Constructor para heredar a Historial
     *
     * @param nombreAnuncio
     * @param nombreAnunciante
     */
    public Anuncio(String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnunciante);
        this.nombreAnuncio = nombreAnuncio;
    }

    public String getNombreAnuncio() {
        return nombreAnuncio;
    }

    public void setNombreAnuncio(String nombreAnuncio) {
        this.nombreAnuncio = nombreAnuncio;
    }

    public String getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(String tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(String fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

}
